package com.automiraj.dto;

import com.automiraj.constant.CommonStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerMaintenanceDTO {

    private String id;
    private CustomerDTO customerDTO;
    private MaintenanceDTO maintenanceDTO;
    private CommonStatus commonStatus;
}
