package com.themiya.ticketingsystem.WebSocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

@Component
public class WebSocketHandler extends TextWebSocketHandler {


    private static final Logger LOGGER = Logger.getLogger(WebSocketHandler.class.getName());
    //CopyOnWriteArrayList to provide concurrent access when broadcasting messages. This is thread safe since it is from java.util.concurrent package
    //ideal to be used in a multithreaded environment
    private static final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();


    //Handles new web socket connection add sessions to the list
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        LOGGER.info("New WebSocket connection: " + session.getId());
    }

    //handles incoming websocket messages
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // Handle incoming messages if needed
        LOGGER.info("Received message: " + message.getPayload());
    }

    //Handles websocket transport errors logging any errors caused during websocket communication
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        LOGGER.warning("WebSocket transport error: " + exception.getMessage());
    }

    //Removes the sessions from the list when websocket connection closures are done
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        LOGGER.info("WebSocket connection closed: " + session.getId());
    }

    // Static method to broadcast messages to all connected clients
    public static void broadcastMessage(String message) {
        TextMessage textMessage = new TextMessage(message);
        for (WebSocketSession session : sessions) {
            try {
                if (session.isOpen()) {
                    session.sendMessage(textMessage);
                }
            } catch (IOException e) {
                LOGGER.warning("Failed to send message: " + e.getMessage());
            }
        }
    }
}
