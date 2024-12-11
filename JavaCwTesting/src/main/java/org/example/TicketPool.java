package org.example;
import java.util.LinkedList;
import java.util.Queue;

public class TicketPool {
    private Queue<Ticket> tickets;
    private int maxCapacity;
    private int ticketCounter;



    public TicketPool(int maxCapacity,int ticketCounter) {
        this.maxCapacity = maxCapacity;
        this.tickets = new LinkedList<Ticket>();
        this.ticketCounter = ticketCounter;

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
        System.out.println(Thread.currentThread().getName()+" bought a ticket. Current size is: "+tickets.size());
        return ticket;
    }
}
