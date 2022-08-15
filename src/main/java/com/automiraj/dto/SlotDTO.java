package com.automiraj.dto;


import com.automiraj.constant.Availability;
import com.automiraj.constant.CommonStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.net.ssl.SSLSession;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SlotDTO {

    private String id;
    private String slotName;
    private TimePeriodDTO timePeriod;
    private Availability availability;
    private CommonStatus commonStatus;
    private MaintenanceTypeDTO maintenanceType;
}
