package kz.bsbnb.controller.impl;

import kz.bsbnb.common.consts.MessageType;
import kz.bsbnb.controller.IChatController;
import kz.bsbnb.processor.ChatProcessor;
import kz.bsbnb.util.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
        return new SimpleResponse(chatProcessor.getChatById(id)).SUCCESS();
    }

    @Override
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public SimpleResponse getChatListPage(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                          @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
        return new SimpleResponse(chatProcessor.getChatListPage(page, pageSize)).SUCCESS();
    }

    //********************* CHAT_MESSAGE ************************//

    @Override
    @RequestMapping(value = "/message/first/{themeId}", method = RequestMethod.POST)
    public SimpleResponse createFirstChatMessage(@PathVariable Long themeId,
                                                 @RequestParam(name = "message") String message) {
        return chatProcessor.createFirstChatMessage(themeId, message);
    }

    @Override
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "/message/admin/first/{themeId}", method = RequestMethod.POST)
    public SimpleResponse createFirstChatMessageAdmin(@PathVariable Long themeId,
                                                      @RequestParam(name = "userId") Long userId,
                                                      @RequestParam(name = "message") String message) {
        return chatProcessor.createFirstChatMessageAdmin(themeId, userId, message);
    }

    @Override
    @RequestMapping(value = "/message/{chatId}", method = RequestMethod.POST)
    public SimpleResponse createChatMessage(@PathVariable Long chatId,
                                            @RequestParam(name = "message") String message) {
        return chatProcessor.createChatMessage(chatId, message, MessageType.INCOMING);
    }

    @Override
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "/message/admin/{chatId}", method = RequestMethod.POST)
    public SimpleResponse createChatMessageAdmin(@PathVariable Long chatId,
                                                 @RequestParam(name = "message") String message) {
        return chatProcessor.createChatMessage(chatId, message, MessageType.OUTGOING);
    }

    @Override
    @RequestMapping(value = "/message/read/{chatId}", method = RequestMethod.POST)
    public void readMessage(Long chatId) {
        chatProcessor.readMessage(chatId, MessageType.OUTGOING);
    }

    @Override
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "/message/admin/read/{chatId}", method = RequestMethod.POST)
    public void readMessageAdmin(Long chatId) {
        chatProcessor.readMessage(chatId, MessageType.INCOMING);
    }

    @Override
    @RequestMapping(value = "/message/list", method = RequestMethod.GET)
    public SimpleResponse getChatMessageListPage(@RequestParam(name = "chatId", required = true) Long chatId,
                                                 @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                                 @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
        return new SimpleResponse(chatProcessor.getChatMessageListPage(chatId, page, pageSize));
    }

    @Override
    //@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "/message/admin/list", method = RequestMethod.GET)
    public SimpleResponse getChatMessageListPageAdmin(@RequestParam(name = "chatId", required = true) Long chatId,
                                                      @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                                      @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
        return new SimpleResponse(chatProcessor.getChatMessageListPageAdmin(chatId, page, pageSize));
    }
}
