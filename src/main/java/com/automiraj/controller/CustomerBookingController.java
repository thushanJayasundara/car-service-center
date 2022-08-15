package com.automiraj.controller;

import com.automiraj.dto.CustomerBookingDTO;
import com.automiraj.service.CustomerBookingService;
import com.automiraj.util.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/customer-booking-management-api")
public class CustomerBookingController {
    private  final CustomerBookingService customerBookingService;

    @Autowired
    public CustomerBookingController(CustomerBookingService customerBookingService) {
        this.customerBookingService = customerBookingService;
    }

    @GetMapping("/{id}")
    public CommonResponse findById(@PathVariable String id)
    {return customerBookingService.findById(id);}

}
