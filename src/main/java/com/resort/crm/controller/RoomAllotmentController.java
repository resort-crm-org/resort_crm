package com.resort.crm.controller;

import com.resort.crm.model.Room;
import com.resort.crm.service.RoomAllotmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*")
public class RoomAllotmentController {

    private final RoomAllotmentService roomAllotmentService;

    public RoomAllotmentController(RoomAllotmentService roomAllotmentService) {
        this.roomAllotmentService = roomAllotmentService;
    }

    @PostMapping("/allot")
    public ResponseEntity<Room> allotRoom(@RequestBody AllotRoomRequest request) {
        Room room = roomAllotmentService.allotRoomToGuest(request.guestId(), request.roomId(), request.days());
        return ResponseEntity.ok(room);
    }

    @PostMapping("/release/{guestId}")
    public ResponseEntity<Room> releaseRoom(@PathVariable Long guestId) {
        Room room = roomAllotmentService.releaseRoom(guestId);
        return ResponseEntity.ok(room);
    }

    public record AllotRoomRequest(Long guestId, Long roomId, Integer days) {
    }
}
