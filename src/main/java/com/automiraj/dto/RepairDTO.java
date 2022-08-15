package com.automiraj.dto;

import com.automiraj.constant.CommonStatus;
import com.automiraj.entity.Maintenance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepairDTO {

    private String id;
    private String repairName;
    private CommonStatus commonStatus;

}
