package com.automiraj.entity;


import com.automiraj.constant.CommonStatus;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table (name="customerbooking")
public class CustomerBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @Enumerated
    private CommonStatus commonStatus;


}
