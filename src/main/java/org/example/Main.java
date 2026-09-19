import exception.EmailAlreadyExistsException;
import exception.InvalidCredentialsException;
import exception.InvalidReservationDateException;
import exception.RoomNotFoundException;
import initializer.RoomInitializer;
import model.*;
import repository.impl.InMemoryReservationRepository;
import repository.impl.InMemoryRoomRepository;
import repository.jdbc.JdbcUserRepository;
import service.AuthService;
import service.ReservationService;
import service.RoomService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

void main() throws Exception {
    Scanner scanner = new Scanner(System.in);

    // ---- Repositories ----
    JdbcUserRepository jdbcUserRepository = new JdbcUserRepository();
    InMemoryRoomRepository roomRepository = new InMemoryRoomRepository();
    InMemoryReservationRepository reservationRepository = new InMemoryReservationRepository();

    // ---- Init ----
    RoomInitializer.init(roomRepository);

    // ---- Services ----
    AuthService authService = new AuthService(jdbcUserRepository);
    RoomService roomService = new RoomService(roomRepository);
    ReservationService reservationService = new ReservationService(reservationRepository, roomRepository);

    boolean running = true;
    while (running) {
        System.out.println("\n---- Menu principal ----");
        System.out.println("1: Registre");
        System.out.println("2: Login");
        System.out.println("3: Quitter");
        System.out.print("Entrez votre choix : ");

        int choix = scanner.nextInt();
        scanner.nextLine();

        switch (choix) {
            case 1 -> register(scanner, authService);
            case 2 -> login(scanner, authService, roomService, reservationService);
            case 3 -> {
                running = false;
                System.out.println("Bye !");
            }
            default -> System.out.println("Choix invalide");
        }
    }
}

// =========================================================
//                     REGISTRATION
// =========================================================
void register(Scanner scanner, AuthService authService) {
    System.out.println("--- Registre ---");
    System.out.print("Prénom : ");
    String firstName = scanner.nextLine();
    System.out.print("Nom : ");
    String lastName = scanner.nextLine();
    System.out.print("Email : ");
    String email = scanner.nextLine();
    System.out.print("Numéro : ");
    String numero = scanner.nextLine();
    System.out.print("Password : ");
    String password = scanner.nextLine();

    try {
        boolean registred = authService.inscription(
                UUID.randomUUID(), firstName, lastName, email, numero, password);
        if (registred) {
            System.out.println("Inscription réussie");
        } else {
            System.out.println("Vous êtes déjà inscrit");
        }
    } catch (EmailAlreadyExistsException | InvalidCredentialsException e) {
        System.out.println(e.getMessage());
    }
}

// =========================================================
//                        LOGIN
// =========================================================
void login(Scanner scanner,
           AuthService authService,
           RoomService roomService,
           ReservationService reservationService) {
    System.out.print("Email : ");
    String email = scanner.nextLine();
    System.out.print("Password : ");
    String password = scanner.nextLine();

    User currentUser = authService.connexion(email, password);
    if (currentUser == null) {
        System.out.println("Email ou mot de passe incorrect");
        return;
    }
    System.out.println("Connexion réussie !");

    if (currentUser.getRole() == Role.ADMIN) {
        adminMenu(scanner, authService, roomService);
    } else {
        userMenu(scanner, authService, roomService, reservationService, currentUser);
    }
}

