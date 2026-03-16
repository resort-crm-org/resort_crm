package com.resort.crm.service;

import com.resort.crm.model.Room;
import com.resort.crm.model.RoomStatus;
import com.resort.crm.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    @Test
    void getAllRoomsReturnsRepositoryResults() {
        List<Room> rooms = List.of(new Room(), new Room());
        when(roomRepository.findAll()).thenReturn(rooms);

        assertThat(roomService.getAllRooms()).containsExactlyElementsOf(rooms);
        verify(roomRepository).findAll();
    }

    @Test
    void getAvailableRoomsReturnsOnlyAvailableRooms() {
        List<Room> rooms = List.of(new Room(), new Room());
        when(roomRepository.findByStatus(RoomStatus.AVAILABLE)).thenReturn(rooms);

        assertThat(roomService.getAvailableRooms()).containsExactlyElementsOf(rooms);
        verify(roomRepository).findByStatus(RoomStatus.AVAILABLE);
    }
}