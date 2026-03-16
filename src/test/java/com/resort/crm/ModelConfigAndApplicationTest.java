package com.resort.crm;

import com.resort.crm.config.CorsConfig;
import com.resort.crm.exception.BadRequestException;
import com.resort.crm.exception.ResourceNotFoundException;
import com.resort.crm.model.Guest;
import com.resort.crm.model.Room;
import com.resort.crm.model.RoomStatus;
import com.resort.crm.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ModelConfigAndApplicationTest {

    @Test
    void guestConstructorAndAccessorsWork() {
        Guest guest = new Guest("Alex", "alex@example.com", "1234567890", "Beach Road");
        guest.setId(10L);
        guest.setName("Blair");
        guest.setEmail("blair@example.com");
        guest.setPhone("9999999999");
        guest.setAddress("Palm Street");

        assertThat(guest.getId()).isEqualTo(10L);
        assertThat(guest.getName()).isEqualTo("Blair");
        assertThat(guest.getEmail()).isEqualTo("blair@example.com");
        assertThat(guest.getPhone()).isEqualTo("9999999999");
        assertThat(guest.getAddress()).isEqualTo("Palm Street");
    }

    @Test
    void roomDefaultsAndAccessorsWork() {
        Guest guest = new Guest();
        Room room = new Room();
        room.setId(20L);
        room.setRoomNumber(101);
        room.setStatus(RoomStatus.OCCUPIED);
        room.setAllottedDays(7);
        room.setGuest(guest);

        assertThat(room.getId()).isEqualTo(20L);
        assertThat(room.getRoomNumber()).isEqualTo(101);
        assertThat(room.getStatus()).isEqualTo(RoomStatus.OCCUPIED);
        assertThat(room.getAllottedDays()).isEqualTo(7);
        assertThat(room.getGuest()).isSameAs(guest);
    }

    @Test
    void roomStartsAsAvailableByDefault() {
        assertThat(new Room().getStatus()).isEqualTo(RoomStatus.AVAILABLE);
    }

    @Test
    void roomStatusEnumContainsExpectedValues() {
        assertThat(RoomStatus.values()).containsExactly(RoomStatus.AVAILABLE, RoomStatus.OCCUPIED);
    }

    @Test
    void exceptionMessagesAreRetained() {
        assertThat(new BadRequestException("bad input")).hasMessage("bad input");
        assertThat(new ResourceNotFoundException("missing")).hasMessage("missing");
    }

    @Test
    void corsConfigRegistersMappingsWithoutError() {
        assertThatCode(() -> new CorsConfig().addCorsMappings(new CorsRegistry()))
                .doesNotThrowAnyException();
    }

    @Test
    void seedRoomsCreatesFifteenRoomsWhenRepositoryIsEmpty() throws Exception {
        RoomRepository roomRepository = mock(RoomRepository.class);
        when(roomRepository.count()).thenReturn(0L);
        CommandLineRunner runner = new ResortCrmApplication().seedRooms(roomRepository);

        runner.run();

        ArgumentCaptor<Room> captor = ArgumentCaptor.forClass(Room.class);
        verify(roomRepository, times(15)).save(captor.capture());
        List<Room> savedRooms = captor.getAllValues();
        assertThat(savedRooms).hasSize(15);
        assertThat(savedRooms).extracting(Room::getRoomNumber).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        assertThat(savedRooms).extracting(Room::getStatus).containsOnly(RoomStatus.AVAILABLE);
    }

    @Test
    void seedRoomsSkipsSeedingWhenRoomsAlreadyExist() throws Exception {
        RoomRepository roomRepository = mock(RoomRepository.class);
        when(roomRepository.count()).thenReturn(4L);
        CommandLineRunner runner = new ResortCrmApplication().seedRooms(roomRepository);

        runner.run();

        verify(roomRepository, never()).save(org.mockito.ArgumentMatchers.any(Room.class));
    }
}