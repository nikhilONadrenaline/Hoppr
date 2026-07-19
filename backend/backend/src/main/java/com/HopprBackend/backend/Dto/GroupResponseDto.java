package com.HopprBackend.backend.Dto;

import com.HopprBackend.backend.Enum.ConversationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupResponseDto {
    private String adminId;
    private List<String> memberIds;
    private String name;

    private ConversationType conversationType; // PRIVATE OR GROUP
    private LocalDateTime lastActive;
}
