package com.automiraj.entity;

import com.automiraj.constant.CommonStatus;
import com.automiraj.constant.UserRole;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table (name = "userlogin")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userName")
    private String userName;

    @Column(name = "password")
    private String password;

    @Enumerated
    private UserRole userRole;

    @Enumerated
    private CommonStatus commonStatus;


}