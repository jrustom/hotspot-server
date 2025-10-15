package com.hotspot;

import com.hotspot.model.User;
import com.hotspot.services.AccountDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@RequiredArgsConstructor
public class WebSocketInterceptor implements HandshakeInterceptor {

    private final JwtService jwtService;
    private final AccountDetailsService accountDetailsService;

    private String extractToken(String queryString) {
        for (String param : queryString.split("&")) {
            String[] paramKeyValue = param.split("=");
            if (paramKeyValue[0].equals("tok")) {
                return paramKeyValue[1];
            }
        }
        return null;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   @NonNull ServerHttpResponse response,
                                   @NonNull WebSocketHandler wsHandler,
                                   @NonNull Map<String, Object> attributes) {

        // Extract token from request query params
        String token = extractToken(request.getURI().getQuery());
        if (token == null) return false;

        // Validate token
        boolean validated = jwtService.validateToken(token);

        if (!validated)
            return false;

        // Extract user
        User connectingUser =
                (User) accountDetailsService.loadUserByUsername(jwtService.extractUsername(token));

        // Set WebSocket session attribute
        attributes.put("user", connectingUser);
        return true;
    }

    @Override
    public void afterHandshake(@NonNull ServerHttpRequest request,
                               @NonNull ServerHttpResponse response,
                               @NonNull WebSocketHandler wsHandler,
                               Exception ex) {
    }
}
