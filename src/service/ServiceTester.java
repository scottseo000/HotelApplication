package service;

import model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.*;

public class ServiceTester {
    public static void main(String[] args) {
        final ReservationService reservationService = ReservationService.getSingleton();

        System.out.println("Testing room functionality");
        //Test room functions
        Room room1 = new Room("100", 100.0, RoomType.SINGLE);
        Room room2 = new Room("101", 120.0, RoomType.SINGLE);
        Room room3 = new Room("102", 160.0, RoomType.DOUBLE);

        reservationService.addRoom(room1);
        reservationService.addRoom(room2);
        reservationService.addRoom(room3);

        System.out.println(reservationService.getARoom("100"));
        System.out.println(reservationService.getARoom("101"));
        System.out.println(reservationService.getARoom("102"));

        System.out.println("\n\n\n");
        System.out.println("Adding reservation from 2024/01/01 to 2024/01/10");
        //Should return all 3 rooms
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, 01, 01);
        Date checkInDate = calendar.getTime();
        calendar.set(2024, 01, 10);
        Date checkOutDate = calendar.getTime();
        Collection<IRoom> roomList = new ArrayList<>(reservationService.findRooms(checkInDate, checkOutDate));

        System.out.println("Available rooms: ");
        //toString all rooms
        for (IRoom room : roomList) {
            System.out.println(room);
        }

        System.out.println("\n\n\n");
        System.out.println("Adding new reservation for Scott");
        //Test adding reservations
        Customer c = new Customer("Scott", "S", "scotts@gmail.com");
        reservationService.reserveARoom(c, room1, checkInDate, checkOutDate);

        System.out.println("\n\n");
        System.out.println("Printing all reservations");
        reservationService.printAllReservations();

        System.out.println("\n\n");
        System.out.println("Printing all reservations with email 'scott@gmail.com'");
        //This should only be one string
        Collection<Reservation> reservations = reservationService.getCustomersReservations(c);
        for (Reservation r : reservations) {
            System.out.println(r.toString());
        }

        //Trying findARoom once again with an overlapping reservation
        System.out.println("\n\n\n");
        System.out.println("Printing available rooms (should only be room 101 102");
        Collection<IRoom> availableRooms = new ArrayList<>(reservationService.findRooms(checkInDate, checkOutDate));
        for (IRoom room : availableRooms) {
            System.out.println(room);
        }

        System.out.println("\n\n\n");
        System.out.println("Customer Service");
        final CustomerService customerService = CustomerService.getSingleton();

        customerService.addCustomer("Scott", "S", "scotts@gmail.com");
        customerService.addCustomer("John", "Doe", "johndoe@gmail.com");
        customerService.addCustomer("Jane", "Doe", "janedoe@gmail.com");

        System.out.println(customerService.getCustomer("scotts@gmail.com"));

        System.out.println("\n\n\n");
        System.out.println("Printing all customers:");
        Collection<Customer> customerList = new ArrayList<>(customerService.getAllCustomers());
        for (Customer customer : customerList) {
            System.out.println(customer);
        }
    }
}
