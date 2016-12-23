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
import kz.bsbnb.repository.IUserRoleRepository;
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
    IUserRoleRepository userRoleRepository;

    @Autowired
    IUserController userController;

    @Override
    @RequestMapping(value = "/newThread", method = RequestMethod.POST)
    public SimpleResponse createThread(@RequestBody @Valid ThreadBean message) {
        System.out.println("newThread start "+new Date().toString());
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
        User user;
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
        System.out.println("newThread end "+new Date().toString());
        return new SimpleResponse(castToThreadBean(mess)).SUCCESS();
    }

    @Override
    @RequestMapping(value = "/newMessage/{threadId}", method = RequestMethod.POST)
    public SimpleResponse createMessage(@PathVariable Long threadId, @RequestParam(defaultValue = "ROLE_USER") String strRole, @RequestBody @Valid MessageBean message) {
        System.out.println("newMessage/{threadId} start "+new Date().toString());
        Message mess = new Message();
        mess.setBody(message.getBody());
        mess.setDateCreate(new Date());
        mess.setFromUser(message.getFromUser() == null ? strRole.equals("ROLE_USER") : message.getFromUser());
        Message parent = messageRepository.findOne(threadId);
        if (parent == null) {
            return new SimpleResponse("Не найдено главное сообщение").ERROR_CUSTOM();
        }
        mess.setParentId(parent);
        Organisation organisation;
        if (message.getOrganisationId() != null) {
            organisation = organisationRepository.findOne(message.getOrganisationId());
        } else {
            organisation = parent.getOrganisationId();
        }
        mess.setOrganisationId(organisation);
        mess.setSubject(message.getSubject() == null ? parent.getSubject() : message.getSubject());
        User user;
        if (message.getUserId() != null) {
            user = userRepository.findOne(message.getUserId());
        } else {
            user = parent.getUserId();
        }
        mess.setUserId(user);
        parent.setDateRead(null);
        messageRepository.save(parent);
        mess = messageRepository.save(mess);
        message.setId(mess.getId());
        System.out.println("newMessage/{threadId} end "+new Date().toString());
        return new SimpleResponse(castToBean(mess)).SUCCESS();
    }


    @Override
    @RequestMapping(value = "/listThread/{userId}", method = RequestMethod.GET)
    public List<ThreadBean> getAllMessages(@PathVariable Long userId, @RequestParam(defaultValue = "ROLE_USER") String strRole) {
        System.out.println("listThread/{userId} start (role="+strRole+")"+new Date().toString());
        User user = userRepository.findOne(userId);
        List<ThreadBean> result = new ArrayList<>();
        Role role = Role.valueOf(strRole);

        if (role.equals(Role.ROLE_USER)) {
            if (user != null) {
                List<Message> messages = messageRepository.findByUserId(user);
                for (Message m : messages) {
                    if (m.getParentId() == null) {
                        ThreadBean t = castToThreadBean(m);
                        List<Message> subMessage = new ArrayList<>(m.getMessageSet());
                        Collections.sort(subMessage, new Comparator<Message>() {
                            @Override
                            public int compare(Message o1, Message o2) {
                                return o2.getDateCreate().compareTo(o1.getDateCreate());
                            }
                        });
                        if (!subMessage.isEmpty()) {
                            t.setBody(subMessage.get(0).getBody());
                            t.setDateCreate(subMessage.get(0).getDateCreate());
                            if (subMessage.get(0).getFromUser()) {
                                t.setDateRead(t.getDateRead() == null ? t.getDateCreate() : t.getDateRead());
                            }
                        } else {
                            if (t.getFromUser()) {
                                t.setDateRead(t.getDateRead() == null ? t.getDateCreate() : t.getDateRead());
                            }
                        }
                        result.add(t);
                    }
                }
            }
        } else {
            for (UserRoles userRole : user.getUserRolesSet()) {
                Role temp = userRole.getRole();
                if (temp.equals(Role.ROLE_OPER)) {
                    List<Message> messages = messageRepository.findByOrganisationId(userRole.getOrgId());
                    for (Message m : messages) {
                        if (m.getParentId() == null) {
                            ThreadBean t = castToThreadBean(m);
                            List<Message> subMessage = new ArrayList<>(m.getMessageSet());
                            Collections.sort(subMessage, new Comparator<Message>() {
                                @Override
                                public int compare(Message o1, Message o2) {
                                    return o2.getDateCreate().compareTo(o1.getDateCreate());
                                }
                            });
                            if (!subMessage.isEmpty()) {
                                t.setBody(subMessage.get(0).getBody());
                                t.setDateCreate(subMessage.get(0).getDateCreate());
                                if (!subMessage.get(0).getFromUser()) {
                                    t.setDateRead(t.getDateRead() == null ? t.getDateCreate() : t.getDateRead());
                                }
                            }
                            result.add(t);
                        }
                    }
                }
            }
        }
        if (role.equals(Role.ROLE_ADMIN)) {
//            List<Message> messages = messageRepository.findByOrganisationId(null);
            List<Message> messages = (List<Message>) messageRepository.findAllThread();
            for (Message m : messages) {
                if (m.getParentId() == null) {
                    ThreadBean t = castToThreadBean(m);
                    List<Message> subMessage = new ArrayList<>(m.getMessageSet());
                    Collections.sort(subMessage, new Comparator<Message>() {
                        @Override
                        public int compare(Message o1, Message o2) {
                            return o2.getDateCreate().compareTo(o1.getDateCreate());
                        }
                    });
                    if (!subMessage.isEmpty()) {
                        t.setBody(subMessage.get(0).getBody());
                        t.setDateCreate(subMessage.get(0).getDateCreate());
                        if (!subMessage.get(0).getFromUser()) {
                            t.setDateRead(t.getDateRead() == null ? t.getDateCreate() : t.getDateRead());
                        }
                    }
                    result.add(t);
                }
            }
        }

        Collections.sort(result, new Comparator<ThreadBean>() {

            @Override
            public int compare(ThreadBean o1, ThreadBean o2) {
                return o2.getDateCreate().compareTo(o1.getDateCreate());
            }
        });
        System.out.println("listThread/{userId} end "+new Date().toString());
        return result;
    }

    @Override
    @RequestMapping(value = "/listMessage/{threadId}", method = RequestMethod.GET)
    public List<MessageBean> getMessages(@PathVariable Long threadId) {
        System.out.println("listMessage/{threadId} start "+new Date().toString());
        Message message = messageRepository.findOne(threadId);
        List<MessageBean> result = new ArrayList<>();
        if (message != null) {
            result.add(castToBean(message));
            for (Message m : message.getMessageSet()) {
                result.add(castToBean(m));
            }
        }
        System.out.println("listMessage/{threadId} before sort "+new Date().toString());
        Collections.sort(result, new Comparator<MessageBean>() {

            @Override
            public int compare(MessageBean o1, MessageBean o2) {
                return o2.getDateCreate().compareTo(o1.getDateCreate());
            }
        });
        System.out.println("listMessage/{threadId} end "+new Date().toString());
        return result;
    }

    @Override
    @RequestMapping(value = "/unReadCount/{userId}", method = RequestMethod.GET)
    public Integer getUnreadMessagesCount(@PathVariable Long userId, @RequestParam(defaultValue = "ROLE_USER") String strRole) {
        System.out.println("unReadCount/{userId} start (role="+strRole+")"+new Date().toString());
        User user = userRepository.findOne(userId);
        Integer result = 0;
        Role role = Role.valueOf(strRole);

        if (role.equals(Role.ROLE_USER)) {
            if (user != null) {
                List<Message> messages = messageRepository.findByUserIdAndFromUser(user, false);
                for (Message m : messages) {
                    if (m.getParentId() != null && m.getDateRead() == null) {
                        result++;
                    }
                }
            }
        } else {
            for (UserRoles userRole : user.getUserRolesSet()) {
                Role temp = userRole.getRole();
                if (temp.equals(Role.ROLE_OPER)) {
                    List<Message> messages = messageRepository.findByOrganisationId(userRole.getOrgId());
                    for (Message m : messages) {
                        if (m.getParentId() != null && m.getDateRead() == null && m.getFromUser()) {
                            result++;
                        }
                    }
                }
            }
        }
        if (role.equals(Role.ROLE_ADMIN)) {
            List<Message> messages = messageRepository.findByOrganisationId(null);
            for (Message m : messages) {
                if (m.getParentId() != null && m.getDateRead() == null && m.getFromUser()) {
                    result++;
                }
            }
        }
        System.out.println("unReadCount/{userId} end "+new Date().toString());
        return result;
    }

    @Override
    @RequestMapping(value = "/listMessage/{threadId}/{userId}", method = RequestMethod.GET)
    public List<MessageBean> getUserMessages(@PathVariable Long threadId, @PathVariable Long userId) {
        System.out.println("listMessage/{threadId}/{userId} start "+new Date().toString());
        Message message = messageRepository.findOne(threadId);
        User user = userRepository.findOne(userId);
        Role role = getUserRole(user);
        List<MessageBean> result = new ArrayList<>();
        if (message != null) {
            if (role.equals(Role.ROLE_USER)) {
                if (message.getUserId().equals(user)) {
                    Message temp = message;
                    for (Message m : message.getMessageSet()) {
                        if (m.getDateCreate().after(temp.getDateCreate())) {
                            temp = m;
                        }
                    }
                    if (temp.getDateRead() == null && !temp.getFromUser()) {
                        message.setDateRead(new Date());
                        temp.setDateRead(new Date());
                        messageRepository.save(temp);
                        message = messageRepository.save(message);
                    }

                    result.add(castToBean(message));
                    for (Message m : message.getMessageSet()) {
                        result.add(castToBean(m));
                    }

                } else {
                    return result;
                }
            } else if (role.equals(Role.ROLE_OPER)) {
                if (message.getOrganisationId() == null && !message.getUserId().equals(user)) {
                    return result;
                } else {
                    if (message.getOrganisationId() != null) {
                        if (message.getUserId().equals(user)) {
                            Message temp = message;
                            for (Message m : message.getMessageSet()) {
                                if (m.getDateCreate().after(temp.getDateCreate())) {
                                    temp = m;
                                }
                            }
                            if (temp.getDateRead() == null && !temp.getFromUser()) {
                                message.setDateRead(new Date());
                                temp.setDateRead(new Date());
                                messageRepository.save(temp);
                                message = messageRepository.save(message);
                            }

                            result.add(castToBean(message));
                            for (Message m : message.getMessageSet()) {
                                result.add(castToBean(m));
                            }

                        } else {
                            List<UserRoles> userRoles = userRoleRepository.findByUserIdAndOrgId(user, message.getOrganisationId());
                            boolean isOper = false;
                            for (UserRoles userRole : userRoles) {
                                if (userRole.getRole().equals(role)) {
                                    isOper = true;
                                }
                            }
                            if (isOper) {
                                Message temp = message;
                                for (Message m : message.getMessageSet()) {
                                    if (m.getDateCreate().after(temp.getDateCreate())) {
                                        temp = m;
                                    }
                                }
                                if (temp.getDateRead() == null && temp.getFromUser()) {
                                    message.setDateRead(new Date());
                                    temp.setDateRead(new Date());
                                    messageRepository.save(temp);
                                    message = messageRepository.save(message);
                                }

                                result.add(castToBean(message));
                                for (Message m : message.getMessageSet()) {
                                    result.add(castToBean(m));
                                }
                            } else {
                                return result;
                            }
                        }
                    } else {
                        Message temp = message;
                        for (Message m : message.getMessageSet()) {
                            if (m.getDateCreate().after(temp.getDateCreate())) {
                                temp = m;
                            }
                        }
                        if (temp.getDateRead() == null && !temp.getFromUser()) {
                            message.setDateRead(new Date());
                            temp.setDateRead(new Date());
                            messageRepository.save(temp);
                            message = messageRepository.save(message);
                        }

                        result.add(castToBean(message));
                        for (Message m : message.getMessageSet()) {
                            result.add(castToBean(m));
                        }
                    }
                }
            } else {
                Message temp = message;
                for (Message m : message.getMessageSet()) {
                    if (m.getDateCreate().after(temp.getDateCreate())) {
                        temp = m;
                    }
                }
                if (temp.getDateRead() == null && temp.getFromUser() && message.getOrganisationId() == null) {
                    message.setDateRead(new Date());
                    temp.setDateRead(new Date());
                    messageRepository.save(temp);
                    message = messageRepository.save(message);
                }

                result.add(castToBean(message));
                for (Message m : message.getMessageSet()) {
                    result.add(castToBean(m));
                }
            }
        }
        System.out.println("listMessage/{threadId}/{userId} before sort "+new Date().toString());
        Collections.sort(result, new Comparator<MessageBean>() {

            @Override
            public int compare(MessageBean o1, MessageBean o2) {
                return o2.getDateCreate().compareTo(o1.getDateCreate());
            }
        });
        System.out.println("listMessage/{threadId}/{userId} end "+new Date().toString());
        return result;
    }

    private Role getUserRole(User user) {
        System.out.println("getUserRole start "+new Date().toString());
        Role result = Role.ROLE_USER;
        for (UserRoles userRoles : user.getUserRolesSet()) {
            Role temp = userRoles.getRole();
            if (result.compareTo(temp) > 0) {
                result = temp;
            }
        }
        System.out.println("getUserRole end "+new Date().toString());
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
        System.out.println("castToBean start "+new Date().toString());
        MessageBean result = new MessageBean();
        result.setDateRead(message.getDateRead());
        result.setId(message.getId());
        if (message.getParentId() != null) {
            result.setParentId(message.getParentId().getId());
            result.setSubject(message.getSubject() == null ? message.getParentId().getSubject() : message.getSubject());
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
            if (message.getUserId().getUserInfoId() != null) {
                result.setUserName(userController.getFullName(message.getUserId().getUserInfoId()));
            }
        }
        System.out.println("castToBean end "+new Date().toString());
        return result;
    }

    private ThreadBean castToThreadBean(Message message) {
        System.out.println("castToThreadBean start "+new Date().toString());
        ThreadBean result = new ThreadBean();
        result.setDateRead(message.getDateRead());
        result.setId(message.getId());
        result.setSubject(message.getSubject());
        result.setBody(message.getBody());
        result.setDateCreate(message.getDateCreate());
        result.setFromUser(message.getFromUser());
        result.setMessageCount(message.getMessageSet() == null ? 0 : message.getMessageSet().size());
        if (message.getOrganisationId() != null) {
            result.setOrganisationId(message.getOrganisationId().getId());
            result.setOrganisationName(message.getOrganisationId().getOrganisationName());
        }
        if (message.getUserId() != null) {
            result.setUserId(message.getUserId().getId());
        }
        System.out.println("castToThreadBean end "+new Date().toString());
        return result;
    }

    @Override
    @RequestMapping(value = "/delete/{threadId}", method = RequestMethod.DELETE)
    public SimpleResponse deleteMessage(@PathVariable Long threadId) {
        Message message = messageRepository.findOne(threadId);
        if (message == null) {
            return new SimpleResponse("Сообщения на сервере отсутствует").ERROR_CUSTOM();
        } else {
            messageRepository.delete(message);
            return new SimpleResponse("Сообщение удалено").SUCCESS();
        }
    }
}