// =========================================================
//                     MENU ADMIN
// =========================================================
void adminMenu(Scanner scanner,
               AuthService authService,
               RoomService roomService) {
    boolean running = true;
    while (running) {
        System.out.println("\n===== MENU ADMIN =====");
        System.out.println("1. Voir tous les utilisateurs");
        System.out.println("2. Ajouter une chambre");
        System.out.println("3. Modifier une chambre");
        System.out.println("4. Supprimer une chambre");
        System.out.println("5. Voir toutes les chambres");
        System.out.println("6. Logout");
        System.out.print("Choix : ");

        int choix = scanner.nextInt();
        scanner.nextLine();

        switch (choix) {
            case 1 -> {
                List<User> users = authService.getAllUsers();
                System.out.println("--- Utilisateurs ---");
                for (User u : users) {
                    System.out.println("ID        : " + u.getId());
                    System.out.println("Nom       : " + u.getFullName());
                    System.out.println("Email     : " + u.getEmail());
                    System.out.println("Téléphone : " + u.getPhone());
                    System.out.println("Rôle      : " + u.getRole());
                    System.out.println("----------------------");
                }
            }

            case 2 -> {
                System.out.println("--- Ajouter une chambre ---");
//                try {
//                    System.out.print("Numéro : ");
//                    String num = scanner.nextLine();
//                    System.out.print("Type (SIMPLE / DOUBLE / SUITE) : ");
//                    RoomType type = RoomType.valueOf(scanner.nextLine().toUpperCase());
//                    System.out.print("Capacité : ");
//                    int capacity = scanner.nextInt();
//                    scanner.nextLine();
//                    System.out.print("Prix par nuit : ");
//                    BigDecimal price = new BigDecimal(scanner.nextLine());
//                    System.out.print("Statut (AVAILABLE / OCCUPIED) : ");
//                    RoomStatus status = RoomStatus.valueOf(scanner.nextLine().toUpperCase());
//
//                    Room room = new Room();
//                    room.setId(UUID.randomUUID());
//                    room.setRoomNumber(num);
//                    room.setType(type);
//                    room.setCapacity(capacity);
//                    room.setPricePerNight(price);
//                    room.setStatus(status);
//
//                    roomService.save(room);
//                    System.out.println("Chambre ajoutée !");
//                } catch (Exception e) {
//                    System.out.println("Erreur : " + e.getMessage());
//                }
            }

            case 3 -> {
                System.out.print("Numéro de la chambre à modifier : ");
                String num = scanner.nextLine();
//                try {
//                    Room room = roomService.getRoomByNumber(num);
//
//                    System.out.print("Nouveau type (" + room.getType() + ") : ");
//                    String type = scanner.nextLine();
//                    if (!type.isBlank()) room.setType(RoomType.valueOf(type.toUpperCase()));
//
//                    System.out.print("Nouvelle capacité (" + room.getCapacity() + ") : ");
//                    String cap = scanner.nextLine();
//                    if (!cap.isBlank()) room.setCapacity(Integer.parseInt(cap));
//
//                    System.out.print("Nouveau prix (" + room.getPricePerNight() + ") : ");
//                    String price = scanner.nextLine();
//                    if (!price.isBlank()) room.setPricePerNight(new BigDecimal(price));
//
//                    System.out.print("Nouveau statut (" + room.getStatus() + ") : ");
//                    String status = scanner.nextLine();
//                    if (!status.isBlank()) room.setStatus(RoomStatus.valueOf(status.toUpperCase()));
//
//                    roomService.update(room);
//                    System.out.println("Chambre modifiée !");
//                } catch (RoomNotFoundException e) {
//                    System.out.println(e.getMessage());
//                } catch (Exception e) {
//                    System.out.println("Erreur : " + e.getMessage());
//                }
            }

            case 4 -> {
                System.out.print("Numéro de la chambre à supprimer : ");
                String num = scanner.nextLine();
//                if (roomService.delete(num)) {
//                    System.out.println("Chambre supprimée !");
//                } else {
//                    System.out.println("Chambre introuvable !");
//                }
            }

            case 5 -> {
                List<Room> rooms = roomService.getAllRooms();
                System.out.println("--- Toutes les chambres ---");
                for (Room r : rooms) {
                    System.out.println("Numéro : " + r.getRoomNumber()
                            + " | Type : " + r.getType()
                            + " | Capacité : " + r.getCapacity()
                            + " | Prix : " + r.getPricePerNight()
                            + " | Statut : " + r.getStatus());
                }
            }

            case 6 -> {
                running = false;
                System.out.println("Logout admin.");
            }

            default -> System.out.println("Choix invalide");
        }
    }
}

