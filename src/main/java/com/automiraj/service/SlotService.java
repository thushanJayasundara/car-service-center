package com.automiraj.service;

import com.automiraj.constant.Availability;
import com.automiraj.constant.CommonMsg;
import com.automiraj.constant.CommonStatus;
import com.automiraj.dto.SlotDTO;
import com.automiraj.entity.Slot;
import com.automiraj.repository.SlotRepository;
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
public class SlotService {

    private final Logger LOGGER = LoggerFactory.getLogger(SlotService.class);

    private final SlotRepository slotRepository;
    private final ModelMapper modelMapper;
    private final MaintenanceTypeService maintenanceTypeService;
    private final TimePeriodService timePeriodService;

    @Autowired
    SlotService(SlotRepository slotRepository, ModelMapper modelMapper, MaintenanceTypeService maintenanceTypeService, TimePeriodService timePeriodService) {
        this.slotRepository = slotRepository;
        this.modelMapper = modelMapper;
        this.maintenanceTypeService = maintenanceTypeService;
        this.timePeriodService = timePeriodService;
    }


    public Slot findSlotById(String id) {
        return slotRepository.getById(Long.parseLong(id));
    }

    /**
     * Slot save
     * @param slotDTO
     * @return
     */
    public CommonResponse save(final SlotDTO slotDTO) {
        CommonResponse commonResponse = new CommonResponse();
        Slot slot;
        try {
            List<String> validationList = this.slotValidation(slotDTO);
            if (!validationList.isEmpty()) {
                commonResponse.setErrorMessages(validationList);
                return commonResponse;
            }
            slot = castSlotDTOIntoSlot(slotDTO);
            slot = slotRepository.save(slot);
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(slot));

        } catch (Exception e) {
            LOGGER.error("/**************** Exception in SlotService -> save()" + e);
        }
        return commonResponse;
    }

    /**
     * slot update
     * @param slotDTO
     * @return
     */
    public CommonResponse update(final SlotDTO slotDTO){
        CommonResponse commonResponse = new CommonResponse();
        try {
            List<String> validationList = this.slotValidation(slotDTO);
            if (!validationList.isEmpty()){
                commonResponse.setErrorMessages(validationList);
                return commonResponse;
            }
            Slot exitSlot = slotRepository.findById(Long.valueOf(slotDTO.getId())).get();
            exitSlot.setId(Long.valueOf(slotDTO.getId()));
            exitSlot.setSlotName(slotDTO.getSlotName());
            exitSlot.setTimePeriod(timePeriodService.getById(slotDTO.getTimePeriod().getId()));
            exitSlot.setMaintenanceType(maintenanceTypeService.getById(slotDTO.getMaintenanceType().getId()));

            exitSlot.setCommonStatus(slotDTO.getCommonStatus());
            slotRepository.save(exitSlot);
            commonResponse.setStatus(true);
            //commonResponse.setPayload((List<Object>)slot);
        }catch (Exception e){
            LOGGER.error("/**************** Exception in SlotService -> save()" + e);
        }return commonResponse;
    }

    /**
     * slot delete
     * @param id
     * @return
     */
    public CommonResponse delete(final String id){
      CommonResponse commonResponse = new CommonResponse();
      Slot slot;
      try {
          slot = slotRepository.findById(Long.valueOf(id)).get();
            if(slot !=null){
              slot.setCommonStatus(CommonStatus.DELETE);
              slotRepository.save(slot);
              commonResponse.setStatus(true);
          }else
              commonResponse.setErrorMessages(Collections.singletonList(CommonMsg.NOT_FOUND));
      }catch (Exception e){
          LOGGER.error("/**************** Exception in SlotService -> save()" + e);
      }return commonResponse;
    }

    /**
     * slot getById
     * @param slotId
     * @return
     */
    public Slot getById(String slotId){return slotRepository.findById(Long.parseLong(slotId)).get();}

    /**
     * repair getAll
     * @return
     */
    public CommonResponse getAll(){
        CommonResponse commonResponse = new CommonResponse();
        List<SlotDTO> slotDTOList;
        try {
            Predicate<Slot> filterOnStatus = slot -> slot.getCommonStatus() != CommonStatus.DELETE;

            slotDTOList = slotRepository.findAll()
                    .stream()
                    .filter(filterOnStatus)
                    .map(this::castSlotIntoSlotDTO)
                    .collect(Collectors.toList());
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(slotDTOList));
        }catch (Exception e){
            LOGGER.error("/**************** Exception in SlotService -> getAll()" + e);
        }return commonResponse;
    }

    /**
     * get by availability
     * @param availability
     * @return
     */
    public CommonResponse getSlotByAvailability(Availability availability){
        CommonResponse commonResponse = new CommonResponse();
        List<SlotDTO> slotDTOList;
        try {
            Predicate<Slot> filterOnStatus = slot -> slot.getCommonStatus() == CommonStatus.ACTIVE;
            Predicate<Slot> filterOnAvailability = slot -> slot.getAvailability() == availability;

            slotDTOList = slotRepository.findAll()
                    .stream()
                    .filter(filterOnStatus.and(filterOnAvailability))
                    .map(this::castSlotIntoSlotDTO)
                    .collect(Collectors.toList());
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(slotDTOList));
        }catch (Exception e){
            LOGGER.error("/**************** Exception in SlotService -> getAll()" + e);
        }return commonResponse;
    }

    /**
     * slot find by id
     * @param id
     * @return
     */
    public CommonResponse findById(final String id){
        CommonResponse commonResponse = new CommonResponse();
        Slot slot;
        try {
            slot = slotRepository.getById(Long.parseLong(id));
            commonResponse.setPayload(Collections.singletonList(castSlotIntoSlotDTO(slot)));
            commonResponse.setStatus(true);
        }catch (Exception e){
            LOGGER.error("/**************** Exception in SlotService -> save()" + e);
        }
           return commonResponse;
    }

    /**
     * update slot availability
     * @param slotId
     */
    public void updateSlotAvailability(String slotId, Availability availability){
        Slot slot = getById(slotId);
        slot.setAvailability(availability);
        slotRepository.save(slot);
    }

    /**
     * cast slot into slotDTO
     * @param slot
     * @return
     */
    public SlotDTO castSlotIntoSlotDTO (Slot slot){
        return modelMapper.map(slot, SlotDTO.class);
    }
    /**
     * cast SlotDTO Into Slot
     * @param slotDTO
     * @return
     */
    public Slot castSlotDTOIntoSlot(SlotDTO slotDTO) {
        Slot slot = new Slot();
        slot.setSlotName(slotDTO.getSlotName());
        slot.setAvailability(Availability.AVAILABLE);
        if (!slotDTO.getTimePeriod().getId().equals("-1"))
        slot.setTimePeriod(timePeriodService.getById(slotDTO.getTimePeriod().getId()));
        slot.setMaintenanceType(maintenanceTypeService.getById(slotDTO.getMaintenanceType().getId()));
        slot.setCommonStatus(slotDTO.getCommonStatus());
        return slot;
    }

    /**
     * Slot Validation
     * @param slotDTO
     * @return
     */
    public List<String> slotValidation(SlotDTO slotDTO) {
        List<String> validationList = new ArrayList<>();
        if (CommonValidation.stringNullValidation(slotDTO.getSlotName()))
            validationList.add(CommonMsg.EMPTY_SLOT_NAME);
        return validationList;
    }

}