package repository.jdbc;

import db.DatabaseConnection;
import model.Reservation;
import model.ReservationStatus;
import model.RoomStatus;
import model.User;
import repository.ReservationRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JdbcReservationRepository implements ReservationRepository {
    private Connection connection;
    public JdbcReservationRepository(){
        this.connection= DatabaseConnection.getInstance().getConnection();
    }
    @Override
    public void save(Reservation reservation){
        String sql= """
                INSERT INTO reservations(id,reservation_code,user_id,room_number,check_in,check_out,number_of_guests,number_of_nights,total_price,status) VALUES(?,?,?,?,?,?,?,?,?,?)
                """;
        try (PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setObject(1,reservation.getId());
            statement.setString(2,reservation.getReservationCode());
            statement.setObject(3,reservation.getUserId() );
            statement.setString(4,reservation.getRoomNumber());
            statement.setDate(5,java.sql.Date.valueOf(reservation.getCheckIn()));
            statement.setDate(6,java.sql.Date.valueOf(reservation.getCheckOut()));
            statement.setInt(7,reservation.getNumberOfGuests());
            statement.setLong(8,reservation.getNumberOfNights());
            statement.setBigDecimal(9,reservation.getTotalPrice());
            statement.setString(10, reservation.getStatus().name());
            statement.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException("Erreur save reservation : " + e.getMessage(), e);
        }
    }
    @Override
    public Optional<Reservation> findById(UUID id){
        return Optional.empty();
    }
    @Override
    public Optional<Reservation> findByCode(String code){
        String sql= """
                SELECT * FROM reservations WHERE reservation_code=?
                """;
        try (PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setString(1,code);
            ResultSet resultSet=statement.executeQuery();
            if(resultSet.next()){
                return Optional.of(mapRow(resultSet));
            }
        }catch (SQLException e){
            throw new RuntimeException("Erreur findByCode : " + e.getMessage(), e);
        }
        return Optional.empty();
    };
    @Override
    public List<Reservation> findByUserId(UUID userId){
        List<Reservation> reservations=new ArrayList<>();
        String sql= """
                SELECT * FROM reservations WHERE user_id=?
                """;
        try (PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setObject(1,userId);
            ResultSet resultSet= statement.executeQuery();
            while (resultSet.next()){
                reservations.add(mapRow(resultSet));
            }
        }catch (SQLException e){
            throw new RuntimeException("Erreur findByUserId : " + e.getMessage(), e);
        }
        return reservations;
    };
    @Override
    public List<Reservation> findByRoomNumber(String roomNumber){
        return new ArrayList<>();
    };
    @Override
    public List<Reservation> findAll(){
        List<Reservation> reservations=new ArrayList<>();
        String sql= """
                SELECT * FROM reservations
                """;
        try (PreparedStatement statement= connection.prepareStatement(sql)){
            ResultSet resultSet= statement.executeQuery();
            while(resultSet.next()){
                reservations.add(mapRow(resultSet));
            }
        }catch (SQLException e){
            throw new RuntimeException("Erreur find All reservations : " + e.getMessage(), e);
        }
        return reservations;
    };

    //update
    @Override
    public boolean update(Reservation reservation) {
        String sql = """
                UPDATE reservations
                SET room_number = ?,
                    check_in = ?,
                    check_out = ?,
                    number_of_guests = ?,
                    number_of_nights = ?,
                    total_price = ?,
                    status = ?
                WHERE reservation_code = ?
                  AND user_id = ?
            """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, reservation.getRoomNumber());
            statement.setDate(2, java.sql.Date.valueOf(reservation.getCheckIn()));
            statement.setDate(3, java.sql.Date.valueOf(reservation.getCheckOut()));
            statement.setInt(4, reservation.getNumberOfGuests());
            statement.setLong(5, reservation.getNumberOfNights());
            statement.setBigDecimal(6, reservation.getTotalPrice());
            statement.setString(7, reservation.getStatus().name());
            statement.setString(8, reservation.getReservationCode());  // WHERE
            statement.setObject(9, reservation.getUserId());           // WHERE
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur update reservation : " + e.getMessage(), e);
        }
    }

    @Override
    public void annuler(Reservation reservation){

    }

    private Reservation mapRow(ResultSet rs) throws SQLException {
        UUID id                 = rs.getObject("id", UUID.class);
        String reservationCode  = rs.getString("reservation_code");
        UUID userId             = rs.getObject("user_id", UUID.class);
        String roomNumber       = rs.getString("room_number");
        LocalDate checkIn       = rs.getDate("check_in").toLocalDate();
        LocalDate checkOut      = rs.getDate("check_out").toLocalDate();
        int numberOfGuests      = rs.getInt("number_of_guests");
        long numberOfNights     = rs.getLong("number_of_nights");
        BigDecimal totalPrice   = rs.getBigDecimal("total_price");
        ReservationStatus status = ReservationStatus.valueOf(rs.getString("status"));
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

        return new Reservation(
                id,
                reservationCode,
                userId,
                roomNumber,
                checkIn,
                checkOut,
                numberOfGuests,
                numberOfNights,
                totalPrice,
                status,
                createdAt
        );
    }
}
