import java.util.*;

class Room {
    private String type;
    private double price;
    private List<String> amenities;

    public Room(String type, double price, List<String> amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getAmenities() {
        return amenities;
    }
}

class Inventory {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRoom(String type, int count) {
        availability.put(type, count);
    }

    public int getAvailableCount(String type) {
        return availability.getOrDefault(type, 0);
    }

    public Map<String, Integer> getAllAvailability() {
        return Collections.unmodifiableMap(availability);
    }
}

class SearchService {
    private Inventory inventory;
    private Map<String, Room> roomCatalog;

    public SearchService(Inventory inventory, Map<String, Room> roomCatalog) {
        this.inventory = inventory;
        this.roomCatalog = roomCatalog;
    }

    public List<Room> searchAvailableRooms() {
        List<Room> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : inventory.getAllAvailability().entrySet()) {
            if (entry.getValue() > 0) {
                Room room = roomCatalog.get(entry.getKey());
                if (room != null) {
                    result.add(room);
                }
            }
        }
        return result;
    }
}

public class UseCase4RoomSearch {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 2);
        inventory.addRoom("Double", 0);
        inventory.addRoom("Suite", 3);

        Map<String, Room> roomCatalog = new HashMap<>();
        roomCatalog.put("Single", new Room("Single", 1000, Arrays.asList("WiFi", "TV")));
        roomCatalog.put("Double", new Room("Double", 2000, Arrays.asList("WiFi", "TV", "AC")));
        roomCatalog.put("Suite", new Room("Suite", 5000, Arrays.asList("WiFi", "TV", "AC", "Mini Bar")));

        SearchService searchService = new SearchService(inventory, roomCatalog);

        List<Room> availableRooms = searchService.searchAvailableRooms();

        for (Room room : availableRooms) {
            System.out.println("Type: " + room.getType());
            System.out.println("Price: " + room.getPrice());
            System.out.println("Amenities: " + room.getAmenities());
            System.out.println();
        }

