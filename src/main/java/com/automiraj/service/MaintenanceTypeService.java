package com.automiraj.service;

import com.automiraj.constant.CommonMsg;
import com.automiraj.constant.CommonStatus;
import com.automiraj.dto.MaintenanceTypeDTO;
import com.automiraj.entity.Maintenance;
import com.automiraj.entity.MaintenanceType;
import com.automiraj.repository.MaintenanceTypeRepository;
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
public class MaintenanceTypeService {

    private final Logger LOGGER = LoggerFactory.getLogger(MaintenanceService.class);

    private final MaintenanceTypeRepository maintenanceTypeRepository;
    private final ModelMapper modelMapper;


    @Autowired
    MaintenanceTypeService(MaintenanceTypeRepository maintenanceTypeRepository, ModelMapper modelMapper) {this.maintenanceTypeRepository = maintenanceTypeRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Maintenance Type save
     * @param maintenanceTypeDTO
     * @return
     */
    public CommonResponse save(final MaintenanceTypeDTO maintenanceTypeDTO) {
        CommonResponse commonResponse = new CommonResponse();
        MaintenanceType maintenanceType;
        try {
            List<String> validationList = this.maintenanceTypeValidation(maintenanceTypeDTO);
            if (!validationList.isEmpty()) {
                commonResponse.setErrorMessages(validationList);
                return commonResponse;
            }
            maintenanceType = castMaintenanceTypeDTOIntoMaintenanceType(maintenanceTypeDTO);
            maintenanceType = maintenanceTypeRepository.save(maintenanceType);
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(maintenanceType));

        } catch (Exception e) {
            LOGGER.error("/**************** Exception in CustomerService -> save()" + e);
        }
        return commonResponse;
    }

    /**
     * maintenance-type update
     * @param maintenanceTypeDTO
     * @return
     */
    public CommonResponse update(final MaintenanceTypeDTO maintenanceTypeDTO){
        CommonResponse commonResponse = new CommonResponse();
        MaintenanceType maintenanceType;
        try {
            List<String> validationList = this.maintenanceTypeValidation(maintenanceTypeDTO);
            if (!validationList.isEmpty()){
                commonResponse.setErrorMessages(validationList);
                return commonResponse;
            }
            MaintenanceType exitMaintenanceType = this.getById(maintenanceTypeDTO.getId());
            maintenanceType = castMaintenanceTypeDTOIntoMaintenanceType(maintenanceTypeDTO);
            exitMaintenanceType.setMaintenanceName(maintenanceType.getMaintenanceName());
            exitMaintenanceType.setCommonStatus(maintenanceType.getCommonStatus());
            maintenanceType =maintenanceTypeRepository.save(exitMaintenanceType);
            commonResponse.setStatus(true);
            commonResponse.setPayload((List<Object>)maintenanceType);
        }catch (Exception e){
            LOGGER.error("/**************** Exception in CustomerService -> save()" + e); 
        }
        return commonResponse;
    }

    /**
     * maintenannce-type delete
     * @param id
     * @return
     */
    public CommonResponse delete(final String id){
        CommonResponse commonResponse =new CommonResponse();
        MaintenanceType maintenanceType;
        try {
            maintenanceType = maintenanceTypeRepository.findById(Long.valueOf(id)).get();
            if (maintenanceType !=null){
              maintenanceType.setCommonStatus(CommonStatus.DELETE);
              maintenanceTypeRepository.save(maintenanceType);
              commonResponse.setStatus(true);
            }
            else
                commonResponse.setErrorMessages(Collections.singletonList(CommonMsg.NOT_FOUND));
        }
        catch (Exception e){
            LOGGER.error("/**************** Exception in MaintenanceTypeService -> save()" + e);
        }
        return commonResponse;
    }

