package kz.bsbnb.controller;

import kz.bsbnb.common.bean.MessageBean;
import kz.bsbnb.common.bean.ThreadBean;
import kz.bsbnb.util.SimpleResponse;

import java.util.List;

public interface IMessageController {

    SimpleResponse createThread(ThreadBean message);

    SimpleResponse createMessage(Long threadId, String strRole, MessageBean message);

    List<MessageBean> getMessages(Long threadId);

    List<ThreadBean> getAllMessages(Long userId, String strRole);

    SimpleResponse readMessage(Long messageId);

    SimpleResponse deleteMessage(Long messageId);

    List<MessageBean> getUserMessages(Long threadId, Long userId);

    Integer getUnreadMessagesCount(Long userId, String strRole);
}
