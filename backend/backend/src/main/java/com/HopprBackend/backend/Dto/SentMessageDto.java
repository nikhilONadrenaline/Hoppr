package com.HopprBackend.backend.Dto;

import com.HopprBackend.backend.Enum.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SentMessageDto {
    private String groupId;
    private MessageType messageType;

    private String message;

    private String filePath;
    private String originalFileName;
    private String mimeType;
    private long fileSize;
}
