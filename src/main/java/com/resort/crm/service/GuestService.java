package com.resort.crm.service;

import com.resort.crm.exception.ResourceNotFoundException;
import com.resort.crm.model.Guest;
import com.resort.crm.model.RoomStatus;
import com.resort.crm.repository.GuestRepository;
import com.resort.crm.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GuestService {

    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;

    public GuestService(GuestRepository guestRepository, RoomRepository roomRepository) {
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
    }

    public Guest createGuest(Guest guest) {
        return guestRepository.save(guest);
    }

    public List<Guest> getAllGuests() {
        return guestRepository.findAll();
    }

    @Transactional
    public Guest updateGuest(Long id, Guest updated) {
        Guest existing = guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found with id " + id));
        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setPhone(updated.getPhone());
        existing.setAddress(updated.getAddress());
        return existing;
    }

    @Transactional
    public void deleteGuest(Long id) {
        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found with id " + id));

        roomRepository.findByGuestId(id).ifPresent(room -> {
            room.setGuest(null);
            room.setStatus(RoomStatus.AVAILABLE);
            room.setAllottedDays(null);
            roomRepository.save(room);
        });

        guestRepository.delete(guest);
    }
}
