package kz.bsbnb.processor;

import kz.bsbnb.common.consts.MessageType;
import kz.bsbnb.common.model.Chat;
import kz.bsbnb.common.model.ChatMessage;
import kz.bsbnb.util.SimpleResponse;
import org.springframework.data.domain.PageImpl;

/**
 * Created by Olzhas.Pazlydayev on 23.12.2017.
 */
public interface ChatProcessor {

    SimpleResponse createChat(Chat chat);

    SimpleResponse updateChat(Chat chat);

    SimpleResponse deleteChat(Long id);

    SimpleResponse preValidateChat(Chat chat);

    SimpleResponse createFirstChatMessage(Long themeId, ChatMessage chatMessage);

    SimpleResponse createChatMessage(Long chatId, ChatMessage chatMessage);

    void readMessage(Long chatId, MessageType messageType);

    Chat getChatById(Long id);

    Chat getChatByThemeId(Long themeId);

    PageImpl<Chat> getChatListPage(int page, int pageSize);

    PageImpl<ChatMessage> getChatMessageListPage(Long chatId, int page, int pageSize);

    PageImpl<ChatMessage> getChatMessageListPageAdmin(Long chatId, int page, int pageSize);

    PageImpl<ChatMessage> getChatMessageListThemePage(Long themeId, int page, int pageSize);
}
