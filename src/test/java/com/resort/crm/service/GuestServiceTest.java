package com.resort.crm.service;

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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GuestServiceTest {

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private GuestService guestService;

    @Test
    void createGuestSavesGuest() {
        Guest guest = new Guest("Alex", "alex@example.com", "1234567890", "Beach Road");
        when(guestRepository.save(guest)).thenReturn(guest);

        Guest created = guestService.createGuest(guest);

        assertThat(created).isSameAs(guest);
        verify(guestRepository).save(guest);
    }

    @Test
    void getAllGuestsReturnsRepositoryResults() {
        List<Guest> guests = List.of(
                new Guest("Alex", "alex@example.com", "1234567890", "Beach Road"),
                new Guest("Blair", "blair@example.com", "0987654321", "Palm Street")
        );
        when(guestRepository.findAll()).thenReturn(guests);

        assertThat(guestService.getAllGuests()).containsExactlyElementsOf(guests);
    }

    @Test
    void updateGuestUpdatesManagedEntityFields() {
        Guest existing = new Guest("Alex", "alex@example.com", "1234567890", "Old Address");
        Guest updated = new Guest("Blair", "blair@example.com", "5555555555", "New Address");
        when(guestRepository.findById(7L)).thenReturn(Optional.of(existing));

        Guest result = guestService.updateGuest(7L, updated);

        assertThat(result).isSameAs(existing);
        assertThat(existing.getName()).isEqualTo("Blair");
        assertThat(existing.getEmail()).isEqualTo("blair@example.com");
        assertThat(existing.getPhone()).isEqualTo("5555555555");
        assertThat(existing.getAddress()).isEqualTo("New Address");
    }

    @Test
    void updateGuestThrowsWhenGuestIsMissing() {
        when(guestRepository.findById(7L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> guestService.updateGuest(7L, new Guest()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Guest not found with id 7");
    }

    @Test
    void deleteGuestReleasesAssignedRoomBeforeDeletingGuest() {
        Guest guest = new Guest("Alex", "alex@example.com", "1234567890", "Beach Road");
        Room room = new Room();
        room.setGuest(guest);
        room.setStatus(RoomStatus.OCCUPIED);
        room.setAllottedDays(4);
        when(guestRepository.findById(3L)).thenReturn(Optional.of(guest));
        when(roomRepository.findByGuestId(3L)).thenReturn(Optional.of(room));

        guestService.deleteGuest(3L);

        assertThat(room.getGuest()).isNull();
        assertThat(room.getStatus()).isEqualTo(RoomStatus.AVAILABLE);
        assertThat(room.getAllottedDays()).isNull();
        verify(roomRepository).save(room);
        verify(guestRepository).delete(guest);
    }

    @Test
    void deleteGuestDeletesGuestWhenNoRoomIsAssigned() {
        Guest guest = new Guest("Alex", "alex@example.com", "1234567890", "Beach Road");
        when(guestRepository.findById(5L)).thenReturn(Optional.of(guest));
        when(roomRepository.findByGuestId(5L)).thenReturn(Optional.empty());

        guestService.deleteGuest(5L);

        verify(roomRepository, never()).save(org.mockito.ArgumentMatchers.any(Room.class));
        verify(guestRepository).delete(guest);
    }

    @Test
    void deleteGuestThrowsWhenGuestIsMissing() {
        when(guestRepository.findById(9L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> guestService.deleteGuest(9L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Guest not found with id 9");
    }
}