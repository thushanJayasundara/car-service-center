package com.automiraj.service;

import com.automiraj.constant.CommonMsg;
import com.automiraj.constant.CommonStatus;
import com.automiraj.dto.UserDTO;
import com.automiraj.entity.User;
import com.automiraj.repository.UserRepository;
import com.automiraj.util.CommonResponse;
import com.automiraj.util.CommonValidation;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * User save
     *
     * @param userDTO
     * @return
     */
    public CommonResponse save(UserDTO userDTO) {
        CommonResponse commonResponse = new CommonResponse();
        User user;
        try {
            List<String> validationList = this.userValidation(userDTO);
            if (!validationList.isEmpty()) {
                commonResponse.setErrorMessages(validationList);
                return commonResponse;
            }
            user = castUserDTOIntoUser(userDTO);
            user = userRepository.save(user);
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(user));

        } catch (Exception e) {
            LOGGER.error("/**************** Exception in UserService -> save()" + e);
        }
        return commonResponse;
    }

    /**
     * User update
     *
     * @param userDTO
     * @return
     */
    public CommonResponse update(UserDTO userDTO) {
        CommonResponse commonResponse = new CommonResponse();
        User user;
        try {
            List<String> validationList = this.userValidation(userDTO);
            if (!validationList.isEmpty()) {
                commonResponse.setErrorMessages(validationList);
                return commonResponse;
            }

            User exitUser = userRepository.findById(Long.parseLong(userDTO.getId())).get();

            exitUser.setUserName(userDTO.getUserName());
            exitUser.setUserRole(userDTO.getUserRole());
            exitUser.setCommonStatus(userDTO.getCommonStatus());
            user = userRepository.save(exitUser);
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(castUserIntoUserDTO(user)));
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in UserService -> save()" + e);
        }
        return commonResponse;
    }

    /**
     * User Delete
     *
     * @param id
     * @return
     */
    public CommonResponse delete(Long id) {
        CommonResponse commonResponse = new CommonResponse();
        User user;
        try {
            if (userRepository.existsById(id)) {
                user = userRepository.findById(Long.parseLong(String.valueOf(id))).get();
                user.setCommonStatus(CommonStatus.DELETE);
                userRepository.save(user);
                commonResponse.setStatus(true);
            } else
                commonResponse.setErrorMessages(Collections.singletonList(CommonMsg.NOT_FOUND));

        } catch (Exception e) {
            LOGGER.error("/**************** Exception in UserService -> save()" + e);
        }
        return commonResponse;
    }

    /**
     * User Get all
     *
     * @return
     */
    public CommonResponse getAll() {
        CommonResponse commonResponse = new CommonResponse();
        List<UserDTO> userDTOList;
        try {
            Predicate<User> filterOnStatus = user -> user.getCommonStatus() != CommonStatus.DELETE;
            userDTOList = userRepository.findAll()
                    .stream()
                    .filter(filterOnStatus)
                    .map(this::castUserIntoUserDTO)
                    .collect(Collectors.toList());
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(userDTOList));
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in UserServiceCustomerService -> getAll()" + e);
        }
        return commonResponse;
    }

    public CommonResponse updatePassword(UserDTO userDTO){
        CommonResponse commonResponse = new CommonResponse();
        try{
            User exitUser = userRepository.findByUserName(userDTO.getUserName());
            if(exitUser != null){
                exitUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                userRepository.save(exitUser);
                commonResponse.setStatus(true);
            }else
                commonResponse.setErrorMessages(Collections.singletonList(CommonMsg.NOT_FOUND));
        }catch (Exception e){
            LOGGER.error("/**************** Exception in UserServiceCustomerService -> updatePassword()" + e);
        }
        return commonResponse;
    }

    /**
     * @param userName
     * @return
     */
    public CommonResponse findByUserName(String userName) {
        CommonResponse commonResponse = new CommonResponse();
        UserDTO userDTO;
        try {
            userDTO = getByUserName(userName);
            commonResponse.setPayload(Collections.singletonList(userDTO));
            commonResponse.setStatus(true);
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in UserServiceCustomerService -> save()" + e);
        }
        return commonResponse;
    }

    /**
     * find by Username
     */
    public UserDTO getByUserName(String userName) {
        return castUserIntoUserDTO(userRepository.findByUserName(userName));
    }

    /**
     * User Find by id
     *
     * @param id
     * @return
     */
    public CommonResponse findById(String id) {
        CommonResponse commonResponse = new CommonResponse();
        User user;
        try {
            user = userRepository.getById(Long.parseLong(id));
            commonResponse.setPayload(Collections.singletonList(castUserIntoUserDTO(user)));
            commonResponse.setStatus(true);
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in UserService -> save()" + e);
        }
        return commonResponse;
    }

    /**
     * Cast user Intp UserDTO
     *
     * @param user
     * @return
     */
    public UserDTO castUserIntoUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    /**
     * cast UserDTO Into User
     *
     * @param userDTO
     * @return
     */
    public User castUserDTOIntoUser(UserDTO userDTO) {
        User user = new User();
        user.setUserName(userDTO.getUserName());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setUserRole(userDTO.getUserRole());
        user.setCommonStatus(userDTO.getCommonStatus());
        return user;
    }

    /**
     * User Validation
     *
     * @param customerDTO
     * @return
     */
    public List<String> userValidation(UserDTO customerDTO) {
        List<String> validationList = new ArrayList<>();
        if (CommonValidation.stringNullValidation(customerDTO.getUserName()))
            validationList.add(CommonMsg.EMPTY_USERNAME);
        if (!customerDTO.getEditable()) {
            if (CommonValidation.stringNullValidation(customerDTO.getPassword()))
                validationList.add(CommonMsg.EMPTY_PASSWORD);
        }
        if (customerDTO.getUserRole() == null)
            validationList.add(CommonMsg.EMPTY_ROLE);
        if (customerDTO.getCommonStatus() == null)
            validationList.add(CommonMsg.EMPTY_STATUS);
        return validationList;
    }
}
