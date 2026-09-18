package repository.impl;

import model.Room;
import model.RoomStatus;
import repository.RoomRepository;

import java.math.BigDecimal;
import java.util.*;

public class InMemoryRoomRepository implements RoomRepository {
    private Map<UUID, Room > rooms=new HashMap<>();

    @Override
    public void save(Room room){
            rooms.put(room.getId(),room);
    }
    @Override
    public Optional<Room>findByRoomNumber(String roomNumber){
        for(Room room:rooms.values()){
            if(room.getRoomNumber().equals(roomNumber)){
                return Optional.of(room);
            };
        }
        return Optional.empty();
    }

    @Override
    public  List<Room> findAll(){
      return new ArrayList<>(rooms.values());
    }

    @Override
    public List<Room> findAvailableRooms(){
        return rooms.values().stream().filter(r->r.isAvailable()).toList();
    }
    @Override
    public List<Room> findByMaxPrice(BigDecimal maxPrice){
        return rooms.values().stream()
                .filter(r->r.getPricePerNight().compareTo(maxPrice)<=0)
                .toList();
    }
}
