package org.example;

public class Customer implements Runnable {
    TicketPool ticketPool; //ticketPool shared by both vendors and customers
    private int retrievalInterval;
    private int quantity;

    public Customer(TicketPool ticketPool, int retrievalInterval, int quantity) {
        this.ticketPool = ticketPool;
        this.retrievalInterval = retrievalInterval;
        this.quantity = quantity;
    }

    @Override
    public void run() {
        for (int i = 0; i < quantity; i++) {
            System.out.println(Thread.currentThread().getName() + " attempting to buy ticket " + (i + 1));
            Ticket ticket = ticketPool.removeTicket();
            if (ticket != null) {
                System.out.println("Ticket bought by customer: " + Thread.currentThread().getName() + ":" + ticket);
            }

            try {
                Thread.sleep(retrievalInterval * 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(Thread.currentThread().getName() + " was interrupted while trying to buy ticket " + (i + 1));
                break;
            }
        }
        System.out.println(Thread.currentThread().getName() + " has finished their shopping");
    }
}