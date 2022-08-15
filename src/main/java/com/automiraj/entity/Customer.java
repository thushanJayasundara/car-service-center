package com.automiraj.entity;

import com.automiraj.constant.CommonStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Table (name = "customeregistration")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "firstName")
    private String firstName;

    @Column (name = "lastName")
    private String lastName;

    @Column (name = "address")
    private String address;

    @Column (name ="email")
    private String email;

    @Column (name = "mobile")
    private Integer mobile;

    @Column (name = "vehicleNo")
    private String vehicleNo;

    @Column (name ="nic")
    private String nic;

    @Enumerated
    private CommonStatus commonStatus;

    @OneToMany(mappedBy = "customer")
    private Set<CustomerBooking> customerBookings;

    @OneToMany(mappedBy = "customer")
    private Set<CustomerMaintenance> customerMaintenance;


}
