import java.util.*;

public class FlightRouteManager {

    private Map<String, List<Route>> graph;

    public FlightRouteManager() {
        graph = new HashMap<>();
    }

    public void addAirport(String airport) {
        if (!graph.containsKey(airport)) {
            graph.put(airport, new ArrayList<>());
        }
    }

    public void addRoute(String from, String to, int distance) {
        if (!graph.containsKey(from)) {
            addAirport(from);
        }
        if (!graph.containsKey(to)) {
            addAirport(to);
        }
        graph.get(from).add(new Route(to, distance));
    }

    public int findShortestRoute(String start, String destination) {
        if (!graph.containsKey(start) || !graph.containsKey(destination)) {
            return -1;
        }

        PriorityQueue<Route> pq = new PriorityQueue<>(Comparator.comparingInt(r -> r.distance));
        Map<String, Integer> distances = new HashMap<>();
        Set<String> visited = new HashSet<>();

        for (String airport : graph.keySet()) {
            distances.put(airport, Integer.MAX_VALUE);
        }
        distances.put(start, 0);
        pq.offer(new Route(start, 0));

        while (!pq.isEmpty()) {
            Route current = pq.poll();
            if (visited.contains(current.destination)) {
                continue;
            }

            visited.add(current.destination);

            for (Route neighbor : graph.getOrDefault(current.destination, new ArrayList<>())) {
                int newDistance = distances.get(current.destination) + neighbor.distance;
                if (newDistance < distances.get(neighbor.destination)) {
                    distances.put(neighbor.destination, newDistance);
                    pq.offer(new Route(neighbor.destination, newDistance));
                }
            }
        }

        return distances.getOrDefault(destination, -1);
    }

    public void printRoutes() {
        for (String airport : graph.keySet()) {
            System.out.println(airport + " -> " + graph.get(airport));
        }
    }

    static class Route {
        String destination;
        int distance;

        Route(String destination, int distance) {
            this.destination = destination;
            this.distance = distance;
        }

        @Override
        public String toString() {
            return destination + " (" + distance + " km)";
        }
    }

    public static void main(String[] args) {
        FlightRouteManager manager = new FlightRouteManager();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Flight Route Manager!");
        boolean running = true;

        while (running) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Add an airport");
            System.out.println("2. Add a flight route");
            System.out.println("3. Find shortest route");
            System.out.println("4. Print all routes");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter airport code: ");
                    String airport = scanner.nextLine();
                    manager.addAirport(airport);
                    System.out.println("Airport added.");
                    break;
                case 2:
                    System.out.print("Enter source airport: ");
                    String from = scanner.nextLine();
                    System.out.print("Enter destination airport: ");
                    String to = scanner.nextLine();
                    System.out.print("Enter distance (in km): ");
                    int distance = scanner.nextInt();
                    manager.addRoute(from, to, distance);
                    System.out.println("Route added.");
                    break;
                case 3:
                    System.out.print("Enter starting airport: ");
                    String start = scanner.nextLine();
                    System.out.print("Enter destination airport: ");
                    String destination = scanner.nextLine();
                    int shortestRoute = manager.findShortestRoute(start, destination);
                    if (shortestRoute == -1) {
                        System.out.println("No route found.");
                    } else {
                        System.out.println("Shortest route distance: " + shortestRoute + " km");
                    }
                    break;
                case 4:
                    System.out.println("Flight Routes:");
                    manager.printRoutes();
                    break;
                case 5:
                    System.out.println("Exiting... Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }
}
