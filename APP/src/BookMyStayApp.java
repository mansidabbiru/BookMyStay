import java.util.*;

class AddOnService {
    private String name;
    private double cost;

    public AddOnService(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }
}

class AddOnServiceManager {
    private Map<String, List<AddOnService>> serviceMap;

    public AddOnServiceManager() {
        serviceMap = new HashMap<>();
    }

    public void addService(String reservationId, AddOnService service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);
    }

    public List<AddOnService> getServices(String reservationId) {
        return serviceMap.getOrDefault(reservationId, new ArrayList<>());
    }

    public double calculateTotalCost(String reservationId) {
        double total = 0;
        List<AddOnService> services = serviceMap.getOrDefault(reservationId, new ArrayList<>());
        for (AddOnService s : services) {
            total += s.getCost();
        }
        return total;
    }
}

public class UseCase7AddOnServiceSelection {
    public static void main(String[] args) {
        AddOnServiceManager manager = new AddOnServiceManager();

        String reservationId1 = "RES-101";
        String reservationId2 = "RES-102";

        manager.addService(reservationId1, new AddOnService("Breakfast", 500));
        manager.addService(reservationId1, new AddOnService("Airport Pickup", 1200));
        manager.addService(reservationId2, new AddOnService("Extra Bed", 800));

        List<AddOnService> services1 = manager.getServices(reservationId1);
        System.out.println("Services for " + reservationId1 + ":");
        for (AddOnService s : services1) {
            System.out.println(s.getName() + " - " + s.getCost());
        }
        System.out.println("Total Add-On Cost: " + manager.calculateTotalCost(reservationId1));

        List<AddOnService> services2 = manager.getServices(reservationId2);
        System.out.println("\nServices for " + reservationId2 + ":");
        for (AddOnService s : services2) {
            System.out.println(s.getName() + " - " + s.getCost());
        }
        System.out.println("Total Add-On Cost: " + manager.calculateTotalCost(reservationId2));
    }
}