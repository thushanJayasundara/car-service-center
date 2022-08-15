package com.automiraj.service;

import com.automiraj.constant.Availability;
import com.automiraj.constant.CommonMsg;
import com.automiraj.constant.CommonStatus;
import com.automiraj.dto.MaintenanceDTO;
import com.automiraj.entity.Customer;
import com.automiraj.entity.CustomerBooking;
import com.automiraj.entity.CustomerMaintenance;
import com.automiraj.entity.Maintenance;
import com.automiraj.repository.MaintenanceRepository;
import com.automiraj.util.CommonResponse;
import com.automiraj.util.CommonValidation;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class MaintenanceService {

    private final Logger LOGGER = LoggerFactory.getLogger(MaintenanceService.class);

    private final MaintenanceRepository maintenanceRepository;

    private final RepairService repairService;
    private final SlotService slotService;
    private final MaintenanceTypeService maintenanceTypeService;
    private final CustomerMaintenanceService customerMaintenanceService;
    private final CustomerBookingService customerBookingService;
    private final CustomerService customerService;
    private final ModelMapper modelMapper;
    @Autowired MaintenanceService(MaintenanceRepository maintenanceRepository, @Lazy RepairService repairService, SlotService slotService, MaintenanceTypeService maintenanceTypeService, CustomerMaintenanceService customerMaintenanceService, CustomerBookingService customerBookingService, CustomerService customerService, ModelMapper modelMapper){this.maintenanceRepository = maintenanceRepository;

        this.repairService = repairService;
        this.slotService = slotService;
        this.maintenanceTypeService = maintenanceTypeService;
        this.customerMaintenanceService = customerMaintenanceService;
        this.customerBookingService = customerBookingService;
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

    public Maintenance findByMaintenanceId(String id){
        return maintenanceRepository.getById(Long.parseLong(id));
    }

    /**
     * Maintenance save
     * @param maintenanceDTO
     * @return
     */
    public CommonResponse save(final MaintenanceDTO maintenanceDTO) {
        CommonResponse commonResponse = new CommonResponse();
        Maintenance maintenance;
        Customer customer;
        CustomerMaintenance customerMaintenance = new CustomerMaintenance();
        CustomerBooking customerBooking;
        try {
            List<String> validationList = this.maintenanceValidation(maintenanceDTO);
            if (!validationList.isEmpty()) {
                commonResponse.setErrorMessages(validationList);
                return commonResponse;
            }
            maintenance = castMaintenanceDTOIntoMaintenance(maintenanceDTO);
            maintenance = maintenanceRepository.save(maintenance);

            customer = customerService.findCustomerById(maintenanceDTO.getCustomerDTO().getId());
            customerMaintenance.setMaintenance(maintenance);
            customerMaintenance.setCustomer(customer);
            customerMaintenanceService.save(customerMaintenance);

            customerBooking = customerBookingService.getByCustomer(customer);
            if (customerBooking != null) {
                if (customerBooking.getCommonStatus().equals(CommonStatus.ACTIVE)) {
                    String slotId = String.valueOf(customerBooking.getBooking().getSlot().getId());
                    slotService.updateSlotAvailability(slotId, Availability.AVAILABLE);
                    customerBookingService.updateBooking(customerBooking.getId());
                }
            }

            commonResponse.setStatus(true);
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in MaintenanceService -> save()" + e);
        }
        return commonResponse;
    }

    /**
     * Maintenance Update
     * @param
     * @return
     */

    public CommonResponse update (final MaintenanceDTO maintenanceDTO) {
        CommonResponse commonResponse = new CommonResponse();
        Maintenance maintenance;
        try {
            List<String> validationList = this.maintenanceValidation(maintenanceDTO);
            if (!validationList.isEmpty()){
                commonResponse.setErrorMessages(validationList);
                return commonResponse;
            }
            Maintenance exitMaintenance = this.getById(maintenanceDTO.getId());
            maintenance = castMaintenanceDTOIntoMaintenance(maintenanceDTO);
            exitMaintenance.setId(maintenance.getId());
            exitMaintenance.setMaintenanceType(maintenance.getMaintenanceType());
            exitMaintenance.setVehicleNo(maintenance.getVehicleNo());
            exitMaintenance.setOtherDescription(maintenance.getOtherDescription());
            exitMaintenance.setCommonStatus(maintenance.getCommonStatus());
            commonResponse.setStatus(true);
            commonResponse.setPayload((List<Object>)maintenance);
        }catch (Exception e){
            LOGGER.error("/**************** Exception in MaintenanceService -> update()" + e);
        }
        return commonResponse;
    }
    /**
     * Maintenance Delete
     */
    public CommonResponse delete(final String id){
        CommonResponse commonResponse = new CommonResponse();
        Maintenance maintenance;
        try {
            if (maintenanceRepository.existsById(Long.valueOf(id))){
                maintenance = maintenanceRepository.findById(Long.parseLong(String.valueOf(id))).get();
                maintenance.setCommonStatus(CommonStatus.DELETE);
                maintenanceRepository.save(maintenance);
            } else
                commonResponse.setErrorMessages(Collections.singletonList(CommonMsg.NOT_FOUND));
        }catch (Exception e){
            LOGGER.error("/**************** Exception in MaintenanceService -> delete()" + e);
        }return commonResponse;
    }

    public CommonResponse getAll() {
        CommonResponse commonResponse = new CommonResponse();
        List<MaintenanceDTO> maintenanceDTOList;
        try{
            Predicate<Maintenance> filterOnStatus = maintenance -> maintenance.getCommonStatus() != CommonStatus.DELETE;
            maintenanceDTOList = maintenanceRepository.findAll()
                    .stream()
                    .filter(filterOnStatus)
                    .map(this::castMaintenanceIntoMaintenanceDTO)
                    .collect(Collectors.toList());
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(maintenanceDTOList));
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in MaintenanceService -> getAll()" + e);
        }
        return commonResponse;
    }


        /**
         * Maintenance Find by id
         * @param id
         * @return
         */
    public CommonResponse findById(final String id){
        CommonResponse commonResponse = new CommonResponse();
        Maintenance maintenance;
        try {
            maintenance = getById(id);
            commonResponse.setPayload(Collections.singletonList(castMaintenanceIntoMaintenanceDTO(maintenance)));
            commonResponse.setStatus(true);
        }catch (Exception e){
            LOGGER.error("/**************** Exception in MaintenanceService -> findById()" + e);
        }return commonResponse;
    }

    public Maintenance getById(String maintenanceId){
        return maintenanceRepository.findById(Long.parseLong(maintenanceId)).get();
    }

    /**
     * cast MaintenanceDTO Into Maintenance
     * @param maintenanceDTO
     * @return
     */
    public Maintenance castMaintenanceDTOIntoMaintenance (MaintenanceDTO maintenanceDTO) {
        Maintenance maintenance = new Maintenance();
        maintenance.setMaintenanceType(maintenanceTypeService.getById(maintenanceDTO.getMaintenanceType().getId()));
        maintenance.setVehicleNo(maintenanceDTO.getVehicleNo());
        maintenance.setOtherDescription(maintenanceDTO.getOtherDescription());
        maintenance.setRepair(maintenanceDTO.getRepairDTOS().stream().map(repairDTO -> repairService.getById(repairDTO.getId())).collect(Collectors.toSet()));
        maintenance.setCommonStatus(maintenanceDTO.getCommonStatus());
        return maintenance;
    }


    public MaintenanceDTO castMaintenanceIntoMaintenanceDTO (Maintenance maintenance){
        MaintenanceDTO maintenanceDTO =  modelMapper.map(maintenance, MaintenanceDTO.class);
        maintenanceDTO.setRepairDTOS(maintenance.getRepair().stream().map(repairService::castRepairIntoRepairDTO).collect(Collectors.toSet()));
        maintenanceDTO.setMaintenanceType(maintenanceTypeService.castMaintenanceTypeIntoMaintenanceTypeDTO(maintenance.getMaintenanceType()));
        return maintenanceDTO;
    }

    /**
     * Maintenance Validation
     * @param maintenanceDTO
     * @return
     */
    public List<String> maintenanceValidation(MaintenanceDTO maintenanceDTO) {
        List<String> validationList = new ArrayList<>();
        if (CommonValidation.stringNullValidation(maintenanceDTO.getMaintenanceType().getId()))
            validationList.add(CommonMsg.EMPTY_MAINTENANCE_TYPE);
        if (CommonValidation.stringNullValidation(maintenanceDTO.getVehicleNo()))
            validationList.add(CommonMsg.EMPTY_VEHICLE_NO);
        if (CommonValidation.stringNullValidation(maintenanceDTO.getOtherDescription()))
            validationList.add(CommonMsg.EMPTY_OTHER_DESCRIPTION);
        if (maintenanceDTO.getCommonStatus() == null)
            validationList.add(CommonMsg.EMPTY_STATUS);
        return validationList;
    }
}

