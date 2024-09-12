package api;

import com.sun.tools.javac.Main;
import model.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class AdminMenu {

    private static final AdminResource adminResource = AdminResource.getSingleton();
    private static final HotelResource hotelResource = HotelResource.getSingleton();

    public static void adminMenu() {
        printAdminMenu();
        Scanner scanner = new Scanner(System.in);
        String userInput = "";

        try {
            boolean exit = false;
            while (!exit) {
                userInput = scanner.nextLine();
                switch (userInput) {
                    case "1":
                        seeAllCustomers();
                        break;
                    case "2":
                        seeAllRooms();
                        break;
                    case "3":
                        seeAllReservations();
                        break;
                    case "4":
                        addARoom();
                        break;
                    case "5":
                        addTestData();
                        break;
                    case "6":
                        System.out.println("Back to main menu\n\n");
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid input");
                        break;
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input");
        }
        MainMenu.mainMenu();
    }
    public static void seeAllCustomers() {
        Collection<Customer> customers = new ArrayList<>(adminResource.getAllCustomers());
        if (customers.isEmpty()) {
            System.out.println("No customers found");
        }
        for (Customer customer : customers) {
            System.out.println(customer.toString());
        }
    }
    public static void seeAllRooms() {
        Collection<IRoom> allRooms = new ArrayList<>(adminResource.getAllRooms());
        if (allRooms.isEmpty()) {
            System.out.println("No rooms found");
        }
        for (IRoom room : allRooms) {
            System.out.println(room.toString());
        }
    }
    public static void seeAllReservations() {
        adminResource.displayAllReservations();
    }

    public static void addARoom() {
        Scanner scanner = new Scanner(System.in);
        String userInput = "";
        String roomNumber = "";
        Double price = 0.0;
        List<IRoom> roomsToAdd = new ArrayList<>();
        boolean doneAddingRooms = false;

        while (!doneAddingRooms) {
            System.out.println("Enter room number: ");
            roomNumber = scanner.nextLine();

            if (hotelResource.getRoom(roomNumber) != null) {
                System.out.println("Room already exists");
            } else {
                System.out.println("Enter price:\n");
                //Round inputted double to 2 decimals
                try {
                    BigDecimal bd = BigDecimal.valueOf(scanner.nextDouble());
                    bd = bd.setScale(2, RoundingMode.HALF_UP);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input");
                }

                //Get single or double information
                do {
                    System.out.println("Enter 1 for single room, 2 for double room");
                    userInput = scanner.nextLine();
                    if (!(userInput.equals("1") || userInput.equals("2"))) {
                        System.out.println("Please enter a valid input");
                    }
                } while (!(userInput.equals("1") || userInput.equals("2")));
                if (userInput.equals("1")) {
                    Room room = new Room(roomNumber, price, RoomType.SINGLE);
                    roomsToAdd.add(room);
                } else {
                    Room room = new Room(roomNumber, price, RoomType.DOUBLE);
                    roomsToAdd.add(room);
                }
            }
            System.out.println("\nWould you like to add more rooms? (y/n)\n");
            userInput = scanner.nextLine();
            if (userInput.equals("n")) {
                doneAddingRooms = true;
            }
        }
        System.out.println("Rooms added");
        adminResource.addRoom(roomsToAdd);
        adminMenu();
    }
    public static void addTestData() {
        Scanner scanner = new Scanner(System.in);
        String userInput = "";

        System.out.println("Are you sure you would like to add test data? (y/n)");
        userInput = scanner.nextLine();
        if (userInput.equalsIgnoreCase("y")) {
            //Add test rooms
            Room room1 = new Room("100", 100.0, RoomType.SINGLE);
            Room room2 = new Room("101", 120.0, RoomType.SINGLE);
            Room room3 = new Room("102", 160.0, RoomType.DOUBLE);
            List<IRoom> testRoomList = new ArrayList<>();
            testRoomList.add(room1);
            testRoomList.add(room2);
            testRoomList.add(room3);
            adminResource.addRoom(testRoomList);

            //Add test customers
            hotelResource.createACustomer("John", "Doe", "johndoe@gmail.com");
            hotelResource.createACustomer("Jane", "Doe", "janedoe@gmail.com");
            hotelResource.createACustomer("Jack", "Smith", "jacksmith@gmail.com");
            hotelResource.createACustomer("Jill", "Smith", "jillsmith@gmail.com");

            //Add 2 test reservations
            Calendar calendar = Calendar.getInstance();
            calendar.set(2024, 00, 01);
            Date checkInDate = calendar.getTime();
            calendar.set(2024, 00, 10);
            Date checkOutDate = calendar.getTime();
            hotelResource.bookARoom("johndoe@gmail.com", room1, checkInDate, checkOutDate);

            calendar.set(2024, 11, 25);
            checkInDate = calendar.getTime();
            calendar.set(2024, 11, 30);
            checkOutDate = calendar.getTime();
            hotelResource.bookARoom("jacksmith@gmail.com", room2, checkInDate, checkOutDate);

            System.out.println("Test data added successfully\n");
            adminMenu();
        } else {
            adminMenu();
        }
    }
    public static void printAdminMenu() {
        System.out.println("Admin Menu\n");
        System.out.println("Please type an option:\n" +
                "1. See all customers\n" +
                "2. See all rooms\n" +
                "3. See all reservations\n" +
                "4. Add a room\n" +
                "5. Add test rooms/reservations/customers\n" +
                "6. Return to main menu");
    }
}
