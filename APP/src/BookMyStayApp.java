import java.util.*;

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    public void validateRoomType(String type) throws InvalidBookingException {
        if (!inventory.containsKey(type)) {
            throw new InvalidBookingException("Invalid room type: " + type);
        }
    }

    public void validateAvailability(String type) throws InvalidBookingException {
        int count = inventory.getOrDefault(type, 0);
        if (count <= 0) {
            throw new InvalidBookingException("No availability for room type: " + type);
        }
    }

    public void decrement(String type) throws InvalidBookingException {
        int count = inventory.getOrDefault(type, 0);
        if (count <= 0) {
            throw new InvalidBookingException("Cannot decrement. Invalid inventory for: " + type);
        }
        inventory.put(type, count - 1);
    }
}

class BookingService {
    private InventoryService inventoryService;

    public BookingService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public void processBooking(Reservation reservation) {
        try {
            inventoryService.validateRoomType(reservation.getRoomType());
            inventoryService.validateAvailability(reservation.getRoomType());
            inventoryService.decrement(reservation.getRoomType());
            System.out.println("Booking confirmed for " + reservation.getGuestName() + " (" + reservation.getRoomType() + ")");
        } catch (InvalidBookingException e) {
            System.out.println("Booking failed for " + reservation.getGuestName() + ": " + e.getMessage());
        }
    }
}

public class UseCase9ErrorHandlingValidation {
    public static void main(String[] args) {
        InventoryService inventory = new InventoryService();
        inventory.addRoom("Single", 1);
        inventory.addRoom("Double", 0);

        BookingService bookingService = new BookingService(inventory);

        Reservation r1 = new Reservation("Alice", "Single");
        Reservation r2 = new Reservation("Bob", "Double");
        Reservation r3 = new Reservation("Charlie", "Suite");

        bookingService.processBooking(r1);
        bookingService.processBooking(r2);
        bookingService.processBooking(r3);
    }
}