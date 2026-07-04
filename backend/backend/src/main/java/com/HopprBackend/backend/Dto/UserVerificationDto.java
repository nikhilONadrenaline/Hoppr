package com.HopprBackend.backend.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVerificationDto {
    private String sessionId;
    private UserResponseDto userResponseDto;
}
