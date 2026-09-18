import exception.EmailAlreadyExistsException;
import exception.InvalidCredentialsException;
import exception.InvalidReservationDateException;
import exception.RoomNotFoundException;
import initializer.RoomInitializer;
import model.Reservation;
import model.ReservationStatus;
import model.Room;
import model.User;
import repository.UserRepository;
import repository.impl.InMemoryReservationRepository;
import repository.impl.InMemoryRoomRepository;
import repository.impl.InMemoryUserRepository;
import repository.jdbc.JdbcUserRepository;
import service.AuthService;
import service.ReservationService;
import service.RoomService;

import java.util.*;//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
void main()throws Exception {

    Scanner scanner=new Scanner(System.in);
    //User
//    InMemoryUserRepository userRepository=new InMemoryUserRepository();
    JdbcUserRepository jdbcUserRepository=new JdbcUserRepository();
    AuthService authService=new AuthService(jdbcUserRepository);
    //Room
    InMemoryRoomRepository roomRepository=new InMemoryRoomRepository();
    RoomInitializer.init(roomRepository);
    RoomService roomService=new RoomService(roomRepository);
    //Reservation
    InMemoryReservationRepository reservationRepository=new InMemoryReservationRepository();
    ReservationService reservationService=new ReservationService(reservationRepository,roomRepository);
    Boolean running=true;
    while (running==true){
        System.out.println("----Menu----");
        System.out.println("1:Registre");
        System.out.println("2:Login");
        /*
        System.out.println("3:Edit profile");
        System.out.println("4:voir tous les utilisateurs");*/
        System.out.println("Entrez votre choix : ");
        Integer choix=scanner.nextInt();
        scanner.nextLine();
        switch (choix){
            case 1:
                System.out.println("---Registre---");
                System.out.println("Entrez votre prénom : ");
                String firstName = scanner.nextLine();
                System.out.println("Entrez votre nom : ");
                String lastName = scanner.nextLine();
                System.out.println("Entrez votre email : ");
                String email=scanner.nextLine();
                System.out.println("Entrez votre numero : ");
                String numero=scanner.nextLine();
                System.out.println("Entrez votre password : ");
                String password=scanner.nextLine();
                try {
                    boolean registred=authService.inscription(UUID.randomUUID(),firstName,lastName,email,numero,password);
                    if(registred){
                        System.out.println("Inscription réussi");
                    }else {
                        System.out.println("Vous étes déja inscrits");
                    }
                } catch (EmailAlreadyExistsException e) {
                    System.out.println(e.getMessage());
                }catch (InvalidCredentialsException e){
                    System.out.println(e.getMessage());
                }

                break;

            case 2:
                System.out.println("Email : ");
                String emailLogin=scanner.nextLine();
                System.out.println("Password : ");
                String passwordLogin=scanner.nextLine();
                boolean connected=authService.connexion(emailLogin,passwordLogin);
                if(connected){
                    System.out.println("connexion réussi avec succes");
                    User currentUser=jdbcUserRepository.findByEmail(emailLogin).get();
                    boolean loggedIn=true;
                    while (loggedIn){
                        System.out.println("\n===== MENU UTILISATEUR =====");
                        System.out.println("1. Mon profil");
                        System.out.println("2. Modifier mon profil");
                        System.out.println("3. Logout");
                        System.out.println("4. Voir tous les chambres");
                        System.out.println("5. Chercher par roomNumber");
                        System.out.println("6. créer une reservation");
                        System.out.println("7. Voir les chambres disponnible");
                        System.out.println("8. Mes reservations");
                        System.out.println("9. Modifier une reservation ");
                        System.out.println("10. Annuler une reservation");
                        System.out.println("11 .Filtrer les chambres par prix");
                        int choixUser = scanner.nextInt();
                        scanner.nextLine();
                        switch (choixUser) {
                            case 1:
                                System.out.println("\n===== MON PROFIL =====");
                                System.out.println("Nom : " + currentUser.getFullName());
                                System.out.println("Email : " + currentUser.getEmail());
                                System.out.println("Téléphone : " + currentUser.getPhone());
                                break;
                            case 2:
                                System.out.println("---Edit profile---");
                                System.out.println("ID : ");
                                UUID id=UUID.fromString(scanner.nextLine());
                                System.out.println("Nouveau prenom : ");
                                String prenom=scanner.nextLine();
                                System.out.println("Nouveau nom : ");
                                String nom=scanner.nextLine();
                                System.out.println("Nouveau email : ");
                                String newEmail=scanner.nextLine();
                                System.out.println("Nouveau phone : ");
                                String newPhone=scanner.nextLine();
                                System.out.println("Nouveau password : ");
                                String newPassword=scanner.nextLine();
                                boolean updated=authService.editProfile(id,prenom,nom,newEmail,newPhone,newPassword);
                                if(updated){
                                    System.out.println("Profil modifié avec succès !");
                                } else {
                                    System.out.println("Utilisateur introuvable !");
                                }
                                break;
                            case 3:
                                loggedIn = false;
                                System.out.println("Logout réussi !");
                                break;
                            case 4:
                                List<Room> rooms=roomService.getAllRooms();
                                System.out.println("---Tous les chambres---");
                                for(Room room:rooms){
                                    System.out.println("Numéro : " + room.getRoomNumber());
                                    System.out.println("Type : " + room.getType());
                                    System.out.println("Capacité : " + room.getCapacity());
                                    System.out.println("Prix/nuit : " + room.getPricePerNight());
                                    System.out.println("Statut : " + room.getStatus());
                                    System.out.println("----------------------");
                                }
                            case 5:
                                System.out.println("Entrez le numero de chambre : ");
                                String number= scanner.nextLine();
                                try {
                                    Room room=roomService.getRoomByNumber(number);
                                    System.out.println("ID : " + room.getId());
                                    System.out.println("Numéro : " + room.getRoomNumber());
                                    System.out.println("Type : " + room.getType());
                                    System.out.println("Capacité : " + room.getCapacity());
                                    System.out.println("Prix : " + room.getPricePerNight());
                                    System.out.println("Status : " + room.getStatus());
                                } catch (RoomNotFoundException e) {
                                    System.out.println(e.getMessage());
                                }
                                break;
                            case 6:
                                System.out.println("=== Créer une réservation ===");
                                System.out.println("Entrez le code de réservation : ");
                                String reservationCode = scanner.nextLine();
                                System.out.println("Entrez le numéro de chambre : ");
                                String roomNumber = scanner.nextLine();
                                Room room=roomService.getRoomByNumber(roomNumber);
                                System.out.println("Entrez la date d'entrée (yyyy-MM-dd) : ");
                                LocalDate checkIn = LocalDate.parse(scanner.nextLine());
                                System.out.println("Entrez la date de sortie (yyyy-MM-dd) : ");
                                LocalDate checkOut = LocalDate.parse(scanner.nextLine());
                                System.out.println("Nombre de personnes : ");
                                int numberOfGuests = scanner.nextInt();
                                scanner.nextLine();
                                long numberOfNights = java.time.temporal.ChronoUnit.DAYS
                                        .between(checkIn, checkOut);
                                BigDecimal totalPrice = room.getPricePerNight()
                                        .multiply(BigDecimal.valueOf(numberOfNights));

                                Reservation reservation = new Reservation(
                                        reservationCode,
                                        currentUser.getId(),
                                        roomNumber,
                                        checkIn,
                                        checkOut,
                                        numberOfGuests,
                                        numberOfNights,
                                        totalPrice,
                                        ReservationStatus.CONFIRMED,
                                        LocalDateTime.now()
                                );
                                try {
                                    reservationService.save(reservation);
                                    System.out.println("Réservation créée avec succès !");
                                }catch (InvalidReservationDateException e){
                                    System.out.println(e.getMessage());
                                }

                                break;
                            case 7:
                                roomService.availableRooms();
                                break;

                            case 8:
                                List<Reservation> mesReservations=reservationService.getAllReservationByUser(currentUser.getId());
                                System.out.println("--- Mes réservations ---");

                                if (mesReservations.isEmpty()) {
                                    System.out.println("Vous n'avez aucune réservation.");
                                } else {
                                    for (Reservation maReservation : mesReservations) {
                                        System.out.println("Code : " + maReservation.getReservationCode());
                                        System.out.println("Chambre : " + maReservation.getRoomNumber());
                                        System.out.println("Check-in : " + maReservation.getCheckIn());
                                        System.out.println("Check-out : " + maReservation.getCheckOut());
                                        System.out.println("Nombre de personnes : " + maReservation.getNumberOfGuests());
                                        System.out.println("Prix total : " + maReservation.getTotalPrice());
                                        System.out.println("-------------------------");
                                    }
                                }
                                break;
                            case 9:
                                System.out.println("---Modifier une reservation---");
                                System.out.println("Entrez le code du reservation : ");
                                String ReservationCode=scanner.nextLine();
                                System.out.println("Entrez le numero du chambre : ");
                                String rNumber=scanner.nextLine();
                                Room newRoom=roomService.getRoomByNumber(rNumber);
                                System.out.println("Entrez le nouveau date d'entree : ");
                                LocalDate newCheckIn= LocalDate.parse(scanner.nextLine());
                                System.out.println("Entrez le nouveau date d'entree : ");
                                LocalDate newCheckOut= LocalDate.parse(scanner.nextLine());
                                System.out.println("Entrez le nouveau nombre des personnes : : ");
                                int newNumberOfGuests=scanner.nextInt();
                                long newNumberOfNights = java.time.temporal.ChronoUnit.DAYS
                                        .between(newCheckIn,newCheckOut);
                                BigDecimal newTotalPrice = newRoom.getPricePerNight()
                                        .multiply(BigDecimal.valueOf(newNumberOfNights));
                                reservationService.update(ReservationCode,rNumber,newCheckIn,newCheckOut,newNumberOfGuests,newNumberOfNights,newTotalPrice);
                                break;

                            case 10:
                                List<Reservation> AllReservations=reservationService.getAllReservationByUser(currentUser.getId());
                                System.out.println("--- Mes réservations ---");

                                if (AllReservations.isEmpty()) {
                                    System.out.println("Vous n'avez aucune réservation.");
                                } else {
                                    for (Reservation maReservation : AllReservations) {
                                        System.out.println("Code : " + maReservation.getReservationCode());
                                        System.out.println("Chambre : " + maReservation.getRoomNumber());
                                        System.out.println("Check-in : " + maReservation.getCheckIn());
                                        System.out.println("Check-out : " + maReservation.getCheckOut());
                                        System.out.println("Nombre de personnes : " + maReservation.getNumberOfGuests());
                                        System.out.println("Prix total : " + maReservation.getTotalPrice());
                                        System.out.println("-------------------------");
                                        System.out.println("Entrez le code du reservation annuller : ");
                                        String codeRes=scanner.nextLine();
                                        reservationService.delete(codeRes);
                                    }
                                }
                                break;

                            case 11:
                                System.out.println("Entrez le prix maximum : ");
                                BigDecimal maxPrice = new BigDecimal(scanner.nextLine());
                                List<Room> chambresFiltrer=roomService.filtrerParPrice(maxPrice);
                                if(chambresFiltrer.size()!=0){
                                    for (Room r:chambresFiltrer){
                                        System.out.println(
                                                r.getRoomNumber() + " - "
                                                        + r.getPricePerNight() + " DH"
                                        );
                                    }
                                }else {
                                    System.out.println("Aucune chambre");
                                }

                                break;
                            default:
                                System.out.println("Choix invalide");
                        }
                    }
                }else {
                    System.out.println("vous etes pas inscrit");
                }
                break;
            case 4:
                List<User> allUsers=authService.getAllUsers();
                System.out.println("---all users---");
                for (User user:allUsers){
                    System.out.println("ID : " + user.getId());
                    System.out.println("FullName : " + user.getFullName());
                    System.out.println("Email : " + user.getEmail());
                    System.out.println("Phone : " + user.getPhone());
                    System.out.println("Password : " + user.getPassword());
                    System.out.println("----------------");
                }
                break;
        }
    }
}

