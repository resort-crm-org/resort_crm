package com.resort.crm.service;

import com.resort.crm.exception.BadRequestException;
import com.resort.crm.exception.ResourceNotFoundException;
import com.resort.crm.model.Guest;
import com.resort.crm.model.Room;
import com.resort.crm.model.RoomStatus;
import com.resort.crm.repository.GuestRepository;
import com.resort.crm.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoomAllotmentService {

    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;

    public RoomAllotmentService(GuestRepository guestRepository, RoomRepository roomRepository) {
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
    }

    @Transactional
    public Room allotRoomToGuest(Long guestId, Long roomId, Integer days) {
        if (days == null || days <= 0) {
            throw new BadRequestException("Days must be greater than zero");
        }

        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found with id " + guestId));

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id " + roomId));

        roomRepository.findByGuestId(guestId).ifPresent(existing -> {
            throw new BadRequestException("Guest already has an allotted room");
        });

        if (room.getStatus() != RoomStatus.AVAILABLE) {
            throw new BadRequestException("Room is not available for allotment");
        }

        room.setGuest(guest);
        room.setStatus(RoomStatus.OCCUPIED);
        room.setAllottedDays(days);
        return roomRepository.save(room);
    }

    @Transactional
    public Room releaseRoom(Long guestId) {
        Room room = roomRepository.findByGuestId(guestId)
                .orElseThrow(() -> new ResourceNotFoundException("No room found for guest id " + guestId));

        room.setGuest(null);
        room.setStatus(RoomStatus.AVAILABLE);
        room.setAllottedDays(null);
        return roomRepository.save(room);
    }
}
