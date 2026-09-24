package service;

import model.Room;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class PricingService {
    private static final BigDecimal HIGH_SEASON_MULT   = new BigDecimal("1.30"); // +30%
    private static final BigDecimal LOW_SEASON_MULT    = new BigDecimal("0.85"); // -15%
    private static final BigDecimal WEEKEND_MULT       = new BigDecimal("1.15"); // +15%
    private static final BigDecimal LONG_STAY_7_MULT   = new BigDecimal("0.90"); // -10%
    private static final BigDecimal LONG_STAY_14_MULT  = new BigDecimal("0.85"); // -15%
    private static final BigDecimal EARLY_BOOKING_MULT = new BigDecimal("0.95"); // -5%
    private static final BigDecimal LAST_MINUTE_MULT   = new BigDecimal("1.10"); // +10%
//    public BigDecimal compteTotalPrice(Room room, LocalDate checkIn, LocalDate checkOut, LocalDateTime createdAt){
//        long nights= ChronoUnit.DAYS.between(checkIn,checkOut);
//        if(nights<=0){
//            throw new IllegalArgumentException("Le séjour doit durer au moins 1 nuit");
//        }
//        BigDecimal basePrice=room.getPricePerNight();
//        BigDecimal totalBrut=BigDecimal.ZERO;
//        LocalDate current=checkIn;
//        while (current.isBefore(checkOut)){
//            BigDecimal nightPrice=basePrice;
//            int mounth=current.getMonthValue();
//            if(mounth==7||mounth==8){
//                nightPrice=nightPrice.multiply(HIGH_SEASON_MULT);
//            } else if (mounth==11||mounth==12||mounth==1||mounth==2) {
//                nightPrice=nightPrice.multiply(LOW_SEASON_MULT);
//            }
//            //add weekend
//            DayOfWeek dow=current.getDayOfWeek();
//            if(dow==DayOfWeek.FRIDAY||dow==DayOfWeek.SATURDAY){
//                nightPrice=nightPrice.multiply(WEEKEND_MULT);
//            }
//            totalBrut=totalBrut.add(nightPrice);
//            current=current.plusDays(1);
//        }
//        //2
//        BigDecimal total=totalBrut;
//        if(nights>=14){
//            total=total.multiply(LONG_STAY_14_MULT);
//        } else if (nights>=7) {
//            total=total.multiply(LONG_STAY_7_MULT);
//        }
//        return total.setScale(2, RoundingMode.HALF_UP);
//    }
    public BigDecimal computeTotalPrice(Room room,LocalDate checkIn,LocalDate checkOut,LocalDateTime createdAt){
        long nights= ChronoUnit.DAYS.between(checkIn,checkOut);
        if(nights<=0){
            throw new IllegalArgumentException("Le séjour doit durer au moins 1 nuit");
        }
        BigDecimal totalBrut=BigDecimal.ZERO;
        LocalDate current=checkIn;
        while (current.isBefore(checkOut)){
            int month=checkIn.getMonthValue();
            BigDecimal nightPrice=room.getPricePerNight();
            if(month==7||month==8){
                nightPrice=nightPrice.multiply(HIGH_SEASON_MULT);
            } else if (month==11||month==12||month==1||month==2) {
                nightPrice=nightPrice.multiply(LOW_SEASON_MULT);
            }
            DayOfWeek dow=current.getDayOfWeek();
            if(dow==DayOfWeek.FRIDAY||dow==DayOfWeek.SATURDAY){
                nightPrice=nightPrice.multiply(WEEKEND_MULT);
            }
            totalBrut=totalBrut.add(nightPrice);
            current=current.plusDays(1);
        }
        //2:Séjour calcul
        BigDecimal total=totalBrut;
        if(nights>=14){
            total=total.multiply(LONG_STAY_14_MULT);
        } else if (nights>=7) {
            total=total.multiply(LONG_STAY_7_MULT);
        }
        //3:Reservation delay
        long daysBeforeCheckIn=ChronoUnit.DAYS.between(createdAt.toLocalDate(),checkIn);
        if(daysBeforeCheckIn>=30){
            total=total.multiply(EARLY_BOOKING_MULT);
        } else if (daysBeforeCheckIn<=3) {
            total=totalBrut.multiply(LAST_MINUTE_MULT);
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }
}
