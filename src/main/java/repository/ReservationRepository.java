package repository;

import model.Reservation;
import model.Room;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public  interface ReservationRepository {
    void save(Reservation reservation);
    Optional<Reservation> findById(UUID id);
    Optional<Reservation> findByCode(String code);
    List<Reservation> findByUserId(UUID userId);
    List<Reservation> findByRoomNumber(String roomNumber);
    List<Reservation> findAll();
    public void update(Reservation reservation);
    public void annuler(Reservation reservation);

}
