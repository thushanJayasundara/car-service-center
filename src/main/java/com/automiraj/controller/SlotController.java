package com.automiraj.controller;

import com.automiraj.constant.Availability;
import com.automiraj.dto.SlotDTO;
import com.automiraj.service.SlotService;
import com.automiraj.util.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("v1/slot-management-api")
public class SlotController {

    private SlotService slotService;

    @Autowired
    public SlotController(SlotService slotService) { this.slotService = slotService;}

    @PostMapping("/")
    public CommonResponse save(@RequestBody SlotDTO slotDTO) {return slotService.save(slotDTO);}

    @PutMapping("/")
    public CommonResponse update(@RequestBody SlotDTO slotDTO) {return slotService.update(slotDTO);}

    @DeleteMapping("/{id}")
    public CommonResponse delete(@PathVariable String id){return slotService.delete(id);}

    @GetMapping("/")
    public CommonResponse getAll(){return slotService.getAll();}

    @GetMapping("/get-by-availability/{availability}")
    public CommonResponse getSlotByAvailability(@PathVariable String availability){return slotService.getSlotByAvailability(Availability.valueOf(availability));}

    @GetMapping("/{id}")
    public CommonResponse findById(@PathVariable String id)
    {return slotService.findById(id);}
}