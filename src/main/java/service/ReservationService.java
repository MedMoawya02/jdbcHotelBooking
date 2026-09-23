package service;

import exception.InvalidReservationDateException;
import exception.RoomNotFoundException;
import model.Reservation;
import model.Room;
import repository.ReservationRepository;
import repository.RoomRepository;
//import repository.impl.InMemoryReservationRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ReservationService {
    private ReservationRepository reservationRepository;
    private RoomRepository roomRepository;
    public ReservationService(ReservationRepository reservationRepository ,RoomRepository roomRepository){
        this.reservationRepository=reservationRepository;
        this.roomRepository=roomRepository;
    }

    public void save(Reservation reservation)throws InvalidReservationDateException ,RoomNotFoundException {
        if(reservation.getCheckIn().isBefore(LocalDate.now())){
            throw new InvalidReservationDateException("La date d'entrée ne peut pas être dans le passé.");
        }
        if(!reservation.getCheckOut().isAfter(reservation.getCheckIn())){
            throw new InvalidReservationDateException("La date de sortie doit être après la date d'entrée");
        }
        //cherchez la chambre
        Optional<Room> roomRepo=roomRepository.findByRoomNumber(reservation.getRoomNumber());
        if(roomRepo.isEmpty()){
            throw new RoomNotFoundException("La chambre "+reservation.getRoomNumber()+" n existe pas");
        }
        Room room=roomRepo.get();
        List<Reservation> reservations=reservationRepository.findByRoomNumber(reservation.getRoomNumber());
        for(Reservation r:reservations){
            boolean reserved=reservation.getCheckIn().isBefore(r.getCheckOut())&&
                             reservation.getCheckOut().isAfter(r.getCheckIn());
            if(reserved){
                throw new InvalidReservationDateException("La chambre "+room.getRoomNumber()+"est deja reserve");
            }
        }

        reservationRepository.save(reservation);
    }
    public List<Reservation> getAllReservationByUser(UUID id){
        return reservationRepository.findByUserId(id);
    }
    //find by code
    public Optional<Reservation> findByCode(String codeReservation){
       return reservationRepository.findByCode(codeReservation);
    }
    //update
    public boolean update(Reservation reservation){
            return reservationRepository.update(reservation);
//        Optional<Reservation> reservationOptional=reservationRepository.findByCode(reservationCode);
//        if(reservationCode.isEmpty()){
//            System.out.println("reservation introuvable");
//            return;
//        }
//        Reservation reservation=reservationOptional.get();
//        reservation.setRoomNumber(roomNumber);
//        reservation.setCheckIn(checkIn);
//        reservation.setCheckOut(checkOut);
//        reservation.setNumberOfGuests(numberOfGuests);
//        reservation.setNumberOfNights(numberOfNights);
//        reservation.setTotalPrice(totalPrice);
//        reservationRepository.update(reservation);
//        System.out.println("Réservation modifiée avec succès !");
    }

    //delete
    public void delete(String codeReservation){
        Optional<Reservation> reservation=reservationRepository.findByCode(codeReservation);
        if(reservation.isEmpty()){
            System.out.print("Aucune reservation");
        }
        reservationRepository.annuler(reservation.get());
    }
}
