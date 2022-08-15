package com.automiraj.service;

import com.automiraj.constant.CommonStatus;
import com.automiraj.dto.CustomerMaintenanceDTO;
import com.automiraj.entity.CustomerMaintenance;
import com.automiraj.repository.CustomerMaintenanceRepository;
import com.automiraj.util.CommonResponse;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
public class CustomerMaintenanceService {
    private final CustomerMaintenanceRepository customerMaintenanceRepository;

    @Autowired
    private final CustomerService customerService;


    @Autowired
    public CustomerMaintenanceService(CustomerMaintenanceRepository customerMaintenanceRepository, CustomerService customerService, ModelMapper modelMapper) {
        this.customerMaintenanceRepository = customerMaintenanceRepository;
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

    private final Logger LOGGER = LoggerFactory.getLogger(CustomerMaintenance.class);

    private final ModelMapper modelMapper;

    public CommonResponse save(CustomerMaintenance customerMaintenance){
        CommonResponse commonResponse = new CommonResponse();
        try {
            customerMaintenance.setCommonStatus(CommonStatus.ACTIVE);
            customerMaintenance = customerMaintenanceRepository.save(customerMaintenance);
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(customerMaintenance));
        }catch (Exception e){
            LOGGER.error("/**************** Exception in CustomerMaintenanceService -> save()" + e);
        }return commonResponse;
    }

    public CommonResponse findById(final String id){
        CommonResponse commonResponse = new CommonResponse();
        CustomerMaintenance customerMaintenance;
        try {
            customerMaintenance = customerMaintenanceRepository.getById(Long.parseLong(id));
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(castCustomerMaintenanceIntoCustomerMaintenanceDTO(customerMaintenance)));
        }catch (Exception e){
            LOGGER.error("/**************** Exception in CustomerMaintenanceService -> save()" + e);
        }return commonResponse;
    }

    public CustomerMaintenanceDTO castCustomerMaintenanceIntoCustomerMaintenanceDTO(CustomerMaintenance customerMaintenance){
        return modelMapper.map(customerMaintenance, CustomerMaintenanceDTO.class);
    }
}
