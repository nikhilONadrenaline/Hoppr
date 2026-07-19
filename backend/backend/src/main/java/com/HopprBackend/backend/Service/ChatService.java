package com.HopprBackend.backend.Service;

import com.HopprBackend.backend.Dto.ReceivedMessageDto;
import com.HopprBackend.backend.Dto.SentMessageDto;
import com.HopprBackend.backend.Entity.Message;
import com.HopprBackend.backend.Enum.MessageStatus;
import com.HopprBackend.backend.Repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

public class ChatService {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private  SessionService sessionService;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private GroupService groupService;

    public void handleIncomingMessage(WebSocketSession session, String payload) {

        SentMessageDto messageDto =objectMapper.readValue(payload, SentMessageDto.class);
        String senderId =sessionService.getUserId(session);

        Message message = getMessage(messageDto, senderId);
        Message savedMessage= messageRepository.save(message);  // ID from mongoDb


        ReceivedMessageDto response = getReceivedMessageDto(savedMessage);

        String json =objectMapper.writeValueAsString(response);
        List<String> members =groupService.getMembers(savedMessage.getGroupId());

        for (String memberId : members) {
            WebSocketSession receiver =sessionService.getSession(memberId);

            if (receiver != null && receiver.isOpen()) {
                try {
                    receiver.sendMessage(new TextMessage(json));
                } catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static Message getMessage(SentMessageDto messageDto, String senderId) {
        Message message = new Message();
        message.setSenderId(senderId);
        message.setGroupId(messageDto.getGroupId());
        message.setMessage(messageDto.getMessage());
        message.setMessageType(messageDto.getMessageType());
        message.setStatus(MessageStatus.SENT);
        message.setTimeOfMessage(LocalDateTime.now());
        message.setFilePath(messageDto.getFilePath());
        message.setOriginalFileName(messageDto.getOriginalFileName());
        message.setMimeType(messageDto.getMimeType());
        message.setFileSize(messageDto.getFileSize());
        return message;
    }
    private static ReceivedMessageDto getReceivedMessageDto(Message savedMessage) {
        ReceivedMessageDto response =new ReceivedMessageDto();
        response.setId(savedMessage.getId());
        response.setSenderId(savedMessage.getSenderId());
        response.setGroupId(savedMessage.getGroupId());
        response.setTimeOfMessage(savedMessage.getTimeOfMessage());
        response.setStatus(savedMessage.getStatus());
        response.setMessage(savedMessage.getMessage());
        response.setMessageType(savedMessage.getMessageType());
        response.setFilePath(savedMessage.getFilePath());
        response.setOriginalFileName(savedMessage.getOriginalFileName());
        response.setMimeType(savedMessage.getMimeType());
        response.setFileSize(savedMessage.getFileSize());
        return response;
    }
}
