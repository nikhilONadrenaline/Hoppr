package com.HopprBackend.backend.Service;

import com.HopprBackend.backend.Entity.Message;
import com.HopprBackend.backend.Repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public List<Message> showMessage(String groupId)
    {
        return messageRepository.findByGroupId(groupId);
    }

    public void deleteMessage(String messageId)
    {
        messageRepository.deleteById(messageId);
    }
}
