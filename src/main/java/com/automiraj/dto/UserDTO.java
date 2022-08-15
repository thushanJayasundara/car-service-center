package com.automiraj.dto;

import com.automiraj.constant.CommonStatus;
import com.automiraj.constant.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private String id;
    private String userName;
    private String password;
    private UserRole userRole;
    private CommonStatus commonStatus;
    private Boolean editable;

}
