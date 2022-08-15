package com.automiraj.service;

import com.automiraj.constant.CommonMsg;
import com.automiraj.constant.CommonStatus;
import com.automiraj.dto.TimePeriodDTO;
import com.automiraj.entity.TimePeriod;
import com.automiraj.repository.TimePeriodRepository;
import com.automiraj.util.CommonResponse;
import com.automiraj.util.CommonValidation;
import com.automiraj.util.DateTimeUtil;
import com.automiraj.util.SequenceGenerator;
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
public class TimePeriodService {

    private final Logger LOGGER = LoggerFactory.getLogger(RepairService.class);

    private final TimePeriodRepository timePeriodRepository;
    private final ModelMapper modelMapper;
    private final SequenceGenerator sequenceGenerator;

    @Autowired TimePeriodService(TimePeriodRepository timePeriodRepository, ModelMapper modelMapper, SequenceGenerator sequenceGenerator){this.timePeriodRepository = timePeriodRepository;
        this.modelMapper = modelMapper;
        this.sequenceGenerator = sequenceGenerator;
    }

    public TimePeriod findTimePeriodById(String id){
        return timePeriodRepository.getById(Long.parseLong(id));
    }
    /**
     * Time period save
     * @param timePeriodDTO
     * @return
     */
    public CommonResponse save(TimePeriodDTO timePeriodDTO){
        CommonResponse commonResponse = new CommonResponse();
        TimePeriod timePeriod;
        try {
            List<String> validationList = this.timePeriodValidation(timePeriodDTO);
            if (!validationList.isEmpty()){
                commonResponse.setErrorMessages(validationList);
                return commonResponse;
            }
            timePeriod = castTimePeriodDTOIntoTimePeriod(timePeriodDTO);
            timePeriod.setTimeCode(genCode(timePeriodDTO));
            timePeriod = timePeriodRepository.save(timePeriod);
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(timePeriod));
        }catch (Exception e){
            LOGGER.error("/**************** Exception in TimePeriodService -> save()" + e);
        }return commonResponse;
    }
