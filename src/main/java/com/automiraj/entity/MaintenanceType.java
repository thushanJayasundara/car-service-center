package com.automiraj.entity;

import com.automiraj.constant.CommonStatus;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table (name="maintenancetype")
public class MaintenanceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name="maintenacneName")
    private String maintenanceName;

    @Enumerated
    private CommonStatus commonStatus;
}
