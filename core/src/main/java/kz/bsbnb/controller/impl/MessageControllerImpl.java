package kz.bsbnb.controller.impl;

import kz.bsbnb.common.bean.MessageBean;
import kz.bsbnb.common.bean.ThreadBean;
import kz.bsbnb.common.consts.Role;
import kz.bsbnb.common.model.Message;
import kz.bsbnb.common.model.Organisation;
import kz.bsbnb.common.model.User;
import kz.bsbnb.common.model.UserRoles;
import kz.bsbnb.controller.IMessageController;
import kz.bsbnb.controller.IUserController;
import kz.bsbnb.repository.IMessageRepository;
import kz.bsbnb.repository.IOrganisationRepository;
import kz.bsbnb.repository.IUserRepository;
import kz.bsbnb.util.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

/**
 * Created by Ruslan on 21.10.2016
 */
@RestController
@RequestMapping(value = "/message")
public class MessageControllerImpl implements IMessageController {

    @Autowired
    IMessageRepository messageRepository;

    @Autowired
    IOrganisationRepository organisationRepository;

    @Autowired
    IUserRepository userRepository;

    @Autowired
    IUserController userController;

    @Override
    @RequestMapping(value = "/newThread", method = RequestMethod.POST)
    public SimpleResponse createThread(@RequestBody @Valid ThreadBean message) {
        Message mess = new Message();
        mess.setBody(message.getBody());
        mess.setDateCreate(new Date());
        mess.setFromUser(message.getFromUser() == null ? true : message.getFromUser());
        Organisation organisation = null;
        if (message.getOrganisationId() != null) {
            organisation = organisationRepository.findOne(message.getOrganisationId());
        }
        mess.setOrganisationId(organisation);
        mess.setSubject(message.getSubject());
        User user = null;
        if (message.getUserId() != null) {
            user = userRepository.findOne(message.getUserId());
        } else {
            return new SimpleResponse("Нет пользователя").ERROR_CUSTOM();
        }
        if (user == null) {
            return new SimpleResponse("Пользователь не найден").ERROR_CUSTOM();
        }
        mess.setUserId(user);
        mess = messageRepository.save(mess);
        message.setId(mess.getId());
        return new SimpleResponse(castToThreadBean(mess)).SUCCESS();
    }

    @Override
    @RequestMapping(value = "/newMessage/{threadId}", method = RequestMethod.POST)
    public SimpleResponse createMessage(@PathVariable Long threadId, @RequestBody @Valid MessageBean message) {
        Message mess = new Message();
        mess.setBody(message.getBody());
        mess.setDateCreate(new Date());
        mess.setFromUser(message.getFromUser() == null ? true : message.getFromUser());
        Message parent = messageRepository.findOne(threadId);
        if (parent == null) {
            return new SimpleResponse("Не найдено главное сообщение").ERROR_CUSTOM();
        }
        mess.setParentId(parent);
        Organisation organisation = null;
        if (message.getOrganisationId() != null) {
            organisation = organisationRepository.findOne(message.getOrganisationId());
        } else {
            organisation = parent.getOrganisationId();
        }
        mess.setOrganisationId(organisation);
        mess.setSubject(message.getSubject()==null?parent.getSubject():message.getSubject());
        User user = null;
        if (message.getUserId() != null) {
            user = userRepository.findOne(message.getUserId());
        } else {
            user = parent.getUserId();
        }
        mess.setUserId(user);
        mess = messageRepository.save(mess);
        message.setId(mess.getId());
        return new SimpleResponse(castToBean(mess)).SUCCESS();
    }

    /**
     * @param userId
     * @param type   FROM_USER_READ FROM_USER_UNREAD TO_USER_READ TO_USER_UNREAD
     * @return
     */
    //@Override
    //@RequestMapping(value = "/list/{type}/{userId}", method = RequestMethod.GET)
    public List<MessageBean> getMessages(@PathVariable Long userId, @PathVariable String type) {
        User user = userRepository.findOne(userId);
        List<MessageBean> result = new ArrayList<>();
        if (user != null) {
            List<Message> messages = messageRepository.findByUserId(user);
            for (Message m : messages) {
                if ((type.equals("FROM_USER_READ") && m.getFromUser() && m.getDateRead() != null)
                        || (type.equals("FROM_USER_UNREAD") && !m.getFromUser() && m.getDateRead() == null)
                        || (type.equals("TO_USER_READ") && m.getFromUser() && m.getDateRead() != null)
                        || (type.equals("TO_USER_UNREAD") && !m.getFromUser() && m.getDateRead() == null)) {
                    result.add(castToBean(m));
                }
            }
        }
        return result;
    }

