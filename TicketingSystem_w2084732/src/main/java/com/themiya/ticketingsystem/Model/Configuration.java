package com.themiya.ticketingsystem.Model;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component

public class Configuration {
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;
    private int numOfVendors;
    private int numOfCustomers;
    private int quantity;

    public Configuration() {

    }

    public Configuration(int totalTickets, int ticketReleaseRate, int customerRetrievalRate,int maxTicketCapacity, int numOfVendors, int numOfCustomers, int quantity) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
        this.numOfVendors = numOfVendors;
        this.numOfCustomers = numOfCustomers;
        this.quantity = quantity;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public int getNumOfVendors() {
        return numOfVendors;
    }

    public int getNumOfCustomers() {
        return numOfCustomers;
    }

    public int getQuantity() {
        return quantity;
    }


}
