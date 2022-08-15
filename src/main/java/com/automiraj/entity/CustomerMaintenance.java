package com.automiraj.entity;

import com.automiraj.constant.CommonStatus;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table (name="customermaintenance")

public class CustomerMaintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToOne
    @JoinColumn(name = "maintenance_id")
    private Maintenance maintenance;

    @Enumerated
    private CommonStatus commonStatus;



}
