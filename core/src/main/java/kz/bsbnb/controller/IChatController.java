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

    SimpleResponse createFirstChatMessage(Long themeId, ChatMessage chatMessage);

    SimpleResponse createFirstChatMessageAdmin(Long themeId, ChatMessage chatMessage);

    SimpleResponse createChatMessage(Long chatId, ChatMessage chatMessage);

    SimpleResponse createChatMessageAdmin(Long chatId, ChatMessage chatMessage);

    void readMessage(Long chatId);

    void readMessageAdmin(Long chatId);

    SimpleResponse getChatById(Long id);

    PageImpl<Chat> getChatListPage(int page, int pageSize);

    PageImpl<ChatMessage> getChatMessageListPage(Long chatId, int page, int pageSize);

    PageImpl<ChatMessage> getChatMessageListPageAdmin(Long chatId, int page, int pageSize);

    PageImpl<ChatMessage> getChatMessageListThemePage(Long themeId, int page, int pageSize);;
}
