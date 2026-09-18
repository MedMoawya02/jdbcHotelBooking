package initializer;

import model.Room;
import model.RoomStatus;
import model.RoomType;
import repository.impl.InMemoryRoomRepository;

import java.math.BigDecimal;
import java.util.UUID;

public class RoomInitializer {
    public static void init(InMemoryRoomRepository inMemoryRoomRepository){
        Room room1=new Room(
                UUID.randomUUID(),
                "101",
                RoomType.SINGLE,
                3,
                new BigDecimal("500.00"),
                RoomStatus.AVAILABLE
        );
        Room room2=new Room(
                UUID.randomUUID(),
                "102",
                RoomType.SINGLE,
                3,
                new BigDecimal("550.00"),
                RoomStatus.AVAILABLE
        );
        Room room3=new Room(
                UUID.randomUUID(),
                "103",
                RoomType.SINGLE,
                3,
                new BigDecimal("1000.50"),
                RoomStatus.AVAILABLE
        );
        inMemoryRoomRepository.save(room1);
        inMemoryRoomRepository.save(room2);
        inMemoryRoomRepository.save(room3);
    }
}