// =========================================================
//                     MENU USER
// =========================================================
void userMenu(Scanner scanner,
              AuthService authService,
              RoomService roomService,
              ReservationService reservationService,
              User currentUser) {
    boolean loggedIn = true;
    while (loggedIn) {
        System.out.println("\n===== MENU UTILISATEUR =====");
        System.out.println("1. Mon profil");
        System.out.println("2. Modifier mon profil");
        System.out.println("3. Logout");
        System.out.println("4. Voir toutes les chambres");
        System.out.println("5. Chercher par roomNumber");
        System.out.println("6. Créer une réservation");
        System.out.println("7. Voir les chambres disponibles");
        System.out.println("8. Mes réservations");
        System.out.println("9. Modifier une réservation");
        System.out.println("10. Annuler une réservation");
        System.out.println("11. Filtrer les chambres par prix");
        System.out.print("Choix : ");

        int choixUser = scanner.nextInt();
        scanner.nextLine();

        switch (choixUser) {
            case 1 -> {
                System.out.println("\n===== MON PROFIL =====");
                System.out.println("Nom : " + currentUser.getFullName());
                System.out.println("Email : " + currentUser.getEmail());
                System.out.println("Téléphone : " + currentUser.getPhone());
            }

            case 2 -> {
                System.out.println("--- Edit profile ---");
                System.out.print("Nouveau prénom : ");
                String prenom = scanner.nextLine();
                System.out.print("Nouveau nom : ");
                String nom = scanner.nextLine();
                System.out.print("Nouveau email : ");
                String newEmail = scanner.nextLine();
                System.out.print("Nouveau phone : ");
                String newPhone = scanner.nextLine();
                System.out.print("Nouveau password : ");
                String newPassword = scanner.nextLine();

                boolean updated = authService.editProfile(
                        currentUser.getId(), prenom, nom, newEmail, newPhone, newPassword);

                if (updated) {
                    System.out.println("Profil modifié !");
                    currentUser = authService.finById(currentUser.getId());
                } else {
                    System.out.println("Utilisateur introuvable !");
                }
            }

            case 3 -> {
                loggedIn = false;
                System.out.println("Logout réussi !");
            }

            case 4 -> {
                List<Room> rooms = roomService.getAllRooms();
                System.out.println("--- Toutes les chambres ---");
                for (Room room : rooms) {
                    System.out.println("Numéro : " + room.getRoomNumber());
                    System.out.println("Type : " + room.getType());
                    System.out.println("Capacité : " + room.getCapacity());
                    System.out.println("Prix/nuit : " + room.getPricePerNight());
                    System.out.println("Statut : " + room.getStatus());
                    System.out.println("----------------------");
                }
            }

            case 5 -> {
                System.out.print("Entrez le numéro de chambre : ");
                String number = scanner.nextLine();
                try {
                    Room room = roomService.getRoomByNumber(number);
                    System.out.println("ID : " + room.getId());
                    System.out.println("Numéro : " + room.getRoomNumber());
                    System.out.println("Type : " + room.getType());
                    System.out.println("Capacité : " + room.getCapacity());
                    System.out.println("Prix : " + room.getPricePerNight());
                    System.out.println("Statut : " + room.getStatus());
                } catch (RoomNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }

            case 6 -> {
                System.out.println("=== Créer une réservation ===");
                System.out.print("Code de réservation : ");
                String reservationCode = scanner.nextLine();
                System.out.print("Numéro de chambre : ");
                String roomNumber = scanner.nextLine();

                try {
                    Room room = roomService.getRoomByNumber(roomNumber);

                    System.out.print("Date d'entrée (yyyy-MM-dd) : ");
                    LocalDate checkIn = LocalDate.parse(scanner.nextLine());
                    System.out.print("Date de sortie (yyyy-MM-dd) : ");
                    LocalDate checkOut = LocalDate.parse(scanner.nextLine());
                    System.out.print("Nombre de personnes : ");
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

                    reservationService.save(reservation);
                    System.out.println("Réservation créée !");
                } catch (RoomNotFoundException | InvalidReservationDateException e) {
                    System.out.println(e.getMessage());
                }
            }

            case 7 -> roomService.availableRooms();

            case 8 -> {
                List<Reservation> mesReservations =
                        reservationService.getAllReservationByUser(currentUser.getId());
                System.out.println("--- Mes réservations ---");
                if (mesReservations.isEmpty()) {
                    System.out.println("Vous n'avez aucune réservation.");
                } else {
                    for (Reservation r : mesReservations) {
                        System.out.println("Code : " + r.getReservationCode());
                        System.out.println("Chambre : " + r.getRoomNumber());
                        System.out.println("Check-in : " + r.getCheckIn());
                        System.out.println("Check-out : " + r.getCheckOut());
                        System.out.println("Personnes : " + r.getNumberOfGuests());
                        System.out.println("Prix total : " + r.getTotalPrice());
                        System.out.println("-------------------------");
                    }
                }
            }

            case 9 -> {
                System.out.println("--- Modifier une réservation ---");
                System.out.print("Code de réservation : ");
                String code = scanner.nextLine();
                System.out.print("Nouveau numéro de chambre : ");
                String rNumber = scanner.nextLine();

                try {
                    Room newRoom = roomService.getRoomByNumber(rNumber);

                    System.out.print("Nouvelle date d'entrée (yyyy-MM-dd) : ");
                    LocalDate newCheckIn = LocalDate.parse(scanner.nextLine());
                    System.out.print("Nouvelle date de sortie (yyyy-MM-dd) : ");
                    LocalDate newCheckOut = LocalDate.parse(scanner.nextLine());
                    System.out.print("Nouveau nombre de personnes : ");
                    int newNumberOfGuests = scanner.nextInt();
                    scanner.nextLine();

                    long newNumberOfNights = java.time.temporal.ChronoUnit.DAYS
                            .between(newCheckIn, newCheckOut);
                    BigDecimal newTotalPrice = newRoom.getPricePerNight()
                            .multiply(BigDecimal.valueOf(newNumberOfNights));

                    reservationService.update(code, rNumber, newCheckIn, newCheckOut,
                            newNumberOfGuests, newNumberOfNights, newTotalPrice);

                    System.out.println("Réservation modifiée !");
                } catch (RoomNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }

            case 10 -> {
                List<Reservation> allReservations =
                        reservationService.getAllReservationByUser(currentUser.getId());

                if (allReservations.isEmpty()) {
                    System.out.println("Vous n'avez aucune réservation.");
                } else {
                    for (Reservation r : allReservations) {
                        System.out.println("Code : " + r.getReservationCode()
                                + " | Chambre : " + r.getRoomNumber()
                                + " | Du " + r.getCheckIn()
                                + " au " + r.getCheckOut());
                    }
                    System.out.print("Code de la réservation à annuler : ");
                    String codeRes = scanner.nextLine();
                    reservationService.delete(codeRes);
                    System.out.println("Réservation annulée !");
                }
            }

            case 11 -> {
                System.out.print("Prix maximum : ");
                BigDecimal maxPrice = new BigDecimal(scanner.nextLine());
                List<Room> chambresFiltrer = roomService.filtrerParPrice(maxPrice);
                if (!chambresFiltrer.isEmpty()) {
                    for (Room r : chambresFiltrer) {
                        System.out.println(r.getRoomNumber() + " - "
                                + r.getPricePerNight() + " DH");
                    }
                } else {
                    System.out.println("Aucune chambre");
                }
            }

            default -> System.out.println("Choix invalide");
        }
    }
}