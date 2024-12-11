package com.themiya.ticketingsystem.Service;


import com.themiya.ticketingsystem.Model.Configuration;
import com.themiya.ticketingsystem.Model.Customer;
import com.themiya.ticketingsystem.Model.TicketPool;
import com.themiya.ticketingsystem.Model.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


import java.util.logging.Logger;


@Service
public class TicketingService {
    private static final Logger LOGGER = Logger.getLogger(TicketingService.class.getName());
    private Configuration config;

    private transient TicketPool ticketPool;
    private transient Vendor[] vendors;
    private transient Customer[] customers;
    private transient Thread[] vendorThreads;
    private transient Thread[] customerThreads;
    private transient boolean isRunning=false;

    public void setConfiguration(Configuration config) {
        LOGGER.info("Setting new configuration");
        this.config = config;
        initialization();
    }

    //method to initialise the system with necessary parameters. This where the vendors customers are initialised
    public void initialization(){
        ticketPool = new TicketPool(config.getMaxTicketCapacity(), config.getTotalTickets());
        vendors = new Vendor[config.getNumOfVendors()];
        customers = new Customer[config.getNumOfCustomers()];
        vendorThreads = new Thread[config.getNumOfVendors()];
        customerThreads = new Thread[config.getNumOfCustomers()];

        //Creating and initializing vendor and customer objects
        for (int i = 0; i <vendors.length ; i++) {
            vendors[i] = new Vendor(ticketPool, config.getTotalTickets(), config.getTicketReleaseRate());
            vendors[i].setId("V"+i+"-");
        }

        for (int i = 0; i <customers.length ; i++) {
            customers[i] = new Customer(ticketPool, config.getCustomerRetrievalRate(), config.getQuantity());
        }

        LOGGER.info("System Configured");
    }

    //Method used for starting the system
    public void startSystem(){
        //check if the configured object is empty for further precaution
        if (config.getTotalTickets() <= 0){
            LOGGER.warning("Attempted to start empty configuration");
            return;
        }

        if(!isRunning) {
            isRunning = true; //isRunning set to true to indicate the system is running

            //Each vendor thread is created
            for (int i = 0; i <vendorThreads.length ; i++) {
                vendorThreads[i] = new Thread(vendors[i],"Vendor - "+i);

            }

            //Each vendor thread is started
            for (int i = 0; i <vendorThreads.length ; i++) {
                vendorThreads[i].start();
            }

            //Each customer thread is created
            for (int i = 0; i <customerThreads.length ; i++) {
                customerThreads[i] = new Thread(customers[i],"Customer - "+i);


            }

            //Each customer thread is started
            for (int i = 0; i <customerThreads.length ; i++) {
                customerThreads[i].start();
            }

            LOGGER.info("System started");
        }else{
            LOGGER.warning("System is already running");
        }


    }

    //Method used for stopping the system
    public void stopSystem() {
        if (isRunning) {//isRunning is true when the system is running so change to false
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
                    customerThreads[i].join(); //Each thread will wait until one thread finishes its job
                } catch (InterruptedException e) {
                    LOGGER.warning("Interrupted while stopping customer thread"+i);
                }
            }
            System.out.println("Stopping system...");
            LOGGER.info("System stopped successfully");
        }else{
            LOGGER.warning("Attempted to stop non-running system");
        }
    }



    public boolean isRunning() {
        return isRunning;
    }

    public Configuration getConfiguration(){
        return config;
    }
}
