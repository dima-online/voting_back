package kz.bsbnb.processor.impl;

import kz.bsbnb.common.consts.MessageType;
import kz.bsbnb.common.model.*;
import kz.bsbnb.common.util.Validator;
import kz.bsbnb.processor.ChatProcessor;
import kz.bsbnb.processor.SecurityProcessor;
import kz.bsbnb.repository.IChatMessageRepository;
import kz.bsbnb.repository.IChatRepository;
import kz.bsbnb.repository.IThemeRepository;
import kz.bsbnb.repository.IUserRepository;
import kz.bsbnb.util.SimpleResponse;
import kz.bsbnb.util.processor.MessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Olzhas.Pazyldayev on 23.12.2017
 */
@Service
public class ChatProcessorImpl implements ChatProcessor {

    @Autowired
    IChatRepository chatRepository;
    @Autowired
    IChatMessageRepository chatMessageRepository;
    @Autowired
    MessageProcessor messageProcessor;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private SecurityProcessor securityProcessor;
    @Autowired
    private IThemeRepository themeRepository;
    @Autowired
    private IUserRepository userRepository;


    @Override
    public SimpleResponse createChat(Chat chat) {
        try {
            chat.setCreateTime(new Date());
            chatRepository.save(chat);
            return new SimpleResponse(chat.getId()).SUCCESS();
        } catch (Exception e) {
            e.printStackTrace();
            return new SimpleResponse(e.getMessage()).ERROR();
        }
    }

    public SimpleResponse updateChat(Chat chat) {
        try {
            chatRepository.save(chat);
        } catch (Exception e) {
            e.printStackTrace();
            return new SimpleResponse(e.getMessage()).ERROR();
        }
        return new SimpleResponse(messageProcessor.getMessage("chat.update.done")).SUCCESS();
    }

    public SimpleResponse deleteChat(Long id) {
        try {
            chatRepository.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new SimpleResponse(e.getMessage()).ERROR();
        }
        return new SimpleResponse(messageProcessor.getMessage("chat.delete.done")).SUCCESS();
    }

    public SimpleResponse preValidateChat(Chat chat) {
        try {
            //Chat validation
            if (chat.getId() != null) {
                Chat chat2 = chatRepository.findOne(chat.getId());
                Validator.checkObjectNotNull(chat2, messageProcessor.getMessage("error.update.chat.not.found"), true);
            }
            Validator.checkObjectNotNull(chat.getUser(), messageProcessor.getMessage("error.chat.author.not.found"), true);
            Validator.checkObjectNotNull(chat.getTheme(), messageProcessor.getMessage("error.chat.author.not.found"), true);

        } catch (Exception e) {
            e.printStackTrace();
            return new SimpleResponse(e.getMessage()).ERROR();
        }
        return null;
    }

    public SimpleResponse createFirstChatMessage(Long themeId, ChatMessage chatMessage) {


        try {

            Validator.checkObjectNotNull(chatMessage.getMessage(), messageProcessor.getMessage("error.chat.message.not.found"), true);
            if (chatMessage.getMessageType().equals(MessageType.OUTGOING)) {
                Validator.checkObjectNotNull(chatMessage.getUser(), messageProcessor.getMessage("error.user.not.found"), true);

            }
            User author = securityProcessor.getLoggedUser();
            Validator.checkObjectNotNull(author, messageProcessor.getMessage("error.user.not.found"), true);

            Theme theme = themeRepository.findOne(themeId);
            Validator.checkObjectNotNull(theme, messageProcessor.getMessage("error.update.theme.not.found"), true);

            Chat chat;
            if (chatMessage.getMessageType().equals(MessageType.OUTGOING)) {
                User user = userRepository.findOne(chatMessage.getUser().getId());
                Validator.checkObjectNotNull(user, messageProcessor.getMessage("error.user.not.found"), true);
                chat = new Chat(user, theme, new Date());
            } else {
                chat = new Chat(author, theme, new Date());
            }
            chatMessage.setChat(chat);
            chatMessage.setUser(author);
            chatMessage.setCreateTime(new Date());
            chat.getMessages().add(chatMessage);
            chatRepository.save(chat);
            return new SimpleResponse(chat.getId()).SUCCESS();
        } catch (Exception e) {
            e.printStackTrace();
            return new SimpleResponse(e.getMessage()).ERROR();
        }
    }

    public SimpleResponse createChatMessage(Long chatId, ChatMessage chatMessage) {
        try {
            User user = securityProcessor.getLoggedUser();
            Validator.checkObjectNotNull(user, messageProcessor.getMessage("error.user.not.found"), true);
            Chat chat = getChatById(chatId);
            Validator.checkObjectNotNull(chat, messageProcessor.getMessage("error.update.chat.not.found"), true);
            Validator.checkObjectNotNull(chatMessage.getMessage(), messageProcessor.getMessage("error.chat.message.not.found"), true);
            chatMessage.setChat(chat);
            chatMessage.setUser(user);
            chatMessage.setCreateTime(new Date());
            chatMessageRepository.save(chatMessage);
            return new SimpleResponse(chatMessage.getId()).SUCCESS();
        } catch (Exception e) {
            e.printStackTrace();
            return new SimpleResponse(e.getMessage()).ERROR();
        }
    }

    public void readMessage(Long chatId, MessageType messageType) {
        String fetchQuery = "select c from Chat c " +
                "LEFT JOIN FETCH c.messages m " +
                "where c.id = :id and m.status=:status and m.messageType=:messageType and m.readTime is null ";

        Chat chat = entityManager.createQuery(fetchQuery, Chat.class)
                .setParameter("id", chatId)
                .setParameter("status", Status.NEW)
                .setParameter("messageType", messageType)
                .getSingleResult();
        Validator.checkObjectNotNull(chat, messageProcessor.getMessage("error.update.chat.not.found"), true);
        if (chat.getMessages().size() > 0) {
            chat.getMessages().forEach(chatMessage -> {
                chatMessage.setStatus(Status.READ);
                chatMessage.setReadTime(new Date());
            });
            chatRepository.save(chat);
        }
    }

