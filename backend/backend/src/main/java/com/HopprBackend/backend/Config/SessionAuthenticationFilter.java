package com.HopprBackend.backend.Config;

import com.HopprBackend.backend.Dto.AuthCookieDto;
import com.HopprBackend.backend.Dto.UserResponseDto;
import com.HopprBackend.backend.Entity.User;
import com.HopprBackend.backend.Repository.UserRepository;
import com.HopprBackend.backend.Service.CookieService;
import com.HopprBackend.backend.Service.RedisSessionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class SessionAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private RedisSessionService sessionService;

    @Autowired
    private CookieService cookieService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();

        if(cookies != null){

            for(Cookie cookie : cookies){

                if(cookie.getName().equals("auth")){

                    AuthCookieDto auth =cookieService.readCookie(cookie.getValue());

                    if(auth == null) break;

                    UserResponseDto redisUser =sessionService.validateSession(auth.getSessionId(),auth.getUserId());

                    if(redisUser == null) break;

                    UsernamePasswordAuthenticationToken authentication =new UsernamePasswordAuthenticationToken( redisUser,null, Collections.emptyList());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    break;
                }
            }
        }
        filterChain.doFilter(request,response);
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String uri = request.getRequestURI();

        return uri.startsWith("/auth")
                || uri.startsWith("/swagger")
                || uri.startsWith("/v3/api-docs");
    }
}