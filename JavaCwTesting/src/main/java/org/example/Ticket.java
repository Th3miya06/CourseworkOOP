package org.example;

import java.math.BigDecimal;

public class Ticket {
    private  int ticketID;
    private  String name;
    private  BigDecimal ticketPrice;



    public Ticket(int ticketID, String name, BigDecimal  ticketPrice) {
        this.ticketID = ticketID;
        this.name = name;
        this.ticketPrice = ticketPrice;
    }


    public int getTicketID() {
        return ticketID;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    @Override
    public String toString() {
        return "Ticket [ticketID=" + ticketID + ", name=" + name + ", ticketPrice="+ticketPrice+"]";
    }
}
