package com.HopprBackend.backend.Service;

import com.HopprBackend.backend.Dto.UserRequestDto;
import com.HopprBackend.backend.Dto.UserResponseDto;
import com.HopprBackend.backend.Dto.UserVerificationDto;
import com.HopprBackend.backend.Entity.User;
import com.HopprBackend.backend.Repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RedisSessionService redisSessionService;

    @Autowired
    private RedisTemplate<String, UserResponseDto> userRedisTemplate;

    @Autowired
    private CookieService cookieService;

    public UserResponseDto fetchUser(String userId)
    {
        UserResponseDto userResponseDtoRedis= userRedisTemplate.opsForValue().get("user:" + userId);

        if(userResponseDtoRedis!=null) return userResponseDtoRedis;

        User user=userRepository.findById(userId).orElseThrow(()->
        {
            log.error("No user Found on fetchUser");
            return new RuntimeException("No user found");
        }
        );
        UserResponseDto userResponseDto = new UserResponseDto();


        userResponseDto.setUserId(user.getUserId());
        userResponseDto.setName(user.getName());
        userResponseDto.setAvatar(user.getAvatar());

        userRedisTemplate.opsForValue().set("user:" + user.getUserId(),userResponseDto, Duration.ofHours(24));

        return userResponseDto;
    }

    public UserResponseDto createIdentity(UserRequestDto userRequestDto)
    {
        User user=new User();
        user.setAvatar(userRequestDto.getAvatar());
        user.setName(userRequestDto.getName());

        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));

        userRepository.save(user);

        UserResponseDto dto = new UserResponseDto();

        dto.setUserId(user.getUserId());
        dto.setName(user.getName());
        dto.setAvatar(user.getAvatar());
        return dto;
    }

    public UserVerificationDto verifyUser(String password, String userId)
    {
        User user=userRepository.findById(userId).orElse(null);
        if(user==null) {
                throw new RuntimeException("No user exists with this userId");
        }
        else if(passwordEncoder.matches(password,user.getPassword())){
            UserResponseDto dto = new UserResponseDto();

            dto.setUserId(user.getUserId());
            dto.setName(user.getName());
            dto.setAvatar(user.getAvatar());
            String sessionId = UUID.randomUUID().toString();

            redisSessionService.createSession(sessionId,userId);

            return new UserVerificationDto(sessionId,dto);
        }
        else throw new RuntimeException("Password is incorrect");
    }
}
