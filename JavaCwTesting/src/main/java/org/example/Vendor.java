package org.example;

import java.math.BigDecimal;

public class Vendor implements Runnable {
    private TicketPool ticketPool; //shared by both vendors and customers;
    private int totTickets;
    private int releaseInterval;

    public Vendor(TicketPool ticketPool,int totTickets,int releaseInterval) {
        this.ticketPool = ticketPool;
        this.totTickets = totTickets;
        this.releaseInterval = releaseInterval;

    }

    @Override
    public void run() {
        for (int i = 1; i <=totTickets; i++) {
            Ticket ticket = new Ticket(i,"Show",new BigDecimal(1500));
            ticketPool.addTicket(ticket); //addTicket() method in ticketPool used by vendor to add tickets

            try {
                Thread.sleep(releaseInterval * 1000); //interval between the release of tickets
            }catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break; //break out of this loop by interrupting the threads when stop functionality is used
            }

        }
    }
}






