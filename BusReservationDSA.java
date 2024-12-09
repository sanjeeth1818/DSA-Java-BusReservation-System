import java.io.*;
import java.util.*;

// Bus class
class Bus {
    private final String busNumber;
    private final int totalSeats;
    private final String startPoint;
    private final String endPoint;
    private final String startTime;
    private final double fare;

    public Bus(String busNumber, int totalSeats, String startPoint, String endPoint, String startTime, double fare) {
        this.busNumber = busNumber;
        this.totalSeats = totalSeats;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.startTime = startTime;
        this.fare = fare;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    @Override
    public String toString() {
        return "Bus Number: " + busNumber +
               ", Seats: " + totalSeats +
               ", Route: " + startPoint + " to " + endPoint +
               ", Time: " + startTime +
               ", Fare: " + fare;
    }

    public String toDataString() {
        return String.join(",", busNumber, String.valueOf(totalSeats), startPoint, endPoint, startTime, String.valueOf(fare));
    }

    public static Bus fromDataString(String data) {
        String[] parts = data.split(",");
        if (parts.length != 6) {
            throw new IllegalArgumentException("Invalid data format for Bus: " + data);
        }
        return new Bus(parts[0], Integer.parseInt(parts[1]), parts[2], parts[3], parts[4], Double.parseDouble(parts[5]));
    }
}

// Customer class
class Customer {
    private final String name;
    private final String mobileNumber;
    private final String email;
    private final String city;
    private final int age;

    public Customer(String name, String mobileNumber, String email, String city, int age) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.city = city;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Name: " + name +
               ", Mobile: " + mobileNumber +
               ", Email: " + email +
               ", City: " + city +
               ", Age: " + age;
    }

    public String toDataString() {
        return String.join(",", name, mobileNumber, email, city, String.valueOf(age));
    }

    public static Customer fromDataString(String data) {
        String[] parts = data.split(",");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Invalid data format for Customer: " + data);
        }
        return new Customer(parts[0], parts[1], parts[2], parts[3], Integer.parseInt(parts[4]));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return age == customer.age &&
               name.equals(customer.name) &&
               mobileNumber.equals(customer.mobileNumber) &&
               email.equals(customer.email) &&
               city.equals(customer.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, mobileNumber, email, city, age);
    }
}

// BusRegistration class
class BusRegistration {
    private final Map<String, Bus> busRegistry = new HashMap<>();
    private final String fileName;

    public BusRegistration(String fileName) {
        this.fileName = fileName;
    }

    public void registerBus(Bus bus) {
        if (busRegistry.containsKey(bus.getBusNumber())) {
            System.out.println("Bus with number " + bus.getBusNumber() + " is already registered.");
        } else {
            busRegistry.put(bus.getBusNumber(), bus);
            System.out.println("Bus registered successfully: " + bus);
            saveToFile();
        }
    }

    public Bus getBusByNumber(String busNumber) {
        return busRegistry.get(busNumber);
    }

    public List<Bus> searchBuses(String startPoint, String endPoint) {
        List<Bus> matchingBuses = new ArrayList<>();
        for (Bus bus : busRegistry.values()) {
            if (bus.getStartPoint().equalsIgnoreCase(startPoint) && bus.getEndPoint().equalsIgnoreCase(endPoint)) {
                matchingBuses.add(bus);
            }
        }
        return matchingBuses;
    }

    public void viewAllBuses() {
        if (busRegistry.isEmpty()) {
            System.out.println("No buses registered.");
        } else {
            System.out.println("Registered Buses:");
            for (Bus bus : busRegistry.values()) {
                System.out.println(bus);
            }
        }
    }

    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Bus bus : busRegistry.values()) {
                writer.write(bus.toDataString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving buses: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                try {
                    Bus bus = Bus.fromDataString(line);
                    busRegistry.put(bus.getBusNumber(), bus);
                } catch (IllegalArgumentException e) {
                    System.out.println("Skipping invalid line in bus file: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading buses: " + e.getMessage());
        }
    }
}

