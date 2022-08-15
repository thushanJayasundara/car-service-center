package com.automiraj.controller;

import com.automiraj.constant.CommonStatus;
import com.automiraj.dto.TimePeriodDTO;
import com.automiraj.service.TimePeriodService;
import com.automiraj.util.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("v1/time-period-management-api")
public class TimingController {

    private final TimePeriodService timePeriodService;

    @Autowired
    public TimingController(TimePeriodService timePeriodService) {
        this.timePeriodService = timePeriodService;
    }

    @PostMapping("/")
    public CommonResponse save(@RequestBody TimePeriodDTO timePeriodDTO){
        return timePeriodService.save(timePeriodDTO);
    }

    @PutMapping("/")
    public CommonResponse update(@RequestBody TimePeriodDTO timePeriodDTO){
        return timePeriodService.update(timePeriodDTO);
    }

    @DeleteMapping("/{id}")
    public CommonResponse delete(@PathVariable String id){
        return timePeriodService.delete(id);
    }

    @GetMapping("/")
    public CommonResponse getAll(){
        return timePeriodService.getAll();
    }

    @GetMapping("/get-by-status/{status}")
    public CommonResponse getByStatus(@PathVariable String status){
        return timePeriodService.getByStatus(CommonStatus.valueOf(status));
    }

    @GetMapping("/{id}")
    public CommonResponse findById(@PathVariable String id){
        return timePeriodService.findById(id);
    }
}
