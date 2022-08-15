package com.automiraj.controller;

import com.automiraj.dto.CustomerMaintenanceDTO;
import com.automiraj.service.CustomerMaintenanceService;
import com.automiraj.util.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/customer-maintenance-management-api")
public class CustomerMaintenanceController {

    private final CustomerMaintenanceService customerMaintenanceService;

    @Autowired
    public CustomerMaintenanceController(CustomerMaintenanceService customerMaintenanceService) {
        this.customerMaintenanceService = customerMaintenanceService;
    }

    @GetMapping("/{id}")
    public CommonResponse findById(@PathVariable String id){
        return customerMaintenanceService.findById(id);
    }
}
