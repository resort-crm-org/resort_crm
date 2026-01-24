package com.resort.crm;

import com.resort.crm.model.Room;
import com.resort.crm.model.RoomStatus;
import com.resort.crm.repository.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.stream.IntStream;

@SpringBootApplication
public class ResortCrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResortCrmApplication.class, args);
    }

    @Bean
    CommandLineRunner seedRooms(RoomRepository roomRepository) {
        return args -> {
            if (roomRepository.count() == 0) {
                IntStream.rangeClosed(1, 15).forEach(number -> {
                    Room room = new Room();
                    room.setRoomNumber(number);
                    room.setStatus(RoomStatus.AVAILABLE);
                    roomRepository.save(room);
                });
            }
        };
    }
}
