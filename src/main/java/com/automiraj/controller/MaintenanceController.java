package com.automiraj.controller;

import com.automiraj.dto.MaintenanceDTO;
import com.automiraj.service.MaintenanceService;
import com.automiraj.util.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/maintenance-management-api")
public class MaintenanceController {

    private MaintenanceService maintenanceService;

    @Autowired
    public MaintenanceController(MaintenanceService maintenanceService) {this.maintenanceService = maintenanceService;}

    @PostMapping("/")
    public CommonResponse save(@RequestBody MaintenanceDTO maintenanceDTO) {
        return maintenanceService.save(maintenanceDTO);}

    @PutMapping("/")
    public CommonResponse update(@RequestBody MaintenanceDTO maintenanceDTO){return maintenanceService.update(maintenanceDTO);}

    @DeleteMapping("/")
    public CommonResponse delete(@PathVariable String id) {return maintenanceService.delete(id);}

    @GetMapping("/")
    public CommonResponse getAll() {return maintenanceService.getAll();}

    @GetMapping("/{id}")
    public CommonResponse findById(@PathVariable String id) {return maintenanceService.findById(id);}
}
