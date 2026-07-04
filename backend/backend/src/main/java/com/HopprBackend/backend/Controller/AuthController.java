package com.HopprBackend.backend.Controller;

import com.HopprBackend.backend.Dto.AuthCookieDto;
import com.HopprBackend.backend.Dto.UserRequestDto;
import com.HopprBackend.backend.Dto.UserResponseDto;
import com.HopprBackend.backend.Dto.UserVerificationDto;
import com.HopprBackend.backend.Enum.LoginStatus;
import com.HopprBackend.backend.Service.CookieService;
import com.HopprBackend.backend.Service.RedisSessionService;
import com.HopprBackend.backend.Service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private CookieService cookieService;

    @Autowired
    private RedisSessionService redisSessionService;

    @GetMapping("/login")
    public LoginStatus login(@CookieValue(name = "auth", required = false) String authCookie)
    {
        if(authCookie==null) return LoginStatus.CREATE_NEW;

        AuthCookieDto authCookieDto=cookieService.readCookie(authCookie);

        String userId=authCookieDto.getUserId();
        String sessionId=authCookieDto.getSessionId();
        String redisUserId= redisSessionService.getUserId(sessionId);

        if(redisUserId==null || !redisUserId.equals(userId)) {
            return LoginStatus.PASSWORD_NEEDED; // Create new session for this user id (also a new session will be created for user which doesnt exist in mongoDB if userId was tempered by someone)
        }

        return LoginStatus.SESSION_EXISTS;
    }

    @GetMapping("/login/session-exists")
    public UserResponseDto fetchSession(@PathVariable String userId)
    {
        return userService.fetchUser(userId);
    }

    @GetMapping("/login/password-needed")
    public UserResponseDto verifyUser(@RequestBody String password, @CookieValue("auth") String authCookie, HttpServletResponse response)
    {
        AuthCookieDto auth = cookieService.readCookie(authCookie);
        try {
            UserVerificationDto dto= userService.verifyUser(password, auth.getUserId());
            cookieService.createAuthCookie(response , dto.getSessionId(), auth.getUserId());
            return dto.getUserResponseDto();
        }
        catch(Exception  e){
            log.error("error", e);
            throw new RuntimeException("ERROR");
    }
    }

    @PostMapping("/login/create-new")
    public UserResponseDto createIdentity(@RequestBody UserRequestDto userRequestDto)
    {
        return userService.createIdentity(userRequestDto);
    }
}
