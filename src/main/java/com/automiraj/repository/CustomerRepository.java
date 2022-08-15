package com.automiraj.repository;

import com.automiraj.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {

    Customer findByNic (String nic);

    Boolean existsByNic (String  nic);
}
