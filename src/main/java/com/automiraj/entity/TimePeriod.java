package com.automiraj.entity;

import com.automiraj.constant.CommonStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Data
@Table (name = "timeperiod")

public class TimePeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "timeCode")
    private String timeCode;

    @Column(name = "timeTo")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime timeTo;

    @Column(name = "timeFrom")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime timeFrom;

    @Enumerated
    private CommonStatus commonStatus;

}
