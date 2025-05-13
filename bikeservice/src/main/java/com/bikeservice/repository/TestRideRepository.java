package com.bikeservice.repository;

import com.common.entity.TestRide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRideRepository extends JpaRepository<TestRide, String> {
}