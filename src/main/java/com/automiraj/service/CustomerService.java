package com.automiraj.service;

import com.automiraj.constant.CommonMsg;
import com.automiraj.constant.CommonStatus;
import com.automiraj.dto.CustomerDTO;
import com.automiraj.entity.Customer;
import com.automiraj.repository.CustomerRepository;
import com.automiraj.util.CommonResponse;
import com.automiraj.util.CommonValidation;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final SmsService smsService;

    @Autowired CustomerService(CustomerRepository customerRepository, ModelMapper modelMapper, SmsService smsService) {this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
        this.smsService = smsService;
    }

    public Customer  findCustomerById(String id){
        return customerRepository.findById(Long.parseLong(id)).get();
    }
    /**
     * Customer save
     */

    public CommonResponse save(final CustomerDTO customerDTO) {
        CommonResponse commonResponse = new CommonResponse();
        Customer customer;
        try {
            List<String> validationList = this.customerValidation(customerDTO);
            if (!validationList.isEmpty()){
                commonResponse.setErrorMessages(validationList);
                return commonResponse;
            }
                customer = castCustomerDTOIntoCustomer(customerDTO);
                customerRepository.save(customer);
                smsService.sendMessage("94-768620405","test");
                commonResponse.setStatus(true);
        } catch (Exception e) {
           LOGGER.error("/**************** Exception in CustomerService -> save()" + e);
        }
        return commonResponse;
    }

    /**
     * customer update
     */

    public CommonResponse update(final CustomerDTO customerDTO) {
        CommonResponse commonResponse = new CommonResponse();
        try {
            List<String> validationList = this.customerValidation(customerDTO);
            if (!validationList.isEmpty()){
                commonResponse.setErrorMessages(validationList);
                return commonResponse;
            }
            Customer exitCustomer = customerRepository.findById(Long.valueOf(customerDTO.getId())).get();
            exitCustomer.setId(Long.valueOf(customerDTO.getId()));
            exitCustomer.setFirstName(customerDTO.getFirstName());
            exitCustomer.setLastName(customerDTO.getLastName());
            exitCustomer.setAddress(customerDTO.getAddress());
            exitCustomer.setEmail(customerDTO.getEmail());
            exitCustomer.setMobile(Integer.valueOf(customerDTO.getMobile()));
            exitCustomer.setVehicleNo(customerDTO.getVehicleNo());
            exitCustomer.setCommonStatus(customerDTO.getCommonStatus());
            customerRepository.save(exitCustomer);
            commonResponse.setStatus(true);
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in CustomerService -> update()" + e);
        }
        return commonResponse;
    }

    /**
     *Customer delete
     */
    public CommonResponse delete(final String id) {
        CommonResponse commonResponse = new CommonResponse();
        Customer customer;
        try {
            customer = customerRepository.findById(Long.valueOf(id)).get();
            if (customer != null) {
                customer.setCommonStatus(CommonStatus.DELETE);
                customerRepository.save(customer);
                commonResponse.setStatus(true);
            } else
                commonResponse.setErrorMessages(Collections.singletonList(CommonMsg.NOT_FOUND));

        } catch (Exception e) {
            LOGGER.error("/**************** Exception in CustomerService -> delete()" + e);
        }
        return commonResponse;
    }

    /**
     * customer get all
     * @return
     */
    public CommonResponse getAll() {
        CommonResponse commonResponse = new CommonResponse();
        List<CustomerDTO> customerDTOList;
        try {
            Predicate<Customer> filterOnStatus = customer -> customer.getCommonStatus() != CommonStatus.DELETE;

            customerDTOList = customerRepository.findAll()
                    .stream()
                    .filter(filterOnStatus)
                    .map(this::castCustomerIntoCustomerDTO)
                    .collect(Collectors.toList());
                    commonResponse.setStatus(true);
                    commonResponse.setPayload(Collections.singletonList(customerDTOList));
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in CustomerService -> getAll()" + e);
        }
        return commonResponse;

    }

    public  CommonResponse findByNic(final String nic) {
        CommonResponse commonResponse = new CommonResponse();
        Customer customer;
        try {
            if (customerRepository.existsByNic(nic)) {
                customer = customerRepository.findByNic(nic);
                commonResponse.setPayload(Collections.singletonList(castCustomerIntoCustomerDTO(customer)));
                commonResponse.setStatus(true);
            }
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in CustomerService -> findByNic()" + e);
        }
        return commonResponse;
    }

    public  CommonResponse findById(final String id) {
        CommonResponse commonResponse = new CommonResponse();
        Customer customer;
        try {
                customer = customerRepository.findById(Long.valueOf(id)).get();
                commonResponse.setPayload(Collections.singletonList(castCustomerIntoCustomerDTO(customer)));
                commonResponse.setStatus(true);

        } catch (Exception e) {
            LOGGER.error("/**************** Exception in CustomerService -> findById()" + e);
        }
        return commonResponse;
    }


    public  CommonResponse advanceSearchByNic(final String nic) {
        CommonResponse commonResponse = new CommonResponse();
        List<CustomerDTO> customerDTOList;
        try {
            Predicate<Customer> filterOnSearchKeyWithPersonNIC = nic.equalsIgnoreCase("-1") ?
                    customer -> !customer.getNic().isEmpty() :
                    customer -> customer.getNic().toLowerCase().contains(nic.toLowerCase());

            customerDTOList = customerRepository.findAll()
                    .stream()
                    .filter(filterOnSearchKeyWithPersonNIC)
                    .map(this::castCustomerIntoCustomerDTO)
                    .collect(Collectors.toList());

            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(customerDTOList));

        }catch (Exception e){
            LOGGER.error("/**************** Exception in CustomerService -> advanceSearchByNic()" + e);
        }
        return  commonResponse;
    }
    /**
     * cast customerDTO into customer
     */

    public Customer castCustomerDTOIntoCustomer (CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setAddress(customerDTO.getAddress());
        customer.setEmail(customerDTO.getEmail());
        customer.setMobile(Integer.parseInt(customerDTO.getMobile()));
        customer.setVehicleNo(customerDTO.getVehicleNo());
        customer.setNic(customerDTO.getNic());
        customer.setCommonStatus(customerDTO.getCommonStatus());
        return customer;
    }

    /**
     * get by nic
     * @param nic
     * @return
     */
    public Customer getByNic(String nic){
        return customerRepository.findByNic(nic);
    }


    /**
     *cast customer  into  customerDto
     */
    public CustomerDTO castCustomerIntoCustomerDTO(Customer customer) {
        return modelMapper.map(customer, CustomerDTO.class);
    }

    /**
     *validation list
     */
    public List<String> customerValidation(CustomerDTO customerDTO) {
        List<String> validationList = new ArrayList<>();
        if (CommonValidation.stringNullValidation(customerDTO.getFirstName()))
            validationList.add(CommonMsg.EMPTY_FIRSTNAME);
        if (CommonValidation.stringNullValidation(customerDTO.getLastName()))
            validationList.add(CommonMsg.EMPTY_LASTNAME);
        if (CommonValidation.stringNullValidation(customerDTO.getMobile()))
            validationList.add(CommonMsg.EMPTY_MOBILE);
        if (CommonValidation.stringNullValidation(customerDTO.getEmail()))
            validationList.add(CommonMsg.EMPTY_EMAIL);
        if (CommonValidation.stringNullValidation(customerDTO.getNic()))
            validationList.add(CommonMsg.EMPTY_NIC);
        if (CommonValidation.isValidEmail(customerDTO.getEmail()))
            validationList.add(CommonMsg.INVALID_EMAIL);
        if (CommonValidation.isValidMobile(customerDTO.getMobile()))
            validationList.add(CommonMsg.INVALID_MOBILE);
        if (CommonValidation.isvalidNic(customerDTO.getNic()))
            validationList.add(CommonMsg.INVALID_NIC);
        if (!customerDTO.getEditable()){
            if(customerRepository.existsByNic(customerDTO.getNic()))
            validationList.add(CommonMsg.ALREADY_EXISTS_NIC);}
        return validationList;
    }
}
