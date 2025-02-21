package com.example.good_lodging_service.repository;

import com.example.good_lodging_service.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {
}
