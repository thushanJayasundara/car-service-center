package com.automiraj.entity;

import com.automiraj.constant.CommonStatus;
import lombok.Data;

import javax.persistence.*;


@Entity
@Data
@Table (name = "slotbooking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "slot_id")
    private Slot slot;

    @Enumerated
    private CommonStatus commonStatus;

}
