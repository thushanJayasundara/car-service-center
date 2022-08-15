package com.automiraj.entity;

import com.automiraj.constant.CommonStatus;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "repair")

public class Repair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "repairName")
    private String repairName;

    @Enumerated
    private CommonStatus commonStatus;


}
