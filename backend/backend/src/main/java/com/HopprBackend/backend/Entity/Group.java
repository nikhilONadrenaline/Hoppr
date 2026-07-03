package com.HopprBackend.backend.Entity;

import com.HopprBackend.backend.Enum.ConversationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    @Id
    private String groupId;

    private String adminId;
    private List<String> memberIds;

    private ConversationType conversationType; // PRIVATE OR GROUP
    private LocalDateTime lastActive;
}
