package com.themiya.ticketingsystem.Controller;

import com.themiya.ticketingsystem.Model.Configuration;
import com.themiya.ticketingsystem.Service.TicketingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController//Provides endpoint for configuring and controlling the ticketing system
@CrossOrigin //Enable cross-origin request . Prevent springboot CORS ERROR
public class TicketingSystemController {

    @Autowired
    private TicketingService ticketingService;

    //configure the system with the necessary parameters(POST)
    @PostMapping("/config")
    public String setConfig(@RequestBody Configuration config) {
        try {
            ticketingService.setConfiguration(config);
            return "Configuration set successfully";
        } catch (Exception e) {
            e.getMessage();
            return "Error setting configuration";
        }
    }

    //start the system when request is sent(POST)
    @PostMapping("/start")
    public String startSystem(){
        try{
            ticketingService.startSystem();
            return "System started successfully";
        }catch(Exception e){
            e.getMessage();
            return "Error starting system";
        }
    }

    //Stop the system when the request is sent from the front end(POST)
    @PostMapping("/stop")
    public String stopSystem(){
        try{
            ticketingService.stopSystem();
            return "System stopped successfully";
        }catch(Exception e){
            e.getMessage();
            return "Error stopping system";
        }
    }
}
