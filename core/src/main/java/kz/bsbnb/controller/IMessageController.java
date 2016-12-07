package kz.bsbnb.controller;

import kz.bsbnb.common.bean.MessageBean;
import kz.bsbnb.common.bean.ThreadBean;
import kz.bsbnb.util.SimpleResponse;

import java.util.List;

/**
 * Created by Ruslan on 21.10.2016.
 */
public interface IMessageController {

    SimpleResponse createThread(ThreadBean message);

    SimpleResponse createMessage(Long threadId, MessageBean message);

    List<MessageBean> getMessages(Long threadId);

    List<ThreadBean> getAllMessages(Long userId);

    SimpleResponse readMessage(Long messageId);

    SimpleResponse deleteMessage(Long messageId);
}
