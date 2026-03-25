import java.util.*;

class Reservation {
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

class BookingHistory {
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    public void addReservation(Reservation reservation) {
        history.add(reservation);
    }

    public List<Reservation> getAllReservations() {
        return new ArrayList<>(history);
    }
}

class BookingReportService {
    private BookingHistory bookingHistory;

    public BookingReportService(BookingHistory bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    public void displayAllBookings() {
        List<Reservation> list = bookingHistory.getAllReservations();
        for (Reservation r : list) {
            System.out.println(r.getReservationId() + " | " + r.getGuestName() + " | " + r.getRoomType());
        }
    }

    public void generateSummary() {
        List<Reservation> list = bookingHistory.getAllReservations();
        Map<String, Integer> countByType = new HashMap<>();

        for (Reservation r : list) {
            countByType.put(r.getRoomType(), countByType.getOrDefault(r.getRoomType(), 0) + 1);
        }

        System.out.println("\nSummary Report:");
        for (Map.Entry<String, Integer> entry : countByType.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

public class UseCase8BookingHistoryReport {
    public static void main(String[] args) {
        BookingHistory history = new BookingHistory();

        history.addReservation(new Reservation("RES-1", "Alice", "Single"));
        history.addReservation(new Reservation("RES-2", "Bob", "Double"));
        history.addReservation(new Reservation("RES-3", "Charlie", "Single"));
        history.addReservation(new Reservation("RES-4", "David", "Suite"));

        BookingReportService reportService = new BookingReportService(history);

        System.out.println("Booking History:");
        reportService.displayAllBookings();

        reportService.generateSummary();
    }
}