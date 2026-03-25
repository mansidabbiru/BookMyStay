import java.io.*;
import java.util.*;

class Reservation implements Serializable {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

class InventoryService implements Serializable {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public void setInventory(Map<String, Integer> inventory) {
        this.inventory = inventory;
    }
}

class BookingHistory implements Serializable {
    private List<Reservation> reservations = new ArrayList<>();

    public void addReservation(Reservation r) {
        reservations.add(r);
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}

class PersistenceService {
    private static final String FILE_NAME = "system_state.ser";

    public void save(InventoryService inventory, BookingHistory history) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(inventory);
            oos.writeObject(history);
            System.out.println("System state saved.");
        } catch (IOException e) {
            System.out.println("Error saving system state.");
        }
    }

    public Object[] load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            InventoryService inventory = (InventoryService) ois.readObject();
            BookingHistory history = (BookingHistory) ois.readObject();
            System.out.println("System state restored.");
            return new Object[]{inventory, history};
        } catch (Exception e) {
            System.out.println("No valid saved state found. Starting fresh.");
            return null;
        }
    }
}

public class UseCase12DataPersistenceRecovery {
    public static void main(String[] args) {
        PersistenceService persistenceService = new PersistenceService();

        Object[] data = persistenceService.load();

        InventoryService inventory;
        BookingHistory history;

        if (data != null) {
            inventory = (InventoryService) data[0];
            history = (BookingHistory) data[1];
        } else {
            inventory = new InventoryService();
            history = new BookingHistory();

            inventory.addRoom("Single", 2);
            inventory.addRoom("Double", 1);

            history.addReservation(new Reservation("RES-1", "Alice", "Single"));
            history.addReservation(new Reservation("RES-2", "Bob", "Double"));
        }

        System.out.println("Current Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.getInventory().entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        System.out.println("\nBooking History:");
        for (Reservation r : history.getReservations()) {
            System.out.println(r.getReservationId() + " | " + r.getGuestName() + " | " + r.getRoomType());
        }

        persistenceService.save(inventory, history);
    }
}