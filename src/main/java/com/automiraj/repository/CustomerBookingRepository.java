package com.automiraj.repository;

import com.automiraj.constant.CommonStatus;
import com.automiraj.entity.Customer;
import com.automiraj.entity.CustomerBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerBookingRepository extends JpaRepository<CustomerBooking,Long> {

    CustomerBooking findCustomerBookingByCustomer(Customer customer);

    Boolean existsByCustomerAndCommonStatus(Customer customer, CommonStatus status);

}
