package model;

import java.math.BigDecimal;
import java.util.UUID;

public class Room {
    private UUID id;
    private String roomNumber;
    private RoomType type;
    private int capacity;
    private BigDecimal pricePerNight;
    private RoomStatus status;
    public Room(UUID id,String roomNumber,RoomType type,int capacity,BigDecimal pricePerNight,RoomStatus status){
        this.id=id;
        this.roomNumber=roomNumber;
        this.type=type;
        this.capacity=capacity;
        this.pricePerNight=pricePerNight;
        this.status=status;
    }
    //Getters
    public UUID getId() {
        return id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public RoomType getType() {
        return type;
    }

    public int getCapacity() {
        return capacity;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public RoomStatus getStatus() {
        return status;
    }
    public boolean isAvailable(){
        if(this.status==RoomStatus.AVAILABLE){
            return  true;
        }else {
            return false;
        }
    }
    //setters
    public void setId(UUID id){
        this.id=id;
    }
    public void setRoomNumber(String roomNumber){
        this.roomNumber=roomNumber;
    }
    public void setType(RoomType type){
        this.type=type;
    }
    public void setCapacity(Integer capacity){
        this.capacity=capacity;
    }
    public void setPricePerNight(BigDecimal pricePerNight){
        this.pricePerNight=pricePerNight;
    }
    public void setStatus(RoomStatus roomStatus){
        this.status=roomStatus;
    }
}