    @Override
    @RequestMapping(value = "/listThread/{userId}", method = RequestMethod.GET)
    public List<ThreadBean> getAllMessages(@PathVariable Long userId) {
        User user = userRepository.findOne(userId);
        List<ThreadBean> result = new ArrayList<>();
        if (user != null) {
            List<Message> messages = messageRepository.findByUserId(user);
            for (Message m : messages) {
                if (m.getParentId() == null) {
                    result.add(castToThreadBean(m));
                }
            }
        }

        Role role = Role.ROLE_USER;
        for (UserRoles userRole : user.getUserRolesSet()) {
            Role temp = userRole.getRole();
            if (role.compareTo(temp) > 0) {
                role = temp;
            }
            if (temp.equals(Role.ROLE_OPER)) {
                List<Message> messages = messageRepository.findByOrganisationId(userRole.getOrgId());
                for (Message m : messages) {
                    if (m.getParentId() == null) {
                        result.add(castToThreadBean(m));
                    }
                }
            }
        }
        if (role.equals(Role.ROLE_ADMIN)) {
//            List<Message> messages = messageRepository.findByOrganisationId(null);
            List<Message> messages = (List<Message>) messageRepository.findAll();
            for (Message m : messages) {
                if (m.getParentId() == null) {
                    result.add(castToThreadBean(m));
                }
            }
        }

        Collections.sort(result, new Comparator<ThreadBean>() {

            @Override
            public int compare(ThreadBean o1, ThreadBean o2) {
                return o2.getDateCreate().compareTo(o1.getDateCreate());
            }
        });
        return result;
    }

    @Override
    @RequestMapping(value = "/listMessage/{threadId}", method = RequestMethod.GET)
    public List<MessageBean> getMessages(@PathVariable Long threadId) {
        Message message = messageRepository.findOne(threadId);
        List<MessageBean> result = new ArrayList<>();
        if (message != null) {
            result.add(castToBean(message));
            for (Message m : message.getMessageSet()) {
                result.add(castToBean(m));
            }
        }
        Collections.sort(result, new Comparator<MessageBean>() {

            @Override
            public int compare(MessageBean o1, MessageBean o2) {
                    return o2.getDateCreate().compareTo(o1.getDateCreate());
            }
        });
        return result;
    }

    @Override
    @RequestMapping(value = "/read/{messageId}", method = RequestMethod.POST)
    public SimpleResponse readMessage(@PathVariable Long messageId) {
        Message message = messageRepository.findOne(messageId);
        if (message == null) {
            return new SimpleResponse("Сообщения на сервере отсутствует").ERROR_CUSTOM();
        } else {
            message.setDateRead(new Date());
            message = messageRepository.save(message);
            MessageBean messageBean = castToBean(message);
            return new SimpleResponse(messageBean).SUCCESS();
        }
    }

    private MessageBean castToBean(Message message) {
        MessageBean result = new MessageBean();
        result.setDateRead(message.getDateRead());
        result.setId(message.getId());
        if (message.getParentId()!=null) {
            result.setParentId(message.getParentId().getId());
            result.setSubject(message.getSubject()==null?message.getParentId().getSubject():message.getSubject());
        } else {
            result.setSubject(message.getSubject());
        }
        result.setBody(message.getBody());
        result.setDateCreate(message.getDateCreate());
        result.setFromUser(message.getFromUser());

        if (message.getOrganisationId() != null) {
            result.setOrganisationId(message.getOrganisationId().getId());
            result.setOrganisationName(message.getOrganisationId().getOrganisationName());
        }
        if (message.getUserId() != null) {
            result.setUserId(message.getUserId().getId());
            result.setUserName(message.getUserId().getUserInfoId().getLastName());
        }
        return result;
    }

    private ThreadBean castToThreadBean(Message message) {
        ThreadBean result = new ThreadBean();
        result.setDateRead(message.getDateRead());
        result.setId(message.getId());
        result.setSubject(message.getSubject());
        result.setBody(message.getBody());
        result.setDateCreate(message.getDateCreate());
        result.setFromUser(message.getFromUser());
        result.setMessageCount(message.getMessageSet()==null?0:message.getMessageSet().size());
        if (message.getOrganisationId() != null) {
            result.setOrganisationId(message.getOrganisationId().getId());
            result.setOrganisationName(message.getOrganisationId().getOrganisationName());
        }
        if (message.getUserId() != null) {
            result.setUserId(message.getUserId().getId());
        }
        return result;
    }

    @Override
    @RequestMapping(value = "/delete/{threadId}", method = RequestMethod.DELETE)
    public SimpleResponse deleteMessage(Long messageId) {
        Message message = messageRepository.findOne(messageId);
        if (message == null) {
            return new SimpleResponse("Сообщения на сервере отсутствует").ERROR_CUSTOM();
        } else {
            messageRepository.delete(message);
            return new SimpleResponse("Сообщение удалено").SUCCESS();
        }
    }
}
