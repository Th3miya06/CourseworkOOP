package com.themiya.ticketingsystem.Model;



import com.themiya.ticketingsystem.WebSocket.WebSocketHandler;
import org.springframework.stereotype.Component;


import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;


@Component
public class TicketPool {
    private Queue<Ticket> tickets = new LinkedList<Ticket>();
    private int maxCapacity;
    private int ticketCounter;
    private static final Logger LOGGER = Logger.getLogger(TicketPool.class.getName());



    public TicketPool(){

    }

    public TicketPool(int maxCapacity,int ticketCounter) {
        this.maxCapacity = maxCapacity;
        this.ticketCounter = ticketCounter;

    }

    private void sendWebSocketMessage(String message) {
        try {
            WebSocketHandler.broadcastMessage(message);
        } catch (Exception e) {
            LOGGER.severe("Unexpected error in WebSocket messaging: " + e.getMessage());
        }
    }


    //Method used by vendor to add tickets to the pool
    public synchronized void addTicket(Ticket ticket){ //synchronized method for thread safety and synchronization
        while(tickets.size()>=maxCapacity || ticketCounter==0){
            try{
                wait();
            }catch(InterruptedException e){
                Thread.currentThread().interrupt();
                return;


            }
        }
        this.tickets.add(ticket);//add tickets to the pool
        this.ticketCounter--; //decrease the ticket counter relative to the total tickets
        notifyAll(); //notify all waiting threads

        String message = Thread.currentThread().getName()+"has added a ticket to the pool. Curren size is "+tickets.size();
        sendWebSocketMessage(message);



        System.out.println(Thread.currentThread().getName()+" has added a ticket to the pool. Current size is: "+tickets.size());

    }

    // Method used by customer to retrieve tickets from the ticket pool
    public synchronized Ticket removeTicket(){
        while(tickets.isEmpty()){
            try{
                wait();

            }catch(InterruptedException e){
                Thread.currentThread().interrupt();
                return null;
            }

        }
        Ticket ticket = tickets.poll(); //remove the first ticket from queue
        notifyAll(); //notify all waiting threads

        String message = Thread.currentThread().getName()+"has removed a ticket from the pool. Current size is: "+tickets.size();
        sendWebSocketMessage(message);



        System.out.println(Thread.currentThread().getName()+" bought a ticket. Current size is: "+tickets.size());
        return ticket;
    }

    public void printTicketId(){
        for(Ticket ticket : tickets){
            System.out.println(ticket);
        }
    }
}
