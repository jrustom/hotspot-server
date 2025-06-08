package com.hotspot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Value("${WS_STOMPJS_URL}")
    private String ws_stomp_url;

    @Value("${WS_BROKER_PREFIX}")
    private String ws_broker_prefix;

    @Value("${WS_SERV_PREFIX}")
    private String ws_serv_prefix;

    @Override
    public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {
        // client connects here
        registry.addEndpoint(ws_stomp_url).setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(@NonNull MessageBrokerRegistry registry) {
        // Define prefix where client listens (client receiving)
        registry.enableSimpleBroker(ws_broker_prefix);
        // Define prefix where server listens (client sending)
        registry.setApplicationDestinationPrefixes(ws_serv_prefix);
    }
}
