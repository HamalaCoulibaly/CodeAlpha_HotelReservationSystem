import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class HotelReservationSystem {
    private List<Room> rooms;
    private List<Reservation> reservations;
    private Scanner scanner;
    private DateTimeFormatter dateFormatter;
    
    public HotelReservationSystem() {
        rooms = new ArrayList<>();
        reservations = new ArrayList<>();
        scanner = new Scanner(System.in);
        dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        initializeRooms();
    }
    
    private void initializeRooms() {
        // Standard rooms
        for (int i = 101; i <= 110; i++) {
            rooms.add(new Room(i, "Standard", 80.0));
        }
        
        // Deluxe rooms
        for (int i = 201; i <= 210; i++) {
            rooms.add(new Room(i, "Deluxe", 120.0));
        }
        
        // Suite rooms
        for (int i = 301; i <= 305; i++) {
            rooms.add(new Room(i, "Suite", 200.0));
        }
    }
    
    public void run() {
        System.out.println("Welcome to CHC Hotel Reservation System");
        System.out.println("===================================================");
        
        while (true) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    searchRooms();
                    break;
                case 2:
                    makeReservation();
                    break;
                case 3:
                    viewReservations();
                    break;
                case 4:
                    cancelReservation();
                    break;
                case 5:
                    checkIn();
                    break;
                case 6:
                    checkOut();
                    break;
                case 7:
                    displayRoomStatus();
                    break;
                case 8:
                    saveData();
                    break;
                case 9:
                    System.out.println("\n Thank you for using CHC Hotel System!");
                    System.exit(0);
                    break;
                default:
                    System.out.println(" Invalid choice. Please try again.");
            }
        }
    }
    
    private void displayMenu() {
        System.out.println("\n========== Main Menu ==========");
        System.out.println("1.  Search Available Rooms");
        System.out.println("2.  Make Reservation");
        System.out.println("3.  View My Reservations");
        System.out.println("4.  Cancel Reservation");
        System.out.println("5.  Check In");
        System.out.println("6.  Check Out");
        System.out.println("7.  Room Status Overview");
        System.out.println("8.  Save Data");
        System.out.println("9.  Exit");
        System.out.println("===============================");
    }
    
    private void searchRooms() {
        System.out.println("\n=== Search Available Rooms ===");
        
        System.out.println("Room Categories:");
        System.out.println("1. All");
        System.out.println("2. Standard ($80/night)");
        System.out.println("3. Deluxe ($120/night)");
        System.out.println("4. Suite ($200/night)");
        
        int categoryChoice = getIntInput("Select category: ");
        String selectedCategory = "";
        
        switch (categoryChoice) {
            case 1: selectedCategory = ""; break;
            case 2: selectedCategory = "Standard"; break;
            case 3: selectedCategory = "Deluxe"; break;
            case 4: selectedCategory = "Suite"; break;
            default:
                System.out.println("Invalid category selected.");
                return;
        }
        
        System.out.println("\nAvailable Rooms:");
        System.out.println("=================");
        boolean found = false;
        
        for (Room room : rooms) {
            if (room.isAvailable() && 
                (selectedCategory.isEmpty() || room.getCategory().equals(selectedCategory))) {
                System.out.println(room);
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No available rooms found for the selected category.");
        }
    }
    
    private void makeReservation() {
        System.out.println("\n=== Make New Reservation ===");
        
        // get guest information
        System.out.println("Guest Information:");
        String name = getStringInput("Full Name: ");
        String email = getStringInput("Email: ");
        String phone = getStringInput("Phone: ");
        String address = getStringInput("Address: ");
        
        Guest guest = new Guest(name, email, phone, address);
        
        // show available rooms
        searchRooms();
        
        int roomNumber = getIntInput("\nEnter room number to book: ");
        Room selectedRoom = findRoom(roomNumber);
        
        if (selectedRoom == null) {
            System.out.println(" Room not found.");
            return;
        }
        
        if (!selectedRoom.isAvailable()) {
            System.out.println(" Room is not available.");
            return;
        }
        
        // get dates
        LocalDate checkIn = getDateInput("Check-in date (yyyy-MM-dd): ");
        LocalDate checkOut = getDateInput("Check-out date (yyyy-MM-dd): ");
        
        if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
            System.out.println(" Invalid dates. Check-out must be after check-in.");
            return;
        }
        
        // create reservation
        Reservation reservation = new Reservation(guest, selectedRoom, checkIn, checkOut);
        
        System.out.println("\n=== Reservation Summary ===");
        System.out.println(reservation);
        
        String confirm = getStringInput("\nConfirm reservation? (y/n): ");
        if (confirm.toLowerCase().startsWith("y")) {
            // Process payment
            System.out.println("\nPayment Methods:");
            System.out.println("1. Credit Card");
            System.out.println("2. Debit Card");
            System.out.println("3. Cash");
            
            int paymentChoice = getIntInput("Select payment method: ");
            String paymentMethod = "";
            
            switch (paymentChoice) {
                case 1: paymentMethod = "Credit Card"; break;
                case 2: paymentMethod = "Debit Card"; break;
                case 3: paymentMethod = "Cash"; break;
                default: paymentMethod = "Credit Card"; break;
            }
            
            if (PaymentProcessor.processPayment(reservation.getTotalAmount(), paymentMethod)) {
                selectedRoom.setAvailable(false);
                reservations.add(reservation);
                System.out.println("\n Reservation confirmed!");
                System.out.println("Reservation ID: " + reservation.getReservationId());
            } else {
                System.out.println("\n Reservation cancelled due to payment failure.");
            }
        } else {
            System.out.println("Reservation cancelled.");
        }
    }
    
    private void viewReservations() {
        System.out.println("\n=== View Reservations ===");
        
        String guestName = getStringInput("Enter guest name: ");
        boolean found = false;
        
        for (Reservation reservation : reservations) {
            if (reservation.getGuest().getName().equalsIgnoreCase(guestName)) {
                System.out.println(reservation);
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No reservations found for: " + guestName);
        }
    }
    
    private void cancelReservation() {
        System.out.println("\n=== Cancel Reservation ===");
        
        int reservationId = getIntInput("Enter reservation ID: ");
        Reservation reservation = findReservation(reservationId);
        
        if (reservation == null) {
            System.out.println(" Reservation not found.");
            return;
        }
        
        if (reservation.getStatus().equals("Cancelled")) {
            System.out.println(" Reservation is already cancelled.");
            return;
        }
        
        System.out.println("\nReservation Details:");
        System.out.println(reservation);
        
        String confirm = getStringInput("\nConfirm cancellation? (y/n): ");
        if (confirm.toLowerCase().startsWith("y")) {
            reservation.setStatus("Cancelled");
            reservation.getRoom().setAvailable(true);
            System.out.println(" Reservation cancelled successfully.");
            
            // simulate refund processing
            double refundAmount = reservation.getTotalAmount() * 0.9; // 10% cancellation fee
            System.out.printf("Refund of $%.2f will be processed to your original payment method.\n", 
                            refundAmount);
        } else {
            System.out.println("Cancellation aborted.");
        }
    }
    
    private void checkIn() {
        System.out.println("\n=== Check In ===");
        
        int reservationId = getIntInput("Enter reservation ID: ");
        Reservation reservation = findReservation(reservationId);
        
        if (reservation == null) {
            System.out.println(" Reservation not found.");
            return;
        }
        
        if (!reservation.getStatus().equals("Confirmed")) {
            System.out.println(" Cannot check in. Reservation status: " + reservation.getStatus());
            return;
        }
        
        LocalDate today = LocalDate.now();
        if (today.isBefore(reservation.getCheckInDate())) {
            System.out.println(" Check-in date has not arrived yet.");
            return;
        }
        
        reservation.setStatus("Checked-in");
        System.out.println(" Check-in successful!");
        System.out.println("Welcome " + reservation.getGuest().getName() + "!");
        System.out.println("Room: " + reservation.getRoom().getRoomNumber());
        System.out.println("Enjoy your stay! ");
    }
    
    private void checkOut() {
        System.out.println("\n=== Check Out ===");
        
        int reservationId = getIntInput("Enter reservation ID: ");
        Reservation reservation = findReservation(reservationId);
        
        if (reservation == null) {
            System.out.println(" Reservation not found.");
            return;
        }
        
        if (!reservation.getStatus().equals("Checked-in")) {
            System.out.println(" Cannot check out. Current status: " + reservation.getStatus());
            return;
        }
        
        reservation.setStatus("Checked-out");
        reservation.getRoom().setAvailable(true);
        
        System.out.println(" Check-out successful!");
        System.out.println("Thank you for staying with us, " + reservation.getGuest().getName() + "!");
        System.out.println("We hope you enjoyed your stay! ");
        
        // show final bill
        System.out.println("\n=== Final Bill ===");
        System.out.printf("Total Amount: $%.2f\n", reservation.getTotalAmount());
        System.out.println("Payment Status: Paid");
    }
    
    private void displayRoomStatus() {
        System.out.println("\n=== Room Status Overview ===");
        
        Map<String, Integer> available = new HashMap<>();
        Map<String, Integer> occupied = new HashMap<>();
        
        for (Room room : rooms) {
            String category = room.getCategory();
            if (room.isAvailable()) {
                available.put(category, available.getOrDefault(category, 0) + 1);
            } else {
                occupied.put(category, occupied.getOrDefault(category, 0) + 1);
            }
        }
        
        System.out.println("Category\t\tAvailable\tOccupied\tTotal");
        System.out.println("==================================================");
        
        for (String category : Arrays.asList("Standard", "Deluxe", "Suite")) {
            int avail = available.getOrDefault(category, 0);
            int occ = occupied.getOrDefault(category, 0);
            int total = avail + occ;
            System.out.printf("%-15s\t%d\t\t%d\t\t%d\n", category, avail, occ, total);
        }
        
        System.out.println("\nDetailed Room Status:");
        System.out.println("====================");
        for (Room room : rooms) {
            System.out.println(room);
        }
    }
    
    private void saveData() {
        System.out.println("\n=== Saving Data ===");
        FileManager.saveReservations(reservations);
        FileManager.saveRooms(rooms);
        System.out.println(" All data saved successfully!");
    }
    
    // utility methods
    private Room findRoom(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }
    
    private Reservation findReservation(int reservationId) {
        for (Reservation reservation : reservations) {
            if (reservation.getReservationId() == reservationId) {
                return reservation;
            }
        }
        return null;
    }
    
    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println(" Please enter a valid number.");
            }
        }
    }
    
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    private LocalDate getDateInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String dateStr = scanner.nextLine().trim();
                return LocalDate.parse(dateStr, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println(" Please enter date in yyyy-MM-dd format.");
            }
        }
    }
    
    // main method
    public static void main(String[] args) {
        HotelReservationSystem system = new HotelReservationSystem();
        system.run();
    }
}