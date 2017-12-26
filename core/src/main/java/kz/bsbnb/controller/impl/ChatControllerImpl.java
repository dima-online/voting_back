package kz.bsbnb.controller.impl;

import kz.bsbnb.common.consts.MessageType;
import kz.bsbnb.common.model.Chat;
import kz.bsbnb.common.model.ChatMessage;
import kz.bsbnb.controller.IChatController;
import kz.bsbnb.processor.ChatProcessor;
import kz.bsbnb.util.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Olzhas.Pazlydayev on 23.12.2017.
 */
@RestController
@RequestMapping(value = "/chat")
public class ChatControllerImpl implements IChatController {

    @Autowired
    ChatProcessor chatProcessor;

    //********************* CHAT ************************//

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public SimpleResponse getChatById(@PathVariable Long id) {
        Chat chat = chatProcessor.getChatById(id);
        if (chat != null) {
            return new SimpleResponse(chat).SUCCESS();
        }
        return new SimpleResponse("Chat not found").ERROR();
    }

    @RequestMapping(value = "/theme/{themeId}", method = RequestMethod.GET)
    public SimpleResponse getChatByThemeId(@PathVariable Long themeId) {
        Chat chat = chatProcessor.getChatByThemeId(themeId);
        if (chat != null) {
            return new SimpleResponse(chat).SUCCESS();
        }
        return new SimpleResponse("Chat not found").ERROR();
    }

    @Override
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public PageImpl<Chat> getChatListPage(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                          @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
        return chatProcessor.getChatListPage(page, pageSize);
    }

    //********************* CHAT_MESSAGE ************************//

    @Override
    @RequestMapping(value = "/message/first/{themeId}", method = RequestMethod.POST)
    public SimpleResponse createFirstChatMessage(@PathVariable Long themeId,
                                                 @RequestBody ChatMessage chatMessage) {
        chatMessage.setMessageType(MessageType.INCOMING);
        return chatProcessor.createFirstChatMessage(themeId, chatMessage);
    }

    @Override
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "/message/admin/first/{themeId}", method = RequestMethod.POST)
    public SimpleResponse createFirstChatMessageAdmin(@PathVariable Long themeId,
                                                      @RequestBody ChatMessage chatMessage) {
        chatMessage.setMessageType(MessageType.OUTGOING);
        return chatProcessor.createFirstChatMessage(themeId, chatMessage);
    }

    @Override
    @RequestMapping(value = "/message/{chatId}", method = RequestMethod.POST)
    public SimpleResponse createChatMessage(@PathVariable Long chatId,
                                            @RequestBody ChatMessage chatMessage) {
        chatMessage.setMessageType(MessageType.INCOMING);
        return chatProcessor.createChatMessage(chatId, chatMessage);
    }

    @Override
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "/message/admin/{chatId}", method = RequestMethod.POST)
    public SimpleResponse createChatMessageAdmin(@PathVariable Long chatId,
                                                 @RequestBody ChatMessage chatMessage) {
        chatMessage.setMessageType(MessageType.OUTGOING);
        return chatProcessor.createChatMessage(chatId, chatMessage);
    }

    @Override
    @RequestMapping(value = "/message/read/{chatId}", method = RequestMethod.POST)
    public void readMessage(@PathVariable Long chatId) {
        chatProcessor.readMessage(chatId, MessageType.OUTGOING);
    }

    @Override
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "/message/admin/read/{chatId}", method = RequestMethod.POST)
    public void readMessageAdmin(@PathVariable Long chatId) {
        chatProcessor.readMessage(chatId, MessageType.INCOMING);
    }

    @Override
    @RequestMapping(value = "/message/list", method = RequestMethod.GET)
    public PageImpl<ChatMessage> getChatMessageListPage(@RequestParam(name = "chatId", required = true) Long chatId,
                                                        @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                                        @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
        return chatProcessor.getChatMessageListPage(chatId, page, pageSize);
    }

    @Override
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "/message/admin/list", method = RequestMethod.GET)
    public PageImpl<ChatMessage> getChatMessageListPageAdmin(@RequestParam(name = "chatId", required = true) Long chatId,
                                                             @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                                             @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
        return chatProcessor.getChatMessageListPageAdmin(chatId, page, pageSize);
    }

    @Override
    @RequestMapping(value = "/message/list/theme", method = RequestMethod.GET)
    public PageImpl<ChatMessage> getChatMessageListThemePage(@RequestParam(name = "themeId", required = true) Long themeId,
                                                             @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                                             @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
        return chatProcessor.getChatMessageListThemePage(themeId, page, pageSize);
    }
}
