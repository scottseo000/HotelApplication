package api;

import model.*;
import service.*;

import java.util.Collection;
import java.util.Date;

public class HotelResource {
    private static final HotelResource SINGLETON = new HotelResource();

    private final CustomerService customerService = CustomerService.getSingleton();
    private final ReservationService reservationService = ReservationService.getSingleton();

    private HotelResource() {

    }

    public static HotelResource getSingleton() {
        return SINGLETON;
    }
    public Customer getCustomer(String email) {
        return customerService.getCustomer(email);
    }
    public void createACustomer(String firstName, String lastName, String email) {
        customerService.addCustomer(firstName, lastName, email);
    }
    public IRoom getRoom (String roomNumber) {
        return reservationService.getARoom(roomNumber);
    }
    public Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate, Date checkOutDate) {
        Customer customer = customerService.getCustomer(customerEmail);
        if (customer == null) {
            System.out.println("Customer account not found!");
            return null;
        } else {
            return reservationService.reserveARoom(customer, room, checkInDate, checkOutDate);
        }
    }
    public Collection<Reservation> getCustomersReservations(String customerEmail) {
        Customer customer = customerService.getCustomer(customerEmail);
        return reservationService.getCustomersReservations(customer);
    }
    public Collection<IRoom> findARoom(Date checkInDate, Date checkOutDate) {
        return reservationService.findRooms(checkInDate, checkOutDate);
    }
}
