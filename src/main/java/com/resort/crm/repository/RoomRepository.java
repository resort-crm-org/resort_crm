package com.resort.crm.repository;

import com.resort.crm.model.Room;
import com.resort.crm.model.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByStatus(RoomStatus status);
    Optional<Room> findByGuestId(Long guestId);
}
