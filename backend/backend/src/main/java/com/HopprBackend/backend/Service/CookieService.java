package com.HopprBackend.backend.Service;

import com.HopprBackend.backend.Dto.AuthCookieDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
@Service
public class CookieService {

    public void createAuthCookie(HttpServletResponse response, String sessionId, String userId) {

        String payload = sessionId + ":" + userId;

        String encoded = Base64.getUrlEncoder()
                .encodeToString(payload.getBytes(StandardCharsets.UTF_8));

        Cookie cookie = new Cookie("auth", encoded);

        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24);

        response.addCookie(cookie);
    }

    public AuthCookieDto readCookie(String encodedCookie) {

        try {

            String decoded = new String(
                    Base64.getUrlDecoder().decode(encodedCookie),
                    StandardCharsets.UTF_8
            );

            String[] parts = decoded.split(":", 2);

            if (parts.length != 2) {
                return null;
            }

            return new AuthCookieDto(parts[0], parts[1]);

        } catch (Exception e) {
            return null;
        }
    }
}