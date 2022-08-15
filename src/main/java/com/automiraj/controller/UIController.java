package com.automiraj.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UIController {

    @GetMapping({"/", "/login"})
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashBord() {
        return "dashboard";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/customer")
    public String customer() {
        return "customer";
    }

    @GetMapping("/booking")
    public String booking() {
        return "booking";
    }

    @GetMapping("/customer-booking")
    public String customerBooking() {
        return "CustomerBooking";
    }

    @GetMapping("/maintenance")
    public String maintenance() {
        return "maintenance";
    }

    @GetMapping("/user")
    public String user() {
        return "user";
    }

    @GetMapping("/maintenance-type")
    public String maintenanceType() {return "maintenance-type";}

    @GetMapping("/slot-type")
    public String slotType() {return "slot-type";}

    @GetMapping("/repair-type")
    public String repairType() {return "repair-type";}

    @GetMapping("/time-period")
    public String timePeriod() {return "time-period";}

}
