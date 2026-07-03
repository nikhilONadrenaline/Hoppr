package com.HopprBackend.backend.Service;

import com.HopprBackend.backend.Entity.User;
import com.HopprBackend.backend.Repository.UserRepository;
import com.HopprBackend.backend.Entity.UserSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisSessionService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;;

    @Autowired
    private UserRepository userRepository;

    public User validateSession(String sessionId, String userId) {

        String redisUserId =
                redisTemplate.opsForValue().get("session:" + sessionId);

        if (redisUserId == null) {
            return null;
        }

        if (!redisUserId.equals(userId)) {
            return null;
        }

        return userRepository.findById(userId).orElse(null);
    }

    public void createSession(
            String sessionId,
            String userId
    ){

        redisTemplate.opsForValue().set(

                "session:"+sessionId,

                userId,

                Duration.ofHours(24)
        );
    }

    public String getUserId(String sessionId){

        return redisTemplate.opsForValue()
                .get("session:"+sessionId);
    }

    public void deleteSession(String sessionId){

        redisTemplate.delete(
                "session:"+sessionId
        );
    }

}