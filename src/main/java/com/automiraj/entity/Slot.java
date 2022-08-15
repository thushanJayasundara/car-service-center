package com.automiraj.entity;

import com.automiraj.constant.Availability;
import com.automiraj.constant.CommonStatus;
import lombok.Data;

import javax.persistence.*;


@Entity
@Data
@Table(name = "slot")

public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name="slotname")
    private String slotName;

    @Enumerated
    private CommonStatus commonStatus;

    @Enumerated
    private Availability availability;

    @ManyToOne
    @JoinColumn(name = "timePeriod_id")
    private TimePeriod timePeriod;

    @ManyToOne
    @JoinColumn(name = "maintainceType_id")
    private MaintenanceType maintenanceType;


}
