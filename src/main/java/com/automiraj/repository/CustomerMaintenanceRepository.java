package com.automiraj.repository;

import com.automiraj.entity.CustomerMaintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerMaintenanceRepository extends JpaRepository<CustomerMaintenance,Long> {
}
