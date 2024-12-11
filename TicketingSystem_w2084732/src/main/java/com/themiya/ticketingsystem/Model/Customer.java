package com.themiya.ticketingsystem.Model;


import com.themiya.ticketingsystem.WebSocket.WebSocketHandler;
import org.springframework.stereotype.Component;


import java.util.logging.Logger;

@Component
public class Customer implements Runnable{
    TicketPool ticketPool; //ticketPool shared by both vendors and customers
    private int retrievalInterval;
    private int quantity;
    private static final Logger LOGGER = Logger.getLogger(TicketPool.class.getName());



    public Customer(){

    }

    public Customer(TicketPool ticketPool, int retrievalInterval, int quantity) {
        this.ticketPool = ticketPool;
        this.retrievalInterval = retrievalInterval;
        this.quantity = quantity;
    }

    //method to send websocket messages to the system log display at the front end
    private void sendWebSocketMessage(String message) {
        try {
            WebSocketHandler.broadcastMessage(message);
        } catch (Exception e) {
            LOGGER.severe("Unexpected error in WebSocket messaging: " + e.getMessage());
        }
    }



    //The customer will call the removeTicket method for each customer thread to show the buying process
    @Override
    public void run() {
        for (int i = 0; i < quantity; i++) {
            String attemptMessage=Thread.currentThread().getName() + " attempting to buy ticket " + (i + 1);


            System.out.println(attemptMessage);
            Ticket ticket = ticketPool.removeTicket();
            if (ticket != null) {

                String buyingMessage = "Ticket bought by customer: " + Thread.currentThread().getName() + ":" + ticket;
                sendWebSocketMessage(buyingMessage);


                System.out.println(buyingMessage);
            }


            try {
                Thread.sleep(retrievalInterval * 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                String interruptMessage=Thread.currentThread().getName() + " was interrupted while trying to buy ticket " + (i + 1);
                sendWebSocketMessage(interruptMessage);


                System.out.println(interruptMessage);
                break;
            }
        }

        String finishMessage = Thread.currentThread().getName() + " has finished their shopping";
        sendWebSocketMessage(finishMessage);


        System.out.println(finishMessage);

    }

}
