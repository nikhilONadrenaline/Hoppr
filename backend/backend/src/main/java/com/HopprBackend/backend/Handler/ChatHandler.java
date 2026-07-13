package com.HopprBackend.backend.Handler;

import com.HopprBackend.backend.Dto.AuthCookieDto;
import com.HopprBackend.backend.Service.ChatService;
import com.HopprBackend.backend.Service.CookieService;
import com.HopprBackend.backend.Service.SessionService;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;

@Component
@Slf4j
public class ChatHandler extends TextWebSocketHandler {

    @Autowired
    private ChatService chatService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private CookieService cookieService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        String authCookie = extractAuthCookie(session.getHandshakeHeaders());
        if(authCookie==null) throw new RuntimeException("Empty Cookie");

        AuthCookieDto auth = cookieService.readCookie(authCookie);
        sessionService.addSession( auth.getUserId() ,session);
        log.info("Connected : {}", session.getId());
    }

    private String extractAuthCookie(HttpHeaders headers) {

        List<String> cookieHeaders = headers.get(HttpHeaders.COOKIE);

        if (cookieHeaders == null) {
            return null;
        }

        for (String header : cookieHeaders) {

            String[] cookies = header.split(";");

            for (String cookie : cookies) {

                String[] parts = cookie.trim().split("=", 2);

                if (parts.length == 2 && "auth".equals(parts[0])) {
                    return parts[1];
                }
            }
        }

        return null;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session,TextMessage message) throws Exception {

        chatService.handleIncomingMessage(session,message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session,CloseStatus status) throws Exception {

        sessionService.removeSession(session);
        log.info("Disconnected : {}", session.getId());
    }

    @Override
    public void handleTransportError(WebSocketSession session,Throwable exception) throws Exception {

        log.error("Transport error", exception);
        sessionService.removeSession(session);

        session.close();
    }

}
