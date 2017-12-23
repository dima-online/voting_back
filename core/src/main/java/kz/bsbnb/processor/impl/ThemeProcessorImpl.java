package kz.bsbnb.processor.impl;

import kz.bsbnb.common.model.Status;
import kz.bsbnb.common.model.Theme;
import kz.bsbnb.common.util.Validator;
import kz.bsbnb.processor.ChatProcessor;
import kz.bsbnb.processor.ThemeProcessor;
import kz.bsbnb.repository.IThemeRepository;
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
 * Created by Olzhas.Pazyldayev on 22.12.2017
 */
@Service
public class ThemeProcessorImpl implements ThemeProcessor {

    @Autowired
    IThemeRepository themeRepository;
    @Autowired
    MessageProcessor messageProcessor;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public SimpleResponse createTheme(Theme theme) {
        try {
            theme.setCreateTime(new Date());
            themeRepository.save(theme);
        } catch (Exception e) {
            e.printStackTrace();
            return new SimpleResponse(e.getMessage()).ERROR();
        }
        return new SimpleResponse(messageProcessor.getMessage("theme.create.done")).SUCCESS();
    }

    public SimpleResponse updateTheme(Theme theme) {
        try {
            themeRepository.save(theme);
        } catch (Exception e) {
            e.printStackTrace();
            return new SimpleResponse(e.getMessage()).ERROR();
        }
        return new SimpleResponse(messageProcessor.getMessage("theme.update.done")).SUCCESS();
    }

    public SimpleResponse deleteTheme(Long id) {
        try {
            themeRepository.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new SimpleResponse(e.getMessage()).ERROR();
        }
        return new SimpleResponse(messageProcessor.getMessage("theme.delete.done")).SUCCESS();
    }

    public SimpleResponse preValidateTheme(Theme theme) {
        try {
            //Theme validation
            if (theme.getId() != null) {
                Theme theme2 = themeRepository.findOne(theme.getId());
                Validator.checkObjectNotNull(theme2, messageProcessor.getMessage("error.update.theme.not.found"), true);
            }
            Validator.checkObjectNotNull(theme.getAuthor(), messageProcessor.getMessage("error.theme.author.not.found"), true);
            Validator.checkObjectNotNull(theme.getVoting(), messageProcessor.getMessage("error.theme.voting.not.found"), true);

            Validator.checkCollectionNullOrEmpty(theme.getMessages(), messageProcessor.getMessage("error.theme.messages.not.found"), true);
            theme.getMessages().forEach(themeMessage -> {
                themeMessage.setTheme(theme);
            });

        } catch (Exception e) {
            e.printStackTrace();
            return new SimpleResponse(e.getMessage()).ERROR();
        }
        return null;
    }

    @Override
    public Theme getThemeById(Long id) {
        return themeRepository.findOne(id);
    }

    @Override
    public Theme getThemeByIdLocale(Long id) {
        try {
            String fetchQuery = "select t from Theme t " +
                    "LEFT JOIN FETCH t.messages m " +
                    "where m.locale = :locale " +
                    "and t.status=:status " +
                    "and t.id =:id " +
                    "order by t.id desc";

            return entityManager.createQuery(fetchQuery, Theme.class)
                    .setParameter("locale", messageProcessor.getCurrentLocale())
                    .setParameter("status", Status.ACTIVE)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Theme getThemeByVotingIdLocale(Long votingId) {
        try {
            String fetchQuery = "select t from Theme t " +
                    "LEFT JOIN FETCH t.messages m " +
                    "JOIN FETCH t.voting v " +
                    "where m.locale = :locale " +
                    "and t.status=:status " +
                    "and v.id =:votingId " +
                    "order by t.id desc";

            return entityManager.createQuery(fetchQuery, Theme.class)
                    .setParameter("locale", messageProcessor.getCurrentLocale())
                    .setParameter("status", Status.ACTIVE)
                    .setParameter("votingId", votingId)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public PageImpl<Theme> getThemeListPage(String text, int page, int pageSize) {

        BigInteger maxN = BigInteger.ZERO;
        List<Theme> list = new ArrayList<>();
        try {
            String fetchQuery = "select t from Theme t " +
                    "LEFT JOIN FETCH t.messages m " +
                    "where m.locale = :locale AND (m.title like :text or t.message like :text) " +
                    "and t.status=:status " +
                    "order by t.id desc";

            String fetchQueryCount = "select count(t.id) from core.theme t " +
                    "left join core.theme_message m on m.theme_id = t.id " +
                    "where m.locale =:locale and s.status=:status and (m.title like :text or m.message like :text )";


            Query query = entityManager.createQuery(fetchQuery)
                    .setParameter("locale", messageProcessor.getCurrentLocale())
                    .setParameter("status", Status.ACTIVE)
                    .setParameter("text", transformStringParameter(text));

            Query queryCount = entityManager.createNativeQuery(fetchQueryCount)
                    .setParameter("locale", messageProcessor.getCurrentLocale().name())
                    .setParameter("status", Status.ACTIVE.name())
                    .setParameter("text", transformStringParameter(text));

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
    public PageImpl<Theme> getThemeListPageAdmin(String text, int page, int pageSize) {

        BigInteger maxN = BigInteger.ZERO;
        List<Theme> list = new ArrayList<>();
        try {
            String fetchQuery = "select t from Theme t " +
                    "LEFT JOIN FETCH t.messages m " +
                    "where m.locale = :locale AND (m.title like :text or t.message like :text) " +
                    "and t.status=:status " +
                    "order by t.id desc";

            String fetchQueryCount = "select count(t.id) from core.theme t " +
                    "left join core.theme_message m on m.theme_id = t.id " +
                    "where m.locale =:locale and s.status=:status and (m.title like :text or m.message like :text )";


            Query query = entityManager.createQuery(fetchQuery)
                    .setParameter("locale", messageProcessor.getCurrentLocale())
                    .setParameter("status", Status.ACTIVE)
                    .setParameter("text", transformStringParameter(text));

            Query queryCount = entityManager.createNativeQuery(fetchQueryCount)
                    .setParameter("locale", messageProcessor.getCurrentLocale().name())
                    .setParameter("status", Status.ACTIVE.name())
                    .setParameter("text", transformStringParameter(text));

            maxN = (BigInteger) queryCount.getSingleResult();

            list = query.setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();

            list.forEach(theme -> {
                theme.setNewMessage(getThemeNewMessageCount(theme.getId()));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PageImpl<>(list, new PageRequest(page - 1, pageSize), maxN.longValue());
    }

    @Override
    public Integer getThemeNewMessageCount(Long themeId) {

        BigInteger maxN = BigInteger.ZERO;
        try {
            String fetchQueryCount = "select count(t.id) from core.chat_message t " +
                    "left join core.chat c on t.chat_id = c.id " +
                    "where t.status='NEW' and t.message_type='INCOMING' and c.theme_id =:theme_id";

            Query queryCount = entityManager.createNativeQuery(fetchQueryCount)
                    .setParameter("theme_id", themeId);

            maxN = (BigInteger) queryCount.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maxN.intValue();
    }


    private String transformStringParameter(String text) {
        if (text == null || text.equals("") || text == "") return "%";
        return "%" + text + "%";
    }
}
