package kz.bsbnb.controller;

import kz.bsbnb.common.consts.MessageType;
import kz.bsbnb.common.model.Chat;
import kz.bsbnb.common.model.ChatMessage;
import kz.bsbnb.util.SimpleResponse;
import org.springframework.data.domain.PageImpl;

/**
 * Created by Olzhas.Pazlydayev on 22.12.2017.
 */
public interface IChatController {

    SimpleResponse createFirstChatMessage(Long themeId, String message);

    SimpleResponse createFirstChatMessageAdmin(Long themeId, Long userId, String message);

    SimpleResponse createChatMessage(Long chatId, String message);

    SimpleResponse createChatMessageAdmin(Long chatId, String message);

    void readMessage(Long chatId);

    void readMessageAdmin(Long chatId);

    SimpleResponse getChatById(Long id);

    SimpleResponse getChatListPage(int page, int pageSize);

    SimpleResponse getChatMessageListPage(Long chatId, int page, int pageSize);

    SimpleResponse getChatMessageListPageAdmin(Long chatId, int page, int pageSize);
}
