package com.themiya.ticketingsystem.Controller;

import com.themiya.ticketingsystem.Model.Configuration;
import com.themiya.ticketingsystem.Service.TicketingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class TicketingSystemController {

    @Autowired
    private TicketingService ticketingService;

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
