package com.automiraj.controller;

import com.automiraj.dto.BookingDTO;
import com.automiraj.service.BookingService;
import com.automiraj.util.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("v1/booking-management-api")
public class BookingController {

    private BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/")
    public CommonResponse save(@RequestBody BookingDTO bookingDTO) {
        return bookingService.save(bookingDTO);
    }

    @PutMapping("/")
    public CommonResponse update(@RequestBody BookingDTO bookingDTO) {
        return bookingService.update(bookingDTO);
    }

    @DeleteMapping("/")
    public CommonResponse delete(@PathVariable String id) {
        return bookingService.delete(id);
    }

    @GetMapping("/")
    public CommonResponse getAll() {
        return bookingService.getAll();
    }

    @GetMapping("/{id}")
    public CommonResponse findById(@PathVariable String id){
        return bookingService.findById(id);
    }
}
