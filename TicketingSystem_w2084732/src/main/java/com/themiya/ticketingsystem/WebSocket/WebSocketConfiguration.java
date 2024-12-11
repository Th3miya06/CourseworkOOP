package com.themiya.ticketingsystem.WebSocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

//This is for rea time updates where we will be creating the connection between the backend and front end
@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    @Autowired
    private WebSocketHandler webSocketHandler;


    //Overridden method for registering handlers
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/ws")
                .setAllowedOrigins("*");// the "*" allows websocket connection from any origin. Used for further precaution to avoid server errors
    }
}
