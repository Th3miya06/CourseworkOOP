package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

public class Configuration {

    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;
    private int numOfVendors;
    private int numOfCustomers;
    private int quantity;

    //Transient keyword used to prevent json serialization error
    private transient TicketPool ticketPool;
    private transient Vendor[] vendors;
    private transient Customer[] customers;
    private transient Thread[] vendorThreads;
    private transient Thread[] customerThreads;
    private transient boolean isRunning=false;

    private static final Logger LOGGER = Logger.getLogger(Configuration.class.getName());



    public Configuration() {
        setLogger();

    }

    public Configuration(int totalTickets,int ticketReleaseRate,int customerRetrievalRate,int maxTicketCapacity,int numOfVendors,int numOfCustomers) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
        this.numOfVendors = numOfVendors;
        this.numOfCustomers = numOfCustomers;
    }



    public int getValidateInput(Scanner scan, String prompt) {
        int value;
        while(true) {
            System.out.print(prompt);
            try {
                value = Integer.parseInt(scan.nextLine());
                if(value > 0) {
                    return value;
                } else {
                    System.out.println("Please enter a number greater than 0");
                }
            } catch(NumberFormatException e) {
                System.out.println("Please enter integers");
            }
        }
    }

    private boolean validateMaxTicketCapacity(int maxCapacity, int totalTickets) {
        if (maxCapacity > totalTickets) {
            System.out.println("Error: Max ticket capacity (" + maxCapacity +
                    ") cannot exceed total tickets (" + totalTickets + ")");
            LOGGER.warning("Invalid configuration: Max ticket capacity exceeds total tickets");
            return false;
        }
        return true;
    }

    private boolean validateQuantity(int quantity, int totalTickets) {
        if (quantity > totalTickets) {
            System.out.println("Error: Customer purchase quantity (" + quantity +
                    ") cannot exceed total available tickets (" + totalTickets + ")");
            LOGGER.warning("Invalid configuration: Purchase quantity exceeds total tickets available");
            return false;
        }
        return true;
    }

    public void userConfig() {
        Scanner scan = new Scanner(System.in);
        boolean running = true;

        while(running) {
            String menu = """
                    
                    ==================================================================
                    |            Welcome to the Event Ticketing System               |
                    ==================================================================
                    |                                                                |
                    |                                                                |
                    *Press 1 to input Configuration.
                    *Press 2 to Load Configuration.
                    *Press 3 to Start.
                    *Press 4 to Stop.
                    *Press 5 to Exit.
                    
                    
                    
                    PLEASE ENTER VALUE POSITIVE VALUES ONLY!
                    |                                                                |
                    |                                                                |
                    ==================================================================
                    """;

            System.out.println(menu);
            System.out.print("Enter your choice: ");

            int choice = getValidateInput(scan, "");

            switch (choice) {
                case 1:
                    inputConfiguration(scan);
                    askToSave(scan);
                    intialization();
                    break;

                case 2:
                    System.out.print("Enter the filename to load the configuration:");
                    String filename = scan.nextLine();
                    loadFromJsonFile(filename);
                    showConfiguration();
                    intialization();
                    break;

                case 3:
                    startSystem();
                    break;

                case 4:
                    stopSystem();
                    break;

                case 5:
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Please try again. (ENTER 1 or 2)");
            }
        }
    }

    public void inputConfiguration(Scanner scan) {
        this.totalTickets = getValidateInput(scan,"Enter the total number of tickets(greater than 0): ");

        while (true) {
            int proposedCapacity = getValidateInput(scan, "Enter the max ticket capacity: ");
            if (validateMaxTicketCapacity(proposedCapacity, this.totalTickets)) {
                this.maxTicketCapacity = proposedCapacity;
                break;
            }
            // If validation fails, prompt will repeat
        }

        this.ticketReleaseRate = getValidateInput(scan, "Enter the ticket release rate (greater than 0): ");
        this.customerRetrievalRate = getValidateInput(scan,"Enter the customer retrieval rate (greater than 0): ");

        System.out.println("---Thread Configuration---");
        this.numOfVendors = getValidateInput(scan,"Enter the number of vendors: ");
        this.numOfCustomers = getValidateInput(scan,"Enter the number of customers: ");
        while (true) {
            int proposedQuantity = getValidateInput(scan, "Enter the quantity a customer will buy: ");
            if (validateQuantity(proposedQuantity, this.totalTickets)) {
                this.quantity = proposedQuantity;
                break;
            }
            // If validation fails, prompt will repeat
        }
    }

    public void saveToJsonFile(String fileName) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try(FileWriter file = new FileWriter(fileName)) {
            file.write(gson.toJson(this));
            System.out.println("Configuration saved to " + fileName);
        } catch(IOException e) {
            System.out.println("Error saving configuration to " + fileName + " " + e.getMessage());
        }
    }

    public void loadFromJsonFile(String fileName) {
        Gson gson = new Gson();
        try(FileReader load = new FileReader(fileName)) {
            Configuration config = gson.fromJson(load, Configuration.class);
            this.totalTickets = config.getTotalTickets();
            this.ticketReleaseRate = config.getTicketReleaseRate();
            this.customerRetrievalRate = config.getCustomerRetrievalRate();
            this.maxTicketCapacity = config.getMaxTicketCapacity();
            this.numOfVendors = config.getNumOfVendors();
            this.numOfCustomers = config.getNumOfCustomers();
            this.quantity = config.getQuantity();
            LOGGER.info("Configuration saved to " + fileName);
        } catch(IOException e) {
            System.out.println("Error loading configuration from " + fileName + " " + e.getMessage());
        }
    }

    public void askToSave(Scanner scan) {
        System.out.println("Do you want to save the configuration to the file? (Y/N)");
        String choice = scan.nextLine().trim();
        if(choice.equalsIgnoreCase("y")) {
            System.out.println("Enter the file name to save(example.json): ");
            String filename = scan.nextLine();
            saveToJsonFile(filename);
        }
    }

    public void intialization(){
        ticketPool = new TicketPool(maxTicketCapacity,totalTickets);
        vendors = new Vendor[numOfVendors];
        customers = new Customer[numOfCustomers];
        vendorThreads = new Thread[numOfVendors];
        customerThreads = new Thread[numOfCustomers];

        //Creating and initializing vendor and customer objects
        for (int i = 0; i <vendors.length ; i++) {
            vendors[i] = new Vendor(ticketPool,totalTickets,ticketReleaseRate);
        }

        for (int i = 0; i <customers.length ; i++) {
            customers[i] = new Customer(ticketPool,customerRetrievalRate,quantity);
        }
    }

    private void setLogger(){
        try{
            FileHandler file = new FileHandler("ticketing-system.log",true);
            file.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(file);
            LOGGER.setLevel(Level.INFO);

        } catch (IOException e) {
            System.out.println("Could not setup logger: " + e.getMessage());
        }
    }



    private void startSystem(){
        if (totalTickets <= 0) {
            LOGGER.warning("Attempted to start empty configuration");
            System.out.println("System is not configured! Please input or load configuration first.");
            return;
        }

        if(!isRunning) {
            isRunning = true;

            for (int i = 0; i <vendorThreads.length ; i++) {
                vendorThreads[i] = new Thread(vendors[i],"Vendor - "+i);
                vendorThreads[i].start();
            }

            for (int i = 0; i <customerThreads.length ; i++) {
                customerThreads[i] = new Thread(customers[i],"Customer - "+i);
                customerThreads[i].start();

            }
            LOGGER.info("System started");
            System.out.println("Starting system...");
        }else{
            LOGGER.warning("System is already running");
            System.out.println("System is already running");
        }


    }

    private void stopSystem() {
        if (isRunning) {
            isRunning = false;


            //stopping of vendor threads
            for (int i = 0; i < vendorThreads.length; i++) {
                vendorThreads[i].interrupt();
                try {
                    vendorThreads[i].join(); //join method used for more orderly stopping of threads
                } catch (InterruptedException e) {
                    LOGGER.warning("Interrupted while stopping vendor thread"+i);
                }

            }

            //stopping of customer threads
            for (int i = 0; i < customerThreads.length; i++) {
                customerThreads[i].interrupt();
                try {
                    customerThreads[i].join();
                } catch (InterruptedException e) {
                    LOGGER.warning("Interrupted while stopping customer thread"+i);
                }
            }
            System.out.println("Stopping system...");
            LOGGER.info("System stopped successfully");
        }else{
            LOGGER.warning("Attempted to stop non-running system");
            System.out.println("System is not running");
        }
    }

    public void showConfiguration() {
        LOGGER.info("Current Configuration - " +
                "Total Tickets: " + totalTickets +
                ", Max Capacity: " + maxTicketCapacity +
                ", Vendors: " + numOfVendors +
                ", Customers: " + numOfCustomers);

        System.out.println("\nLoaded Configuration:");
        System.out.println("==================================================================");
        System.out.println("Total Tickets: " + totalTickets);
        System.out.println("Max Ticket Capacity: " + maxTicketCapacity);
        System.out.println("Ticket Release Rate: " + ticketReleaseRate);
        System.out.println("Customer Retrieval Rate: " + customerRetrievalRate);
        System.out.println("Number of Vendors: " + numOfVendors);
        System.out.println("Number of Customers: " + numOfCustomers);
        System.out.println("Quantity per Customer: " + quantity);
        System.out.println("==================================================================");
    }


    // Getters and Setters
    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
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

    @Override
    public String toString() {
        return "Total Tickets: " + totalTickets + ", Ticket Release Rate: " + ticketReleaseRate;
    }
}