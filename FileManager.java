import java.io.*;
import java.util.*;

class FileManager {
    private static final String RESERVATIONS_FILE = "reservations.txt";
    private static final String ROOMS_FILE = "rooms.txt";
    
    public static void saveReservations(List<Reservation> reservations) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RESERVATIONS_FILE))) {
            for (Reservation reservation : reservations) {
                writer.println(reservationToString(reservation));
            }
            System.out.println("Reservations saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving reservations: " + e.getMessage());
        }
    }
    
    public static void saveRooms(List<Room> rooms) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ROOMS_FILE))) {
            for (Room room : rooms) {
                writer.printf("%d,%s,%.2f,%b\n",
                    room.getRoomNumber(), room.getCategory(), 
                    room.getPricePerNight(), room.isAvailable());
            }
            System.out.println("Room data saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving rooms: " + e.getMessage());
        }
    }
    
    private static String reservationToString(Reservation res) {
        return String.format("%d|%s|%s|%s|%s|%d|%s|%.2f|%s|%s|%s",
            res.getReservationId(),
            res.getGuest().getName(),
            res.getGuest().getEmail(),
            res.getGuest().getPhone(),
            res.getGuest().getAddress(),
            res.getRoom().getRoomNumber(),
            res.getRoom().getCategory(),
            res.getRoom().getPricePerNight(),
            res.getCheckInDate(),
            res.getCheckOutDate(),
            res.getStatus());
    }
}