/**
 * Time period update
 *
 */

    public CommonResponse update(TimePeriodDTO timePeriodDTO){
        CommonResponse commonResponse = new CommonResponse();
        TimePeriod timePeriod;
        try {
            List<String> validationList = this.timePeriodValidation(timePeriodDTO);
            if (!validationList.isEmpty()){
                commonResponse.setErrorMessages(validationList);
                return commonResponse;
            }
            TimePeriod exitTimePeriod = this.getById(timePeriodDTO.getId());
            timePeriod = castTimePeriodDTOIntoTimePeriod(timePeriodDTO);
            exitTimePeriod.setId(timePeriod.getId());
            exitTimePeriod.setTimeCode(timePeriod.getTimeCode());
          //  exitTimePeriod.setTimeTo(DateTimeUtil.getFormattedDateTime(timePeriodDTO.getTimeTo()));
          //  exitTimePeriod.setTimeFrom(DateTimeUtil.getFormattedDateTime(timePeriodDTO.getTimeFrom()));
            exitTimePeriod.setCommonStatus(timePeriodDTO.getCommonStatus());
            timePeriod = timePeriodRepository.save(exitTimePeriod);
            commonResponse.setStatus(true);
            commonResponse.setPayload((List<Object>)timePeriod);
        }catch (Exception e){
            LOGGER.error("/**************** Exception in TimePeriodService -> save()" + e);
        }return commonResponse;
    }
    /**
     * Time-period Delete
     */

    public CommonResponse delete(String id){
        CommonResponse commonResponse =new CommonResponse();
        TimePeriod timePeriod;
        try {
            if (timePeriodRepository.existsById(Long.valueOf(id))){
                timePeriod = timePeriodRepository.findById(Long.parseLong(id)).get();
                timePeriod.setCommonStatus(CommonStatus.DELETE);
                timePeriodRepository.save(timePeriod);
                commonResponse.setStatus(true);
            }else
                commonResponse.setErrorMessages(Collections.singletonList(CommonMsg.NOT_FOUND));
        }catch (Exception e){
            LOGGER.error("/**************** Exception in TimePeriodService -> delete()" + e);
        }return commonResponse;
    }
    /**
     * Time-period getAll
     *
     */

     public CommonResponse getAll() {
         CommonResponse commonResponse = new CommonResponse();
         List<TimePeriodDTO> timePeriodDTOList;
         try {
             Predicate<TimePeriod> filterOnStatus = timePeriod -> timePeriod.getCommonStatus() != CommonStatus.DELETE;

             timePeriodDTOList =timePeriodRepository.findAll()
                     .stream()
                     .filter(filterOnStatus)
                     .map(this::castTimePeriodIntoTimePeriodDTO)
                     .collect(Collectors.toList());
             commonResponse.setStatus(true);
             commonResponse.setPayload(Collections.singletonList(timePeriodDTOList));
         }catch (Exception e){
             LOGGER.error("/**************** Exception in TimePeriodService -> save()" + e);
         }return commonResponse;
     }

    /**
     * get time period by status
     * @param status
     * @return
     */
    public CommonResponse getByStatus(CommonStatus status) {
        CommonResponse commonResponse = new CommonResponse();
        List<TimePeriodDTO> timePeriodDTOList;
        try {
            Predicate<TimePeriod> filterOnStatus = timePeriod -> timePeriod.getCommonStatus() == status;

            timePeriodDTOList =timePeriodRepository.findAll()
                    .stream()
                    .filter(filterOnStatus)
                    .map(this::castTimePeriodIntoTimePeriodDTO)
                    .collect(Collectors.toList());
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(timePeriodDTOList));
        }catch (Exception e){
            LOGGER.error("/**************** Exception in TimePeriodService -> save()" + e);
        }return commonResponse;
    }

    /**
     * Time period find by id
     * @param id
     * @return
     */
    public CommonResponse findById(final String id){
        CommonResponse commonResponse = new CommonResponse();
        TimePeriod timePeriod;
        try {
            timePeriod = timePeriodRepository.getById(Long.parseLong(id));
            commonResponse.setPayload(Collections.singletonList(castTimePeriodIntoTimePeriodDTO(timePeriod)));
            commonResponse.setStatus(true);

        }catch (Exception e){
            LOGGER.error("/**************** Exception in TimePeriodService -> save()" + e);
        }return commonResponse;
    }
    /**
     * Time-period getById
     * @param timePeriodId
     * @return
     */
    public TimePeriod getById(String timePeriodId)
    {return timePeriodRepository.findById(Long.parseLong(timePeriodId)).get();}
    /**
     * cast Time periodDTO Into Time period
     * @param timePeriodDTO
     * @return
     */

    public TimePeriod castTimePeriodDTOIntoTimePeriod (TimePeriodDTO timePeriodDTO){
        TimePeriod timePeriod = new TimePeriod();
        timePeriod.setTimeCode(timePeriodDTO.getTimeCode());
        timePeriod.setTimeFrom(DateTimeUtil.getFormattedTime(timePeriodDTO.getTimeFrom()));
        timePeriod.setTimeTo(DateTimeUtil.getFormattedTime(timePeriodDTO.getTimeTo()));
        timePeriod.setCommonStatus(timePeriodDTO.getCommonStatus());
        return timePeriod;
    }

    /**
     * cast time-period into time-periodDTO
     * @param timePeriod
     * @return
     */
    public TimePeriodDTO castTimePeriodIntoTimePeriodDTO(TimePeriod timePeriod)
    {   TimePeriodDTO timePeriodDTO = new TimePeriodDTO();
        timePeriodDTO.setTimeCode(timePeriod.getTimeCode());
        //timePeriodDTO.setTimeTo(DateTimeUtil.getFormattedDateTime(timePeriod.getTimeTo()));

        return modelMapper.map(timePeriod, TimePeriodDTO.class);}
    /**
     *
     * Time period Validation list
     * @param timePeriodDTO
     * @return
     */
    public List<String> timePeriodValidation (TimePeriodDTO timePeriodDTO){
        List<String> validationList = new ArrayList<>();

        if (CommonValidation.stringNullValidation(timePeriodDTO.getTimeTo()))
            validationList.add(CommonMsg.EMPTY_TIME_TO);
        if (CommonValidation.stringNullValidation(timePeriodDTO.getTimeFrom()))
            validationList.add(CommonMsg.EMPTY_TIME_FROM);


        return validationList;

    }

    private String genCode(TimePeriodDTO timePeriodDTO){
        String code = "";
        code = timePeriodDTO.getTimeTo().substring(0,2).concat("-").concat(timePeriodDTO.getTimeFrom().substring(0,2));
        return code;
    }
}
