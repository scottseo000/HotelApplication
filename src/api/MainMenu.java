package api;

import model.IRoom;
import model.Reservation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class MainMenu {
    static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    private static final HotelResource hotelResource = HotelResource.getSingleton();


    public static void  mainMenu() {
        String userInput = "";
        Scanner scanner = new Scanner(System.in);

        try {
            boolean exit = false;
            while (!exit) {
                showMainMenu();
                userInput = scanner.nextLine();
                switch (userInput) {
                    case "1":
                        findAndReserveRoom();
                        break;
                    case "2":
                        findCustomerReservation();
                        break;
                    case "3":
                        createCustomerAccount();
                        break;
                    case "4":
                        AdminMenu.adminMenu();
                        break;
                    case "5":
                        System.out.println("Exiting program.");
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid input, please try again");
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input");
        }
    }
    public static void findAndReserveRoom() {
        Scanner scanner = new Scanner(System.in);
        String userInput;
        boolean isFilledOut = false;
        boolean customerAccountExists = false;
        Date checkInDate = new Date();
        Date checkOutDate = new Date();
        String email = "";

        while (!isFilledOut) {
            try {
                //Check customer for account
                System.out.println("Do you have an account with us? (y/n):\n");
                userInput = scanner.nextLine();
                if (userInput.equalsIgnoreCase("y")) {
                    System.out.println("Please enter your email:");
                    email = scanner.nextLine();
                    if (hotelResource.getCustomer(email) != null) {
                        System.out.println("Customer account found!");
                        customerAccountExists = true;
                    } else {
                        System.out.println("Customer account not found");
                    }
                } else if (userInput.equalsIgnoreCase("n")) {
                    System.out.println("Would you like to create an account? (y/n):\n");
                    userInput = scanner.nextLine();
                    if (userInput.equalsIgnoreCase("y")) {
                        createCustomerAccount();
                    }
                } else {
                    System.out.println("Invalid input, please try again");
                }

                df.setLenient(false);
                //Get requested date information
                if (customerAccountExists) {
                    System.out.println("\nPlease enter desired check-in date in format MM/DD/YYYY (ex. 02/14/2024):\n");
                    System.out.println("To cancel, type 'exit': \n");
                    userInput = scanner.nextLine();
                    if (userInput.equalsIgnoreCase("exit")) {
                        customerAccountExists = false;
                    } else {
                        checkInDate = new SimpleDateFormat("MM/dd/yyyy").parse(userInput);
                    }
                }

                if (customerAccountExists) {
                    System.out.println("\nPlease enter desired check-out date in format MM/DD/YYYY (ex. 02/14/2024):\n");
                    System.out.println("To cancel, type 'exit': \n");
                    userInput = scanner.nextLine();
                    if (userInput.equalsIgnoreCase("exit")) {
                        customerAccountExists = false;
                    } else {
                        checkOutDate = new SimpleDateFormat("MM/dd/yyyy").parse(userInput);
                        isFilledOut = true;
                    }
                }

                //Retrieve all available rooms based on date range
                Collection<IRoom> availableRooms = new ArrayList<>(hotelResource.findARoom(checkInDate, checkOutDate));

                if (customerAccountExists){
                    if (availableRooms.isEmpty()) {
                        System.out.println("No rooms were found within that date range");

                        //Check for openings +- 7 days
                        Calendar cal = Calendar.getInstance();

                        //+7 days from requested dates
                        cal.setTime(checkInDate);
                        cal.add(Calendar.DATE, 7);
                        Date altCheckInDate = cal.getTime();
                        cal.setTime(checkOutDate);
                        cal.add(Calendar.DATE, 7);
                        Date altCheckOutDate = cal.getTime();
                        Collection<IRoom> alternativeRooms = new ArrayList<>(hotelResource.findARoom(altCheckInDate, altCheckOutDate));

                        //-7 days from requested dates
                        cal.setTime(checkInDate);
                        cal.add(Calendar.DATE, -7);
                        altCheckInDate = cal.getTime();
                        cal.setTime(checkOutDate);
                        cal.add(Calendar.DATE, -7);
                        altCheckOutDate = cal.getTime();
                        alternativeRooms.addAll(hotelResource.findARoom(altCheckInDate, altCheckOutDate));

                        if (!alternativeRooms.isEmpty()) {
                            System.out.println("\n\nWe have found some open rooms close to your original requested date, would you like to view them? (y/n):\n");
                            userInput = scanner.nextLine();
                            if (userInput.equalsIgnoreCase("y")) {
                                for (IRoom room : alternativeRooms) {
                                    System.out.println(room.toString());
                                    System.out.println("\n");
                                }
                                System.out.println("Would you like to book one of these rooms? (y/n):\n");
                                userInput = scanner.nextLine();
                                if (userInput.equalsIgnoreCase("y")) {
                                    System.out.println("\nWhich room number would you like to reserve?");
                                    userInput = scanner.nextLine();
                                    if (!alternativeRooms.contains(userInput)) {
                                        System.out.println("Room number not found");
                                    } else {
                                        System.out.println("Please confirm if this information is correct with (y/n):\n");
                                        System.out.println(hotelResource.getRoom(userInput).toString() + "\n");
                                        System.out.println("Check in date: " + df.format(checkInDate) + "\n");
                                        System.out.println("Check out date: " + df.format(checkOutDate) + "\n");
                                        userInput = scanner.nextLine();
                                        if (userInput.equalsIgnoreCase("y")) {
                                            System.out.println("Reservation confirmed!\n");
                                            hotelResource.bookARoom(email, hotelResource.getRoom(userInput), checkInDate, checkOutDate);
                                        }
                                    }
                                }
                            }
                        }

                    } else {
                        System.out.println("Here is a list of available rooms: \n");
                        for (IRoom room : availableRooms) {
                            System.out.println(room);
                            System.out.println("\n");
                        }
                        System.out.println("Would you like to reserve one of these rooms? y/n:\n");
                        userInput = scanner.nextLine();

                        //Enter this while loop if user does NOT enter y or n
                        while (!userInput.equalsIgnoreCase("y") && !userInput.equalsIgnoreCase("n")) {
                            System.out.println("Please enter y or n: ");
                            System.out.println("Would you like to reserve one of these rooms? y/n:\n");
                            userInput = scanner.nextLine();
                        }

                        if (userInput.equalsIgnoreCase("n")) {
                            customerAccountExists = false;
                        } else if (userInput.equalsIgnoreCase("y")) {
                            //Make a list of available rooms
                            Collection<String> listOfRoomNumbers = new ArrayList<>();
                            for (IRoom room : availableRooms) {
                                listOfRoomNumbers.add(room.getRoomNumber());
                            }

                            System.out.println("\nWhich room number would you like to reserve?");
                            String requestedRoom = scanner.nextLine();
                            if (!listOfRoomNumbers.contains(requestedRoom)) {
                                System.out.println("Room number not found");
                            } else {
                                System.out.println("Please confirm if this information is correct with (y/n):\n");
                                System.out.println(hotelResource.getRoom(requestedRoom).toString() + "\n");
                                System.out.println("Check in date: " + df.format(checkInDate) + "\n");
                                System.out.println("Check out date: " + df.format(checkOutDate) + "\n");
                                userInput = scanner.nextLine();
                                if (userInput.equalsIgnoreCase("y")) {
                                    System.out.println("Reservation confirmed!\n");
                                    hotelResource.bookARoom(email, hotelResource.getRoom(requestedRoom), checkInDate, checkOutDate);
                                }
                            }
                        }
                    }
                }

            } catch(ParseException e) {
                System.out.println("Invalid input, please try again\n\n");
            }
        }
    }
    public static void findCustomerReservation() {
        Scanner scanner = new Scanner(System.in);
        String userInput = "";
        System.out.println("\nPlease enter your email: ");
        userInput = scanner.nextLine();
        String emailRegex = "^(.+)@(.+).(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        if(pattern.matcher(userInput).matches()) {
            System.out.println("user email is valid");
            Collection<Reservation> customersReservations = new ArrayList<>(hotelResource.getCustomersReservations(userInput));
            if (customersReservations.isEmpty()) {
                System.out.println("No reservations found for that email");
            }
            else {
                for (Reservation reservation : customersReservations) {
                    System.out.println("\n" + reservation + "\n");
                }
            }
        }
        else {
            System.out.println("Invalid input, please try again");
        }
    }
    public static void createCustomerAccount() {
        String userInput = "";
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter your first name:");
        String firstName = scanner.nextLine();
        System.out.println("Please enter your last name:");
        String lastName = scanner.nextLine();
        System.out.println("Please enter your email:");
        String email = scanner.nextLine();

        //Validate email
        String emailRegex = "^(.+)@(.+).(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        if(pattern.matcher(email).matches()) {
            hotelResource.createACustomer(firstName, lastName, email);
            System.out.println("Customer account created!");
        } else {
            System.out.println("Invalid input, please try again");
        }
    }
    public static void showMainMenu() {
        System.out.println("Welcome to the Hotel Reservation Java Application!\n" +
                "Please type a number to select an option: \n" +
                "1. Find and reserve a room\n" +
                "2. See my reservations\n" +
                "3. Create an account\n" +
                "4. Admin options\n" +
                "5 Exit\n");
    }
}
