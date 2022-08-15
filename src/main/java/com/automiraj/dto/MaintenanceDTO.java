package com.automiraj.dto;

import com.automiraj.constant.CommonStatus;
import com.automiraj.entity.CustomerMaintenance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaintenanceDTO {

    private String id;
    private Set<RepairDTO> repairDTOS;
    private String vehicleNo;
    private MaintenanceTypeDTO maintenanceType;
    private String otherDescription;
    private CustomerDTO customerDTO;
    private CommonStatus commonStatus;

}
