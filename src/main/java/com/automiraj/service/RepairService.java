package com.automiraj.service;

import com.automiraj.constant.CommonMsg;
import com.automiraj.constant.CommonStatus;
import com.automiraj.dto.RepairDTO;
import com.automiraj.entity.MaintenanceType;
import com.automiraj.entity.Repair;
import com.automiraj.repository.RepairRepository;
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
public class RepairService {

    private final Logger LOGGER = LoggerFactory.getLogger(RepairService.class);

    private final RepairRepository repairRepository;
    private final MaintenanceService maintenanceService;
    private final ModelMapper modelMapper;
    @Autowired
    RepairService(RepairRepository repairRepository, MaintenanceService maintenanceService, ModelMapper modelMapper){
        this.repairRepository = repairRepository;
        this.maintenanceService = maintenanceService;
        this.modelMapper = modelMapper;
    }

    /**
     * Repair save
     * @param repairDTO
     * @return
     */
    public CommonResponse save(final RepairDTO repairDTO){
        CommonResponse commonResponse = new CommonResponse();
        Repair repair;
        try {
            List<String> validationList = this.repairValidation(repairDTO);
            if (!validationList.isEmpty()){
                commonResponse.setErrorMessages(validationList);
                return commonResponse;
            }
          repair = castRepairDTOInoRepair(repairDTO);
            repair = repairRepository.save(repair);
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(repair));
        }catch (Exception e){
            LOGGER.error("/**************** Exception in RepairService -> save()" + e);
        }return commonResponse;
    }

    /**
     * Repair update
     * @param repairDTO
     * @return
     */
    public CommonResponse update (final RepairDTO repairDTO){
        CommonResponse commonResponse = new CommonResponse();
        Repair repair;
        try {
            List<String> validationList = this.repairValidation(repairDTO);
            if (!validationList.isEmpty()){
                commonResponse.setErrorMessages(validationList);
                return commonResponse;
            }
            Repair exitRepair = this.getById(repairDTO.getId());
            repair = castRepairDTOInoRepair(repairDTO);
            exitRepair.setId(repair.getId());
            exitRepair.setRepairName(repair.getRepairName());
            exitRepair.setCommonStatus(repair.getCommonStatus());
            repair = repairRepository.save(exitRepair);
            commonResponse.setStatus(true);
            commonResponse.setPayload((List<Object>)repair);
        } catch (Exception e){
            LOGGER.error("/**************** Exception in RepairService -> update()" + e);
        } return commonResponse;
    }

    /**
     * Repair delete
     * @param
     * @return
     */
    public CommonResponse delete(final String id){
        CommonResponse commonResponse = new CommonResponse();
        Repair repair;
        try {
            if (repairRepository.existsById(Long.valueOf(id))){
                repair = repairRepository.findById(Long.parseLong(String.valueOf(id))).get();
                repair.setCommonStatus(CommonStatus.DELETE);
                repairRepository.save(repair);
            }else
                commonResponse.setErrorMessages(Collections.singletonList(CommonMsg.NOT_FOUND));
        }catch (Exception e){
            LOGGER.error("/**************** Exception in RepairService -> delete()" + e);
        }return commonResponse;
    }
    /**
    * repair get all
     */
    public CommonResponse getAll(){
        CommonResponse commonResponse = new CommonResponse();
        List<RepairDTO> repairDTOList = new ArrayList<>();
        try {
            Predicate<Repair> filterOnStatus = repair -> repair.getCommonStatus() != CommonStatus.DELETE;

            repairDTOList = repairRepository.findAll()
                    .stream()
                    .filter(filterOnStatus)
                    .map(this::castRepairIntoRepairDTO)
                    .collect(Collectors.toList());
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(repairDTOList));
        }catch (Exception e){
            LOGGER.error("/**************** Exception in RepairService -> getAll()" + e);
        }return commonResponse;
    }

    /**
     * get by status
     * @param status
     * @return
     */
    public CommonResponse getByStatus(CommonStatus status){
        CommonResponse commonResponse = new CommonResponse();
        List<RepairDTO> repairDTOList = new ArrayList<>();
        try {
            Predicate<Repair> filterOnStatus = repair -> repair.getCommonStatus() == status;

            repairDTOList = repairRepository.findAll()
                    .stream()
                    .filter(filterOnStatus)
                    .map(this::castRepairIntoRepairDTO)
                    .collect(Collectors.toList());
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(repairDTOList));
        }catch (Exception e){
            LOGGER.error("/**************** Exception in RepairService -> getAll()" + e);
        }return commonResponse;
    }

    /**
     * repair get by id
     * @param repairId
     * @return
     */
    public Repair getById(final String repairId)
    {return repairRepository.findById(Long.parseLong(repairId)).get();}
    /**
     * Repair get id
     * @param id
     * @return
     */
    public CommonResponse findById (String id){
        CommonResponse commonResponse = new CommonResponse();
        Repair repair;
        try {
            repair = repairRepository.getById(Long.parseLong(id));
            commonResponse.setPayload(Collections.singletonList(castRepairIntoRepairDTO(repair)));
            commonResponse.setStatus(true);
        }catch (Exception e){
            LOGGER.error("/**************** Exception in RepairService -> findById()" + e);
        }
        return commonResponse;
    }
    /**
     * cast Repair Into  RepairDTO
     * @param repair
     * @return
     */
    public RepairDTO castRepairIntoRepairDTO(Repair repair){
        RepairDTO repairDTO = modelMapper.map(repair, RepairDTO.class);
        return repairDTO;}

    /**
     * cast repairDTO into repair
     * @param repairDTO
     * @return
     */
    public Repair castRepairDTOInoRepair (RepairDTO repairDTO){
        Repair repair = modelMapper.map(repairDTO , Repair.class);
        return repair;
    }

    /**
     * Repair Validation
     * @param repairDTO
     * @return
     */
    public List<String> repairValidation (RepairDTO repairDTO){
        List<String> validationList = new ArrayList<>();
        if (CommonValidation.stringNullValidation(repairDTO.getRepairName()))
            validationList.add(CommonMsg.EMPTY_REPAIR_NAME);
        if (repairDTO.getCommonStatus() == null)
            validationList.add(CommonMsg.EMPTY_STATUS);
        return validationList;
    }


}
