package com.automiraj.controller;

import com.automiraj.dto.CustomerDTO;
import com.automiraj.service.CustomerService;
import com.automiraj.util.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("v1/customer-management-api")
public class CustomerController {

    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/")
    public CommonResponse save(@RequestBody CustomerDTO customerDTO) {
        return customerService.save(customerDTO);
    }

    @PutMapping("/")
    public CommonResponse update(@RequestBody CustomerDTO customerDTO) {
        return customerService.update(customerDTO);
    }

    @DeleteMapping("/{id}")
    public CommonResponse delete(@PathVariable String id) {
        return customerService.delete(id);
    }

    @GetMapping("/")
    public CommonResponse getAll() {
        return customerService.getAll();
    }

    @GetMapping("/nic/{nic}")
    public CommonResponse findByNic(@PathVariable String nic) {
        return customerService.findByNic(nic);
    }

    @GetMapping("/{id}")
    public CommonResponse findById(@PathVariable String id) {
        return customerService.findById(id);
    }

    @GetMapping("/advance-search/{nic}")
    public CommonResponse advanceSearchByNic(@PathVariable String nic){
    return customerService.advanceSearchByNic(nic);
    }

}
