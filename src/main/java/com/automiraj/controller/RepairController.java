package com.automiraj.controller;

import com.automiraj.constant.CommonStatus;
import com.automiraj.dto.RepairDTO;
import com.automiraj.service.RepairService;
import com.automiraj.util.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/repair-management-api")
public class RepairController {

    private final RepairService repairService;

    @Autowired
    public RepairController(RepairService repairService) {
        this.repairService = repairService;
    }

    @PostMapping("/")
    public CommonResponse save(@RequestBody RepairDTO repairDTO){return repairService.save(repairDTO);}

    @PutMapping("/")
    public CommonResponse update(@RequestBody RepairDTO repairDTO){return  repairService.update(repairDTO);}

    @DeleteMapping("/{id}")
    public CommonResponse delete(@PathVariable String id){return repairService.delete(id);}

    @GetMapping("/")
    public CommonResponse getAll(){return repairService.getAll();}

    @GetMapping("/get-by-status/{status}")
    public CommonResponse getByStatus(@PathVariable String status){return repairService.getByStatus(CommonStatus.valueOf(status));}

    @GetMapping("/{id}")
    public CommonResponse findById(@PathVariable String id)
    {return repairService.findById(id);}
}
