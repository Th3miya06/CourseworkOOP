package com.themiya.ticketingsystem.Model;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class Ticket {
    private  String ticketID;
    private  String name;
    private BigDecimal ticketPrice;

    public Ticket(){

    }



    public Ticket(String ticketID, String name, BigDecimal  ticketPrice) {
        this.ticketID = ticketID;
        this.name = name;
        this.ticketPrice = ticketPrice;
    }


    @Override
    public String toString() {
        return "Ticket [ticketID=" + ticketID + ", name=" + name + ", ticketPrice="+ticketPrice+"]";
    }
}
