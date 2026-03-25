import java.util.*;

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

    public boolean isAvailable(String type) {
        return inventory.getOrDefault(type, 0) > 0;
    }

    public void decrement(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }
}

class BookingService {
    private Queue<Reservation> queue;
    private InventoryService inventoryService;
    private Set<String> allocatedRoomIds;
    private Map<String, Set<String>> roomAllocations;
    private int idCounter = 1;

    public BookingService(Queue<Reservation> queue, InventoryService inventoryService) {
        this.queue = queue;
        this.inventoryService = inventoryService;
        this.allocatedRoomIds = new HashSet<>();
        this.roomAllocations = new HashMap<>();
    }

    public void processBookings() {
        while (!queue.isEmpty()) {
            Reservation reservation = queue.poll();
            String type = reservation.getRoomType();

            if (inventoryService.isAvailable(type)) {
                String roomId = generateRoomId(type);
                allocatedRoomIds.add(roomId);

                roomAllocations.putIfAbsent(type, new HashSet<>());
                roomAllocations.get(type).add(roomId);

                inventoryService.decrement(type);

                System.out.println("Confirmed: " + reservation.getGuestName() + " -> " + roomId);
            } else {
                System.out.println("Failed: " + reservation.getGuestName() + " (No availability)");
            }
        }
    }

    private String generateRoomId(String type) {
        String roomId;
        do {
            roomId = type + "-" + idCounter++;
        } while (allocatedRoomIds.contains(roomId));
        return roomId;
    }
}

public class UseCase6RoomAllocationService {
    public static void main(String[] args) {
        InventoryService inventory = new InventoryService();
        inventory.addRoom("Single", 2);
        inventory.addRoom("Double", 1);
        inventory.addRoom("Suite", 1);

        Queue<Reservation> queue = new LinkedList<>();
        queue.offer(new Reservation("Alice", "Single"));
        queue.offer(new Reservation("Bob", "Single"));
        queue.offer(new Reservation("Charlie", "Single"));
        queue.offer(new Reservation("David", "Suite"));

        BookingService bookingService = new BookingService(queue, inventory);
        bookingService.processBookings();
    }
}