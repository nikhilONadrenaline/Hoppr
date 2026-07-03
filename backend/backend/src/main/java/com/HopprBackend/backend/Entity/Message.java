package com.HopprBackend.backend.Entity;

import com.HopprBackend.backend.Enum.MessageStatus;
import com.HopprBackend.backend.Enum.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @Id
    private String id;

    private String senderId;
    private String groupId;
    private LocalDateTime timeOfMessage;
    private MessageType messageType;
    private MessageStatus status;

    private String message;
    private String filePath;          // e.g. storage/videos/a8f73d.mp4
    private String originalFileName;  // vacation.mp4
    private String mimeType;          // video/mp4, image/png, audio/mpeg
    private long fileSize;
}
