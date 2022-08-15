package com.automiraj.entity;

import com.automiraj.constant.CommonStatus;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Table (name = "maintenancedetails")

public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "vehicleNo")
    private String vehicleNo;

    @Column (name = "otherDescription")
    private String otherDescription;

    @ManyToOne
    @JoinColumn(name = "maintainceType_id")
    private MaintenanceType maintenanceType;

    @Enumerated
    private CommonStatus commonStatus;

    @OneToOne(mappedBy = "maintenance" )
    private CustomerMaintenance customerMaintenance;

    @ManyToMany
    private Set<Repair> repair;


}
