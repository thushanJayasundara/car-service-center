package com.automiraj.service;


import com.automiraj.constant.CommonStatus;
import com.automiraj.dto.CustomerBookingDTO;
import com.automiraj.entity.Customer;
import com.automiraj.entity.CustomerBooking;
import com.automiraj.repository.CustomerBookingRepository;
import com.automiraj.util.CommonResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomerBookingService {

    private final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    private final ModelMapper modelMapper;
    private final CustomerBookingRepository customerBookingRepository;

    @Autowired
    public CustomerBookingService(CustomerBookingRepository customerBookingRepository, ModelMapper modelMapper) {
        this.customerBookingRepository = customerBookingRepository;
        this.modelMapper = modelMapper;

    }

    public CustomerBooking save(CustomerBooking customerBooking) {
  CustomerBooking customerBookingRtn = null;
        try {
            customerBooking.setCommonStatus(CommonStatus.ACTIVE);
            customerBookingRtn = customerBookingRepository.save(customerBooking);
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in CustomerBookingService -> save()" + e);
        }
        return customerBookingRtn;
    }

    public void updateBooking(Long bookingId){
        try {
            CustomerBooking customerBooking = customerBookingRepository.findById(bookingId).get();
            customerBooking.setCommonStatus(CommonStatus.INACTIVE);
            customerBookingRepository.save(customerBooking);
        }catch (Exception e){
            LOGGER.error("/**************** Exception in CustomerBookingService -> updateBooking()" + e);
        }
    }

    public CommonResponse findById(final String id) {
        CommonResponse commonResponse = new CommonResponse();
        CustomerBooking customerBooking;
        try {
            customerBooking = getById(id);
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(castCustomerBookingIntoCustomerBookingDTO(customerBooking)));
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in CustomerBookingService -> save()" + e);
        }
        return commonResponse;

    }

    public CustomerBooking getById(final String id){
        return customerBookingRepository.findById(Long.parseLong(id)).get();
    }

    public CustomerBooking getByCustomer(final Customer customer){
        return customerBookingRepository.findCustomerBookingByCustomer(customer);
    }

    public CustomerBookingDTO castCustomerBookingIntoCustomerBookingDTO(CustomerBooking customerBooking) {
        return modelMapper.map(customerBooking, CustomerBookingDTO.class);
    }

    /**
     *
     * @param customer
     * @param status
     * @return
     */
    public Boolean existsByCustomerAndCommonStatus(Customer customer, CommonStatus status){
        return customerBookingRepository.existsByCustomerAndCommonStatus(customer,status);
    }
}

