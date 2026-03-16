package com.resort.crm.service;

import com.resort.crm.exception.BadRequestException;
import com.resort.crm.exception.ResourceNotFoundException;
import com.resort.crm.model.Guest;
import com.resort.crm.model.Room;
import com.resort.crm.model.RoomStatus;
import com.resort.crm.repository.GuestRepository;
import com.resort.crm.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomAllotmentServiceTest {

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomAllotmentService roomAllotmentService;

    @Test
    void allotRoomRejectsNonPositiveDays() {
        assertThatThrownBy(() -> roomAllotmentService.allotRoomToGuest(1L, 2L, 0))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Days must be greater than zero");
    }

    @Test
    void allotRoomThrowsWhenGuestIsMissing() {
        when(guestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roomAllotmentService.allotRoomToGuest(1L, 2L, 3))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Guest not found with id 1");
    }

    @Test
    void allotRoomThrowsWhenRoomIsMissing() {
        Guest guest = new Guest();
        when(guestRepository.findById(1L)).thenReturn(Optional.of(guest));
        when(roomRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roomAllotmentService.allotRoomToGuest(1L, 2L, 3))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Room not found with id 2");
    }

    @Test
    void allotRoomRejectsGuestWithExistingAllotment() {
        Guest guest = new Guest();
        Room room = new Room();
        Room existingRoom = new Room();
        room.setStatus(RoomStatus.AVAILABLE);
        when(guestRepository.findById(1L)).thenReturn(Optional.of(guest));
        when(roomRepository.findById(2L)).thenReturn(Optional.of(room));
        when(roomRepository.findByGuestId(1L)).thenReturn(Optional.of(existingRoom));

        assertThatThrownBy(() -> roomAllotmentService.allotRoomToGuest(1L, 2L, 3))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Guest already has an allotted room");
    }

    @Test
    void allotRoomRejectsUnavailableRoom() {
        Guest guest = new Guest();
        Room room = new Room();
        room.setStatus(RoomStatus.OCCUPIED);
        when(guestRepository.findById(1L)).thenReturn(Optional.of(guest));
        when(roomRepository.findById(2L)).thenReturn(Optional.of(room));
        when(roomRepository.findByGuestId(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roomAllotmentService.allotRoomToGuest(1L, 2L, 3))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Room is not available for allotment");
    }

    @Test
    void allotRoomAssignsGuestAndMarksRoomOccupied() {
        Guest guest = new Guest("Alex", "alex@example.com", "1234567890", "Beach Road");
        Room room = new Room();
        room.setStatus(RoomStatus.AVAILABLE);
        when(guestRepository.findById(1L)).thenReturn(Optional.of(guest));
        when(roomRepository.findById(2L)).thenReturn(Optional.of(room));
        when(roomRepository.findByGuestId(1L)).thenReturn(Optional.empty());
        when(roomRepository.save(room)).thenReturn(room);

        Room saved = roomAllotmentService.allotRoomToGuest(1L, 2L, 6);

        assertThat(saved).isSameAs(room);
        assertThat(room.getGuest()).isSameAs(guest);
        assertThat(room.getStatus()).isEqualTo(RoomStatus.OCCUPIED);
        assertThat(room.getAllottedDays()).isEqualTo(6);
        verify(roomRepository).save(room);
    }

    @Test
    void releaseRoomClearsAllotmentDetails() {
        Guest guest = new Guest();
        Room room = new Room();
        room.setGuest(guest);
        room.setStatus(RoomStatus.OCCUPIED);
        room.setAllottedDays(2);
        when(roomRepository.findByGuestId(4L)).thenReturn(Optional.of(room));
        when(roomRepository.save(room)).thenReturn(room);

        Room saved = roomAllotmentService.releaseRoom(4L);

        assertThat(saved).isSameAs(room);
        assertThat(room.getGuest()).isNull();
        assertThat(room.getStatus()).isEqualTo(RoomStatus.AVAILABLE);
        assertThat(room.getAllottedDays()).isNull();
        verify(roomRepository).save(room);
    }

    @Test
    void releaseRoomThrowsWhenGuestHasNoRoom() {
        when(roomRepository.findByGuestId(4L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roomAllotmentService.releaseRoom(4L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("No room found for guest id 4");
    }
}