package com.example.good_lodging_service.repository;

import com.example.good_lodging_service.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
