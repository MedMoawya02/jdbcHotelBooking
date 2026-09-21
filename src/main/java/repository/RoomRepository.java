package repository;

import model.Room;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface RoomRepository {
    void save(Room room);
    Optional<Room> findByRoomNumber(String roomNumber);
    boolean update(Room room);
    List<Room> findAll();
    List<Room> findAvailableRooms();
    List<Room> findByMaxPrice(BigDecimal maxPrice);
    boolean delete(String roomNumber);

}
