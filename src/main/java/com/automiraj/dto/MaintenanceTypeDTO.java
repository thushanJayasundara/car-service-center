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
public class MaintenanceTypeDTO {

    private String id;
    private String maintenanceName;
    private CommonStatus commonStatus;

}
