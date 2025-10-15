package com.hotspot;

import com.hotspot.exceptions.ErrorCode;
import com.hotspot.exceptions.HotspotException;
import com.hotspot.model.User;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

public class WebSocketHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String,Object> attributes) {

        if (attributes.get("user") == null) {
            throw new HotspotException(ErrorCode.USER_NOT_FOUND, "An error " +
                    "occurred while identifying the messaging user.");
        }

        User connectedUser = (User) attributes.get("user");

        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setId(connectedUser.getId());
        userPrincipal.setUserName(connectedUser.getUsername());

        return userPrincipal;
    }
}
