package com.HopprBackend.backend.Dto;

import com.HopprBackend.backend.Enum.MessageStatus;
import com.HopprBackend.backend.Enum.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceivedMessageDto {
    private String id;

    private String senderId;

    private String groupId;

    private LocalDateTime timeOfMessage;

    private MessageType messageType;

    private MessageStatus status;

    private String message;

    private String filePath;
    private String originalFileName;
    private String mimeType;
    private long fileSize;
}
