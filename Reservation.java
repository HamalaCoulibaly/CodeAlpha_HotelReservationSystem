import java.time.LocalDate;

class Reservation {
    private static int nextId = 1000;
    private int reservationId;
    private Guest guest;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalAmount;
    private String status; // "Confirmed", "Cancelled", "Checked-in", "Checked-out"
    
    public Reservation(Guest guest, Room room, LocalDate checkIn, LocalDate checkOut) {
        this.reservationId = nextId++;
        this.guest = guest;
        this.room = room;
        this.checkInDate = checkIn;
        this.checkOutDate = checkOut;
        this.status = "Confirmed";
        calculateTotalAmount();
    }
    
    private void calculateTotalAmount() {
        long nights = java.time.temporal.ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        this.totalAmount = nights * room.getPricePerNight();
    }
    
    // getters
    public int getReservationId() { return reservationId; }
    public Guest getGuest() { return guest; }
    public Room getRoom() { return room; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    @Override
    public String toString() {
        return String.format("ID: %d | Guest: %s | Room: %d (%s) | %s to %s | $%.2f | Status: %s",
                reservationId, guest.getName(), room.getRoomNumber(), room.getCategory(),
                checkInDate, checkOutDate, totalAmount, status);
    }
}