package service;

import exception.RoomNotFoundException;
import model.Room;
import model.RoomStatus;
import repository.RoomRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import model.RoomStatus;
public class RoomService {
    private RoomRepository roomRepository;
    public RoomService(RoomRepository roomRepository){
        this.roomRepository=roomRepository;
    }
    public Room getRoomByNumber(String number)throws RoomNotFoundException {
        Optional<Room> room=roomRepository.findByRoomNumber(number);
        if(room.isEmpty()){
            throw new RoomNotFoundException("La chambre "+number+" n existe pas");
        }
        return room.get();
    }
    public List<Room> getAllRooms(){
        return roomRepository.findAll();
    }

    public void availableRooms(){
        List<Room> roomsAvailable=roomRepository.findAvailableRooms();
        if(roomsAvailable.isEmpty()){
            System.out.println("Aucune chambre disponible");
            return ;
        }
        System.out.println("---chambres disponible---");
        for(Room room:roomsAvailable){
            System.out.println(
                    "Chambre : " + room.getRoomNumber() +
                            " | Type : " + room.getType() +
                            " | Capacité : " + room.getCapacity() +
                            " | Prix : " + room.getPricePerNight()
            );
        }
    }
    public List<Room> filtrerParPrice(BigDecimal maxPrice){
        return roomRepository.findByMaxPrice(maxPrice);
    }
}
