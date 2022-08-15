package com.automiraj.dto;

import com.automiraj.constant.CommonStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class TimePeriodDTO {

    private String id;
    private String timeCode;
    private String timeTo;
    private String timeFrom;
    private CommonStatus commonStatus;
    private Boolean editable;

}