// CustomerRegistration class
class CustomerRegistration {
    private final Map<String, Customer> customers = new HashMap<>();
    private final String fileName;

    public CustomerRegistration(String fileName) {
        this.fileName = fileName;
    }

    public void registerCustomer(Customer customer) {
        customers.put(customer.getName(), customer);
        System.out.println("Customer registered successfully.");
        saveToFile();
    }

    public Customer getCustomerByName(String name) {
        return customers.get(name);
    }

    public void viewAllCustomers() {
        if (customers.isEmpty()) {
            System.out.println("No customers registered.");
        } else {
            System.out.println("Registered Customers (Newest to Oldest):");
            Stack<Customer> stack = new Stack<>();
            for (Customer customer : customers.values()) {
                stack.push(customer);
            }
            while (!stack.isEmpty()) {
                System.out.println(stack.pop());
            }
        }
    }

    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Customer customer : customers.values()) {
                writer.write(customer.toDataString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving customers: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                try {
                    Customer customer = Customer.fromDataString(line);
                    customers.put(customer.getName(), customer);
                } catch (IllegalArgumentException e) {
                    System.out.println("Skipping invalid line in customer file: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading customers: " + e.getMessage());
        }
    }
}

// ReservationSystem class
class ReservationSystem {
    private final Map<String, List<Customer>> reservations = new HashMap<>();
    private final Map<String, Queue<Customer>> waitingList = new HashMap<>();
    private final String fileName;

    public ReservationSystem(String fileName) {
        this.fileName = fileName;
    }

    public void reserveSeat(Customer customer, String busNumber, BusRegistration busReg) {
        Bus bus = busReg.getBusByNumber(busNumber);
        if (bus == null) {
            System.out.println("No bus registered with: " + busNumber);
            return;
        }

        int capacity = bus.getTotalSeats();
        reservations.putIfAbsent(busNumber, new ArrayList<>());
        List<Customer> busReservations = reservations.get(busNumber);

        if (busReservations.size() < capacity) {
            busReservations.add(customer);
            System.out.println("Seat reserved for " + customer);
        } else {
            waitingList.putIfAbsent(busNumber, new LinkedList<>());
            waitingList.get(busNumber).offer(customer);
            System.out.println("Bus seats are full. Added to waiting list: " + customer);
        }
        saveToFile();
    }
    public boolean isCustomerReserved(Customer customer, String busNumber) {
        List<Customer> busReservations = reservations.get(busNumber);
        return busReservations != null && busReservations.contains(customer);
    }

    public void addCustomerToWaitingList(Customer customer, String busNumber, BusRegistration busReg) {
        Bus bus = busReg.getBusByNumber(busNumber);
        if (bus == null) {
            System.out.println("No such bus registered with number: " + busNumber);
            return;
        }

        int capacity = bus.getTotalSeats();
        reservations.putIfAbsent(busNumber, new ArrayList<>());
        List<Customer> busReservations = reservations.get(busNumber);

        if (busReservations.size() >= capacity) {
            waitingList.putIfAbsent(busNumber, new LinkedList<>());
            waitingList.get(busNumber).offer(customer);
            System.out.println("Bus seats are full. Added to waiting list: " + customer);
        } else {
            System.out.println("Seats are still available. Use option 6 to reserve a seat.");
        }
        saveToFile();
    }


