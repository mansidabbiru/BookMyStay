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

    public synchronized void addRoom(String type, int count) {
        inventory.put(type, count);
    }

    public synchronized boolean allocateRoom(String type) {
        int count = inventory.getOrDefault(type, 0);
        if (count > 0) {
            inventory.put(type, count - 1);
            return true;
        }
        return false;
    }

    public synchronized int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }
}

class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public synchronized void addReservation(Reservation r) {
        queue.offer(r);
    }

    public synchronized Reservation getNextReservation() {
        return queue.poll();
    }
}

class BookingProcessor implements Runnable {
    private BookingQueue bookingQueue;
    private InventoryService inventoryService;

    public BookingProcessor(BookingQueue bookingQueue, InventoryService inventoryService) {
        this.bookingQueue = bookingQueue;
        this.inventoryService = inventoryService;
    }

    public void run() {
        while (true) {
            Reservation reservation;
            synchronized (bookingQueue) {
                reservation = bookingQueue.getNextReservation();
            }

            if (reservation == null) {
                break;
            }

            synchronized (inventoryService) {
                boolean success = inventoryService.allocateRoom(reservation.getRoomType());
                if (success) {
                    System.out.println(Thread.currentThread().getName() + " confirmed booking for " + reservation.getGuestName());
                } else {
                    System.out.println(Thread.currentThread().getName() + " failed booking for " + reservation.getGuestName());
                }
            }
        }
    }
}

public class UseCase11ConcurrentBookingSimulation {
    public static void main(String[] args) {
        InventoryService inventory = new InventoryService();
        inventory.addRoom("Single", 2);

        BookingQueue queue = new BookingQueue();

        queue.addReservation(new Reservation("Alice", "Single"));
        queue.addReservation(new Reservation("Bob", "Single"));
        queue.addReservation(new Reservation("Charlie", "Single"));
        queue.addReservation(new Reservation("David", "Single"));

        Thread t1 = new Thread(new BookingProcessor(queue, inventory), "Thread-1");
        Thread t2 = new Thread(new BookingProcessor(queue, inventory), "Thread-2");

        t1.start();
        t2.start();
    }
}