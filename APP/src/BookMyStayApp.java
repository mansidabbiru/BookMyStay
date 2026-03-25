import java.util.*;

class Reservation {
    private String reservationId;
    private String roomType;
    private String roomId;

    public Reservation(String reservationId, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }
}

class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    public void increment(String type) {
        inventory.put(type, inventory.getOrDefault(type, 0) + 1);
    }

    public int getCount(String type) {
        return inventory.getOrDefault(type, 0);
    }
}

class BookingHistory {
    private Map<String, Reservation> confirmedBookings = new HashMap<>();

    public void addReservation(Reservation reservation) {
        confirmedBookings.put(reservation.getReservationId(), reservation);
    }

    public Reservation getReservation(String id) {
        return confirmedBookings.get(id);
    }

    public void removeReservation(String id) {
        confirmedBookings.remove(id);
    }
}

class CancellationService {
    private InventoryService inventoryService;
    private BookingHistory bookingHistory;
    private Stack<String> rollbackStack;

    public CancellationService(InventoryService inventoryService, BookingHistory bookingHistory) {
        this.inventoryService = inventoryService;
        this.bookingHistory = bookingHistory;
        this.rollbackStack = new Stack<>();
    }

    public void cancelBooking(String reservationId) {
        Reservation reservation = bookingHistory.getReservation(reservationId);

        if (reservation == null) {
            System.out.println("Cancellation failed: Invalid reservation ID " + reservationId);
            return;
        }

        rollbackStack.push(reservation.getRoomId());
        inventoryService.increment(reservation.getRoomType());
        bookingHistory.removeReservation(reservationId);

        System.out.println("Cancelled reservation " + reservationId + ", Room released: " + rollbackStack.pop());
    }
}

public class UseCase10BookingCancellation {
    public static void main(String[] args) {
        InventoryService inventory = new InventoryService();
        inventory.addRoom("Single", 0);
        inventory.addRoom("Double", 1);

        BookingHistory history = new BookingHistory();
        history.addReservation(new Reservation("RES-1", "Single", "Single-101"));
        history.addReservation(new Reservation("RES-2", "Double", "Double-201"));

        CancellationService cancellationService = new CancellationService(inventory, history);

        cancellationService.cancelBooking("RES-1");
        cancellationService.cancelBooking("RES-3");
    }
}