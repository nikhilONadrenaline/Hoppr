package com.HopprBackend.backend.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    @Id
    private String userId;

    private String name;
    private String avatar;

}

