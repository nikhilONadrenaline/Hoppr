package com.HopprBackend.backend.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


//WEB SOCKET KA SESSION KE LIYE HAI
@Slf4j
public class SessionService {

    private final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    private final Map<String, String> sessionUsers = new ConcurrentHashMap<>();

    public void addSession(String userId,WebSocketSession session) {
        userSessions.put(userId, session);
        sessionUsers.put(session.getId(), userId);
    }

    public void removeSession(WebSocketSession session) {
        String userId = sessionUsers.remove(session.getId());

        if (userId != null) {
            userSessions.remove(userId);
            sessionUsers.remove(session.getId());
        }
    }

    public String getUserId(WebSocketSession session) {
        return sessionUsers.get(session.getId());
    }

    public WebSocketSession getSession(String userId) {
        return userSessions.get(userId);
    }

    public List<String> getUsers() {
        List<String> users=new ArrayList<>();
        users=userSessions.keySet().stream().toList();
        return users;
    }
}
