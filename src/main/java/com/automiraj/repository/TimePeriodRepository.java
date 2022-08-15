package com.automiraj.repository;

import com.automiraj.entity.TimePeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimePeriodRepository extends JpaRepository<TimePeriod,Long> {
}