    @Override
    public Chat getChatById(Long id) {
        try {
            String fetchQuery = "select c from Chat c " +
                    "LEFT JOIN FETCH c.messages m " +
                    "LEFT JOIN FETCH c.user u " +
                    "where c.id = :id ";

            return entityManager.createQuery(fetchQuery, Chat.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public PageImpl<Chat> getChatListPage(int page, int pageSize) {

        BigInteger maxN = BigInteger.ZERO;
        List<Chat> list = new ArrayList<>();
        try {
            String fetchQuery = "select c from Chat c " +
                    "LEFT JOIN FETCH c.messages m " +
                    "LEFT JOIN FETCH c.user u " +
                    "order by m.status asc, m.createTime desc, c.id desc";
            String fetchQueryCount = "select count(t.id) from core.chat t ";

            Query query = entityManager.createQuery(fetchQuery);
            Query queryCount = entityManager.createNativeQuery(fetchQueryCount);

            maxN = (BigInteger) queryCount.getSingleResult();
            list = query.setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PageImpl<>(list, new PageRequest(page - 1, pageSize), maxN.longValue());
    }

    @Override
    public PageImpl<ChatMessage> getChatMessageListPage(Long chatId, int page, int pageSize) {

        BigInteger maxN = BigInteger.ZERO;
        List<ChatMessage> list = new ArrayList<>();
        try {
            User user = securityProcessor.getLoggedUser();

            String fetchQuery = "select cm from ChatMessage cm " +
                    "JOIN FETCH cm.chat c " +
                    "LEFT JOIN FETCH cm.user u " +
                    "where c.id =:chatId and c.user=:user " +
                    "and (cm.user=:user or cm.messageType=:messageType) " +
                    "order by cm.status asc, cm.createTime desc ";
            String fetchQueryCount = "select count(t.id) from core.chat_message t " +
                    "left join core.chat c on t.chat_id = c.id " +
                    "where t.chat_id =:chat_id and c.user_id =:user_id " +
                    "and (t.user_id =:user_id or t.message_type =:message_type)";

            Query query = entityManager.createQuery(fetchQuery)
                    .setParameter("chatId", chatId)
                    .setParameter("user", user)
                    .setParameter("messageType", MessageType.OUTGOING);
            Query queryCount = entityManager.createNativeQuery(fetchQueryCount)
                    .setParameter("chat_id", chatId)
                    .setParameter("user_id", user.getId())
                    .setParameter("message_type", MessageType.OUTGOING.name());

            maxN = (BigInteger) queryCount.getSingleResult();
            list = query.setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PageImpl<>(list, new PageRequest(page - 1, pageSize), maxN.longValue());
    }

    @Override
    public PageImpl<ChatMessage> getChatMessageListPageAdmin(Long chatId, int page, int pageSize) {

        BigInteger maxN = BigInteger.ZERO;
        List<ChatMessage> list = new ArrayList<>();
        try {
            String fetchQuery = "select cm from ChatMessage cm " +
                    "JOIN FETCH cm.chat c " +
                    "LEFT JOIN FETCH cm.user u " +
                    "where c.id =:chatId " +
                    "order by cm.status asc, cm.createTime desc ";
            String fetchQueryCount = "select count(t.id) from core.chat_message t " +
                    "where t.chat_id =:chatId";

            Query query = entityManager.createQuery(fetchQuery)
                    .setParameter("chatId", chatId);
            Query queryCount = entityManager.createNativeQuery(fetchQueryCount)
                    .setParameter("chatId", chatId);

            maxN = (BigInteger) queryCount.getSingleResult();
            list = query.setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PageImpl<>(list, new PageRequest(page - 1, pageSize), maxN.longValue());
    }

    @Override
    public PageImpl<ChatMessage> getChatMessageListThemePage(Long themeId, int page, int pageSize) {

        BigInteger maxN = BigInteger.ZERO;
        List<ChatMessage> list = new ArrayList<>();
        try {
            User user = securityProcessor.getLoggedUser();

            String fetchQuery = "select cm from ChatMessage cm " +
                    "JOIN FETCH cm.chat c " +
                    "LEFT JOIN FETCH cm.user u " +
                    "where c.theme.id=:themeId and c.user=:user " +
                    "and (cm.user=:user or cm.messageType=:messageType) " +
                    "order by cm.status asc, cm.createTime desc ";
            String fetchQueryCount = "select count(t.id) from core.chat_message t " +
                    "left join core.chat c on t.chat_id = c.id " +
                    "where c.theme_id =:theme_id and c.user_id =:user_id " +
                    "and (t.user_id =:user_id or t.message_type =:message_type)";

            Query query = entityManager.createQuery(fetchQuery)
                    .setParameter("themeId", themeId)
                    .setParameter("user", user)
                    .setParameter("messageType", MessageType.OUTGOING);
            Query queryCount = entityManager.createNativeQuery(fetchQueryCount)
                    .setParameter("theme_id", themeId)
                    .setParameter("user_id", user.getId())
                    .setParameter("message_type", MessageType.OUTGOING.name());

            maxN = (BigInteger) queryCount.getSingleResult();
            list = query.setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PageImpl<>(list, new PageRequest(page - 1, pageSize), maxN.longValue());
    }

    private String transformStringParameter(String text) {
        if (text == null || text.equals("") || text == "") return "%";
        return "%" + text + "%";
    }
}
