class Room {
    private int roomNumber;
    private String category;
    private double pricePerNight;
    private boolean isAvailable;
    
    public Room(int roomNumber, String category, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.pricePerNight = pricePerNight;
        this.isAvailable = true;
    }
    
    
    public int getRoomNumber() { return roomNumber; }
    public String getCategory() { return category; }
    public double getPricePerNight() { return pricePerNight; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { this.isAvailable = available; }
    
    @Override
    public String toString() {
        return String.format("Room %d - %s ($%.2f/night) - %s",
                roomNumber, category, pricePerNight, 
                isAvailable ? "Available" : "Occupied");
    }
}