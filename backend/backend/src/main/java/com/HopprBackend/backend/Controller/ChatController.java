package com.HopprBackend.backend.Controller;

import com.HopprBackend.backend.Entity.Message;
import com.HopprBackend.backend.Service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {
    @Autowired
    private MessageService messageService;

    @PostMapping("/showMessages/{groupId}")
    public List<Message> showMessage(@PathVariable String groupId){
        return messageService.showMessage(groupId);
    }

    @PostMapping("/deleteMessage/{groupId}")
    public void deleteMessage(@PathVariable String messageId){
        messageService.deleteMessage(messageId);
    }


}
