package com.hotspot.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @SendTo("/chat/messages") // where client listens - /chat/
    @MessageMapping("/message") // where server listens - /server/message
    public String connection(String message) {
        return message;
    }

}
