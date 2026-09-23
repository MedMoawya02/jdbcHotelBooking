//package repository.impl;
//
//import model.Reservation;
//import repository.ReservationRepository;
//
//import java.util.*;
//
//public class InMemoryReservationRepository  {
//    private Map<UUID,Reservation> reservations=new HashMap<>();
//    @Override
//    public void save(Reservation reservation){
//        reservations.put(UUID.randomUUID(),reservation);
//    }
//    @Override
//    public Optional<Reservation> findById(UUID id){
//        return Optional.empty();
//    }
//    @Override
//    public Optional<Reservation> findByCode(String code){
//        for (Reservation r:reservations.values()){
//            if(r.getReservationCode().equals(code));
//            return Optional.ofNullable(r);
//        }
//        return Optional.empty();
//    };
//    @Override
//    public List<Reservation> findByUserId(UUID userId){
//        return reservations.values().stream()
//                .filter(re->re.getUserId().equals(userId))
//                .toList();
//    };
//    @Override
//    public List<Reservation> findByRoomNumber(String roomNumber){
//        return new ArrayList<>(reservations.values());
//    };
//    @Override
//    public List<Reservation> findAll(){
//        return new ArrayList<>(reservations.values());
//    };
//
//    //update
//    @Override
//    public void update(Reservation reservation){
//        reservations.put(reservation.getId(),reservation);
//    }
//
//    @Override
//    public void annuler(Reservation reservation){
//        reservations.remove(reservation.getId());
//    }
//}
//