    public void cancelReservation(Customer customer, String busNumber) {
        List<Customer> busReservations = reservations.get(busNumber);
        if (busReservations != null && busReservations.remove(customer)) {
            // Notify the passenger whose reservation is canceled
            System.out.println("Notification to " + customer.getName() + ": Your seat has been canceled successfully.");
            
            // Notify co-passengers
            for (Customer coPassenger : busReservations) {
                System.out.println("Notification to " + coPassenger.getName() + ": Dear " + coPassenger.getName() +
                        ", your co-passenger " + customer.getName() + " canceled their seat just now.");
            }
    
            // Promote a customer from the waiting list, if available
            Queue<Customer> waitQueue = waitingList.get(busNumber);
            if (waitQueue != null && !waitQueue.isEmpty()) {
                Customer promotedCustomer = waitQueue.poll();
                busReservations.add(promotedCustomer);
    
                // Notify the promoted passenger
                System.out.println("Notification to " + promotedCustomer.getName() +
                        ": Dear " + promotedCustomer.getName() + ", your seat is now booked from the waiting list!");
            }
        } else {
            System.out.println("No reservation found for " + customer);
        }
        saveToFile();
    }
    

    public void viewReservations(String busNumber) {
        List<Customer> busReservations = reservations.get(busNumber);
        if (busReservations != null && !busReservations.isEmpty()) {
            System.out.println("Reservations for Bus " + busNumber + ":");
            for (Customer customer : busReservations) {
                System.out.println(customer);
            }
        } else {
            System.out.println("No reservations found for Bus " + busNumber);
        }
    }

    public void viewWaitingList(String busNumber) {
        Queue<Customer> waitQueue = waitingList.get(busNumber);
        if (waitQueue != null && !waitQueue.isEmpty()) {
            System.out.println("Waiting List for Bus " + busNumber + ":");
            for (Customer customer : waitQueue) {
                System.out.println(customer);
            }
        } else {
            System.out.println("No waiting list found for Bus " + busNumber);
        }
    }

    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Map.Entry<String, List<Customer>> entry : reservations.entrySet()) {
                writer.write("Reservations:" + entry.getKey());
                writer.newLine();
                for (Customer customer : entry.getValue()) {
                    writer.write(customer.toDataString());
                    writer.newLine();
                }
            }
            for (Map.Entry<String, Queue<Customer>> entry : waitingList.entrySet()) {
                writer.write("WaitingList:" + entry.getKey());
                writer.newLine();
                for (Customer customer : entry.getValue()) {
                    writer.write(customer.toDataString());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving reservations: " + e.getMessage());
        }
    }

    public void loadFromFile(CustomerRegistration customerReg) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            String currentBus = null;
            boolean isWaitingList = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.startsWith("Reservations:")) {
                    currentBus = line.substring(13).trim();
                    isWaitingList = false;
                    reservations.putIfAbsent(currentBus, new ArrayList<>());
                } else if (line.startsWith("WaitingList:")) {
                    currentBus = line.substring(12).trim();
                    isWaitingList = true;
                    waitingList.putIfAbsent(currentBus, new LinkedList<>());
                } else {
                    try {
                        Customer customer = Customer.fromDataString(line);
                        if (isWaitingList) {
                            waitingList.get(currentBus).offer(customer);
                        } else {
                            reservations.get(currentBus).add(customer);
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Skipping invalid line in reservation file: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading reservations: " + e.getMessage());
        }
    }
}

