package com.HopprBackend.backend.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApproveUser {
    @Id
    private String id;
    private String groupId;
    private String userId;
}
