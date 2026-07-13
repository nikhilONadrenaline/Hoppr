package com.HopprBackend.backend.Service;

import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


//WEB SOCKET KA SESSION KE LIYE HAI
public class SessionService {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void addSession(String userId,WebSocketSession session) {
        sessions.put(userId, session);
    }

    public void removeSession(WebSocketSession session) {

    }

    public String getUserId(WebSocketSession session) {
    }

    public WebSocketSession getSession(String memberId) {
    }
}
