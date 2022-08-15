package com.automiraj.dto;

import com.automiraj.constant.CommonStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDTO {

    private String id;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String mobile;
    private String vehicleNo;
    private String nic;
    private CommonStatus commonStatus;
    private Boolean editable;

}