    /**
     * maintenance-type getAll
     * @return
     */
    public CommonResponse getAll(){
        CommonResponse commonResponse = new CommonResponse();
        List<MaintenanceTypeDTO> maintenanceTypeDTOList;
        try {
            Predicate<MaintenanceType> filterOnStatus = maintenanceType -> maintenanceType.getCommonStatus() != CommonStatus.DELETE;

            maintenanceTypeDTOList = maintenanceTypeRepository.findAll()
                    .stream()
                    .filter(filterOnStatus)
                    .map(this::castMaintenanceTypeIntoMaintenanceTypeDTO)
                    .collect(Collectors.toList());
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList((maintenanceTypeDTOList)));
        }catch (Exception e){
            LOGGER.error("/**************** Exception in MaintenanceTypeService-> save()" + e);
        }
        return commonResponse;
    }


    /**
     * maintenance-type get by status
     * @return
     */
    public CommonResponse getByStatus(CommonStatus status){
        CommonResponse commonResponse = new CommonResponse();
        List<MaintenanceTypeDTO> maintenanceTypeDTOList;
        try {
            Predicate<MaintenanceType> filterOnStatus = maintenanceType -> maintenanceType.getCommonStatus() == status;

            maintenanceTypeDTOList = maintenanceTypeRepository.findAll()
                    .stream()
                    .filter(filterOnStatus)
                    .map(this::castMaintenanceTypeIntoMaintenanceTypeDTO)
                    .collect(Collectors.toList());
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList((maintenanceTypeDTOList)));
        }catch (Exception e){
            LOGGER.error("/**************** Exception in MaintenanceTypeService-> save()" + e);
        }
        return commonResponse;
    }

    /**
     * Maintenance type get by id
     * @param maintenanceTypeId
     * @return
     */
    public MaintenanceType getById(final String maintenanceTypeId){
        return maintenanceTypeRepository.findById(Long.parseLong(maintenanceTypeId)).get();
    }

    /**
     * maintenance type find by id
     * @param id
     * @return
     */
    public CommonResponse findById(String id){
        CommonResponse commonResponse = new CommonResponse();
        MaintenanceType maintenanceType;
        try {
            maintenanceType = maintenanceTypeRepository.getById(Long.parseLong(id));
            commonResponse.setPayload(Collections.singletonList(castMaintenanceTypeIntoMaintenanceTypeDTO(maintenanceType)));
            commonResponse.setStatus(true);
        } catch (Exception e){
            LOGGER.error("/**************** Exception in CustomerService -> save()" + e);
        }
        return commonResponse;
    }

    /**
     * cast maintenance-type into maintenance-typeDTO
     * @param maintenanceType
     * @return
     */
    public MaintenanceTypeDTO castMaintenanceTypeIntoMaintenanceTypeDTO(MaintenanceType maintenanceType){
        return modelMapper.map(maintenanceType,MaintenanceTypeDTO.class);}
    /**
     * Cast MaintenanceTypeDTO Into MaintenanceType
     * @param maintenanceTypeDTO
     * @return
     */
    public MaintenanceType castMaintenanceTypeDTOIntoMaintenanceType(MaintenanceTypeDTO maintenanceTypeDTO) {
        MaintenanceType maintenanceType = new MaintenanceType();
        maintenanceType.setId(maintenanceType.getId());
        maintenanceType.setMaintenanceName(maintenanceTypeDTO.getMaintenanceName());
        maintenanceType.setCommonStatus(maintenanceTypeDTO.getCommonStatus());
        return maintenanceType;
    }

    /**
     * MaintenanceType Validation
     * @param maintenanceTypeDTO
     * @return
     */
    public List<String> maintenanceTypeValidation(MaintenanceTypeDTO maintenanceTypeDTO) {
        List<String> validationList = new ArrayList<>();
        if (CommonValidation.stringNullValidation(maintenanceTypeDTO.getMaintenanceName()))
            validationList.add(CommonMsg.EMPTY_MAINTENANCE_NAME);
        return validationList;
    }
}