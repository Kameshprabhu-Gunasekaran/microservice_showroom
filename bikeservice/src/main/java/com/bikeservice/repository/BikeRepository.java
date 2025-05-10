package com.bikeservice.repository;

import com.common.entity.Bike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BikeRepository extends JpaRepository<Bike, String> {
}