// Main class
public class BusReservationDSA {
    public static void main(String[] args) {
        CustomerRegistration customerReg = new CustomerRegistration("customers.txt");
        ReservationSystem reservationSystem = new ReservationSystem("reservations.txt");
        BusRegistration busReg = new BusRegistration("buses.txt");

        customerReg.loadFromFile();
        busReg.loadFromFile();
        reservationSystem.loadFromFile(customerReg);

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("********************************");
                System.out.println("*       Bus Reservation        *");
                System.out.println("********************************");
                System.out.println("* 1. Register Customer         *");
                System.out.println("* 2. View Registered Customers *");
                System.out.println("* 3. Register New Bus          *");
                System.out.println("* 4. View Registered Buses     *");
                System.out.println("* 5. Search Bus                *");
                System.out.println("* 6. Reserve Seat              *");
                System.out.println("* 7. Cancel Seat Reservation   *");
                System.out.println("* 8. Request Additional Seat   *");
                System.out.println("* 9. View Reservations         *");
                System.out.println("* 10. View Waiting Queue       *");
                System.out.println("* 11. Exit                     *");
                System.out.println("********************************");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        System.out.print("Enter Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter Mobile: ");
                        String mobile = scanner.nextLine();
                        System.out.print("Enter Email: ");
                        String email = scanner.nextLine();
                        System.out.print("Enter City: ");
                        String city = scanner.nextLine();
                        System.out.print("Enter Age: ");
                        int age = scanner.nextInt();
                        scanner.nextLine();
                        Customer customer = new Customer(name, mobile, email, city, age);
                        customerReg.registerCustomer(customer);
                        break;

                    case 2:
                        customerReg.viewAllCustomers();
                        break;

                    case 3:
                        System.out.print("Enter Bus Number: ");
                        String busNumber = scanner.nextLine();
                        System.out.print("Enter Total Seats: ");
                        int seats = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter Start Point: ");
                        String startPoint = scanner.nextLine();
                        System.out.print("Enter End Point: ");
                        String endPoint = scanner.nextLine();
                        System.out.print("Enter Start Time: ");
                        String startTime = scanner.nextLine();
                        System.out.print("Enter Fare: ");
                        double fare = scanner.nextDouble();
                        scanner.nextLine();
                        Bus bus = new Bus(busNumber, seats, startPoint, endPoint, startTime, fare);
                        busReg.registerBus(bus);
                        break;

                    case 4:
                        busReg.viewAllBuses();
                        break;

                    case 5:
                        System.out.print("Enter Start Point: ");
                        startPoint = scanner.nextLine();
                        System.out.print("Enter End Point: ");
                        endPoint = scanner.nextLine();
                        List<Bus> buses = busReg.searchBuses(startPoint, endPoint);
                        if (buses.isEmpty()) {
                            System.out.println("No buses found for the specified route.");
                        } else {
                            System.out.println("Available Buses:");
                            for (Bus b : buses) {
                                System.out.println(b);
                            }
                        }
                        break;

                    case 6:
                        System.out.print("Enter Customer Name: ");
                        name = scanner.nextLine();
                        customer = customerReg.getCustomerByName(name);
                        if (customer != null) {
                            System.out.print("Enter Bus Number: ");
                            busNumber = scanner.nextLine();
                            reservationSystem.reserveSeat(customer, busNumber, busReg);
                        } else {
                            System.out.println("Customer not found.");
                        }
                        break;

                    case 7:
                        System.out.print("Enter Customer Name to Cancel Reservation: ");
                        name = scanner.nextLine();
                        customer = customerReg.getCustomerByName(name);
                        if (customer != null) {
                            System.out.print("Enter Bus Number: ");
                            busNumber = scanner.nextLine();
                            reservationSystem.cancelReservation(customer, busNumber);
                        } else {
                            System.out.println("Customer not found.");
                        }
                        break;

                        case 8:
                        System.out.print("Enter Customer Name to Request Additional Seat: ");
                        name = scanner.nextLine();
                        customer = customerReg.getCustomerByName(name);
                        if (customer != null) {
                            System.out.print("Enter Bus Number: ");
                            busNumber = scanner.nextLine();
                            if (!reservationSystem.isCustomerReserved(customer, busNumber)) {
                                System.out.println("You do not have a reservation for this bus. Please book your seat via option 6.");
                            } else {
                                reservationSystem.addCustomerToWaitingList(customer, busNumber, busReg);
                            }
                        } else {
                            System.out.println("Customer not found.");
                        }
                        break;


                    case 9:
                        System.out.print("Enter Bus Number to View Reservations: ");
                        busNumber = scanner.nextLine();
                        reservationSystem.viewReservations(busNumber);
                        break;

                    case 10:
                        System.out.print("Enter Bus Number to View Waiting Queue: ");
                        busNumber = scanner.nextLine();
                        reservationSystem.viewWaitingList(busNumber);
                        break;

                    case 11:
                        System.out.println("Exiting...");
                        return;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }
    }
}
