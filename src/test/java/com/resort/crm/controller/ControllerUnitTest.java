package com.resort.crm.controller;

import com.resort.crm.model.Guest;
import com.resort.crm.model.Room;
import com.resort.crm.service.GuestService;
import com.resort.crm.service.RoomAllotmentService;
import com.resort.crm.service.RoomService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ControllerUnitTest {

    @Mock
    private GuestService guestService;

    @Mock
    private RoomService roomService;

    @Mock
    private RoomAllotmentService roomAllotmentService;

    @Test
    void guestControllerDelegatesCrudOperations() {
        GuestController controller = new GuestController(guestService);
        Guest guest = new Guest("Alex", "alex@example.com", "1234567890", "Beach Road");
        when(guestService.createGuest(guest)).thenReturn(guest);
        when(guestService.getAllGuests()).thenReturn(List.of(guest));
        when(guestService.updateGuest(3L, guest)).thenReturn(guest);

        ResponseEntity<Guest> created = controller.createGuest(guest);
        ResponseEntity<List<Guest>> allGuests = controller.getAllGuests();
        ResponseEntity<Guest> updated = controller.updateGuest(3L, guest);
        ResponseEntity<Void> deleted = controller.deleteGuest(3L);

        assertThat(created.getBody()).isSameAs(guest);
        assertThat(allGuests.getBody()).containsExactly(guest);
        assertThat(updated.getBody()).isSameAs(guest);
        assertThat(deleted.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(guestService).createGuest(guest);
        verify(guestService).getAllGuests();
        verify(guestService).updateGuest(3L, guest);
        verify(guestService).deleteGuest(3L);
    }

    @Test
    void roomControllerReturnsAllAndAvailableRooms() {
        RoomController controller = new RoomController(roomService);
        Room room = new Room();
        when(roomService.getAllRooms()).thenReturn(List.of(room));
        when(roomService.getAvailableRooms()).thenReturn(List.of(room));

        ResponseEntity<List<Room>> allRooms = controller.getAllRooms();
        ResponseEntity<List<Room>> availableRooms = controller.getAvailableRooms();

        assertThat(allRooms.getBody()).containsExactly(room);
        assertThat(availableRooms.getBody()).containsExactly(room);
    }

    @Test
    void roomAllotmentControllerDelegatesAllotAndRelease() {
        RoomAllotmentController controller = new RoomAllotmentController(roomAllotmentService);
        Room room = new Room();
        RoomAllotmentController.AllotRoomRequest request = new RoomAllotmentController.AllotRoomRequest(1L, 2L, 5);
        when(roomAllotmentService.allotRoomToGuest(1L, 2L, 5)).thenReturn(room);
        when(roomAllotmentService.releaseRoom(1L)).thenReturn(room);

        ResponseEntity<Room> allotted = controller.allotRoom(request);
        ResponseEntity<Room> released = controller.releaseRoom(1L);

        assertThat(request.guestId()).isEqualTo(1L);
        assertThat(request.roomId()).isEqualTo(2L);
        assertThat(request.days()).isEqualTo(5);
        assertThat(allotted.getBody()).isSameAs(room);
        assertThat(released.getBody()).isSameAs(room);
        verify(roomAllotmentService).allotRoomToGuest(1L, 2L, 5);
        verify(roomAllotmentService).releaseRoom(1L);
    }
}