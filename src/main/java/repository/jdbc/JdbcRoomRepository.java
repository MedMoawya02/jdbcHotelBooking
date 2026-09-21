package repository.jdbc;

import db.DatabaseConnection;
import model.Room;
import model.RoomStatus;
import model.RoomType;
import repository.RoomRepository;

import javax.security.sasl.SaslException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JdbcRoomRepository implements RoomRepository {
    private final Connection connection;
    public JdbcRoomRepository(){
        this.connection= DatabaseConnection.getInstance().getConnection();
    }
    @Override
    public void save(Room room){
        String sql= """
                   INSERT INTO rooms (id, room_number, type, capacity, price_per_night, status) VALUES(?,?,?,?,?,?)
                """;
        try(PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setObject(1,room.getId());
            statement.setString(2,room.getRoomNumber());
            statement.setString(3,room.getType().name());
            statement.setInt(4,room.getCapacity());
            statement.setBigDecimal(5,room.getPricePerNight());
            statement.setString(6,room.getStatus().name());
            statement.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException("Erreur save room : " + e.getMessage(), e);
        }

    }
    @Override
    public Optional<Room> findByRoomNumber(String roomNumber){
        String sql= """
                SELECT * FROM  rooms WHERE room_number=?
                """;
        try (PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setString(1,roomNumber);
            ResultSet resultSet= statement.executeQuery();
            if(resultSet.next()){
                UUID id=resultSet.getObject("id",UUID.class);
                String room_number=resultSet.getString("room_number");
                RoomType roomType=RoomType.valueOf(resultSet.getString("type"));
                int capacity=resultSet.getInt("capacity");
                BigDecimal price_per_night=resultSet.getBigDecimal("price_per_night");
                RoomStatus roomStatus=RoomStatus.valueOf(resultSet.getString("status"));
                Room room=new Room(id,room_number,roomType,capacity,price_per_night,roomStatus);
                return Optional.of(room);
            }
        }catch (SQLException e){
            throw new RuntimeException(
                    "Erreur lors de la récupération des utilisateurs : "
                            + e.getMessage(),
                    e
            );
        }
        return Optional.empty();
    }

    @Override
    public boolean update(Room room){
        String sql= """
                    UPDATE rooms set type=?, capacity=?, price_per_night=?, status=? 
                    WHERE room_number=?
                """;
        try (PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setString(1, room.getType().name());
            statement.setInt(2, room.getCapacity());
            statement.setBigDecimal(3, room.getPricePerNight());
            statement.setString(4, room.getStatus().name());
            statement.setString(5, room.getRoomNumber());   // WHERE
            return statement.executeUpdate()>0 ;
        }catch (SQLException e){
            throw new RuntimeException("Erreur update room : " + e.getMessage(), e);

        }
    }

    @Override
    public List<Room> findAll(){
        List<Room> rooms=new ArrayList<>();
        String sql= """
                SELECT * FROM rooms 
                """;
        try (PreparedStatement statement=connection.prepareStatement(sql)){
            ResultSet resultSet=statement.executeQuery();
            while (resultSet.next()){
                UUID id=resultSet.getObject("id",UUID.class);
                String room_number=resultSet.getString("room_number");
                RoomType roomType=RoomType.valueOf(resultSet.getString("type"));
                int capacity=resultSet.getInt("capacity");
                BigDecimal price_per_night=resultSet.getBigDecimal("price_per_night");
                RoomStatus roomStatus=RoomStatus.valueOf(resultSet.getString("status"));
                Room room=new Room(id,room_number,roomType,capacity,price_per_night,roomStatus);
                rooms.add(room);
            }
        }catch (SQLException e){
            throw new RuntimeException(
                    "Erreur lors de la récupération des utilisateurs : "
                            + e.getMessage(),
                    e
            );
        }
        return rooms;
    }
    @Override
    public List<Room> findAvailableRooms(){
        return new ArrayList<>() ;
    }
    @Override
    public List<Room> findByMaxPrice(BigDecimal maxPrice){
        return new ArrayList<>();
    }
    @Override
    public boolean delete(String roomNumber){
        String sql= """
                    DELETE  FROM rooms WHERE room_number=?
                """;
        try(PreparedStatement statement=connection.prepareStatement(sql)) {
            statement.setString(1,roomNumber);
            return statement.executeUpdate()>0;
        }catch (SQLException e){
            throw new RuntimeException("Erreur delete room : " + e.getMessage(), e);
        }
    }
}
