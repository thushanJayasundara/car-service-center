package com.automiraj.controller;

import com.automiraj.constant.CommonStatus;
import com.automiraj.dto.MaintenanceTypeDTO;
import com.automiraj.service.MaintenanceTypeService;
import com.automiraj.util.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/maintenance-type-management-api")
public class MaintenanceTypeController {

    private final MaintenanceTypeService maintenanceTypeService;

    @Autowired
    public MaintenanceTypeController(MaintenanceTypeService maintenanceTypeService) {
        this.maintenanceTypeService = maintenanceTypeService;
    }

    @PostMapping("/")
    public CommonResponse save (@RequestBody final MaintenanceTypeDTO maintenanceTypeDTO){
        return maintenanceTypeService.save(maintenanceTypeDTO);}

    @PutMapping("/")
    public CommonResponse update(@RequestBody MaintenanceTypeDTO maintenanceTypeDTO){
        return maintenanceTypeService.update(maintenanceTypeDTO);
    }
    @DeleteMapping("/{id}")
    public CommonResponse delete(@PathVariable String id){return maintenanceTypeService.delete(id);}

    @GetMapping("/")
    public CommonResponse getAll(){return maintenanceTypeService.getAll();}

    @GetMapping("/get-by-status/{status}")
    public CommonResponse getByStatus(@PathVariable String status){return maintenanceTypeService.getByStatus(CommonStatus.valueOf(status));}

    @GetMapping("/{id}")
    public CommonResponse findById(@PathVariable String id)
    {return maintenanceTypeService.findById(id);}
}

