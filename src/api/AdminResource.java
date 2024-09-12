package api;
import model.*;
import service.*;

import java.util.Collection;
import java.util.List;

public class AdminResource {

    static final AdminResource SINGLETON = new AdminResource();

    final CustomerService customerService = CustomerService.getSingleton();
    final ReservationService reservationService = ReservationService.getSingleton();

    public static AdminResource getSingleton() {
        return SINGLETON;
    }

    public Customer getCustomer(String email) {
        return customerService.getCustomer(email);
    }
    public void addRoom(List<IRoom> rooms) {
        for(IRoom room : rooms) {
            reservationService.addRoom(room);
        }
    }
    public Collection<IRoom> getAllRooms() {
        return reservationService.getAllRooms();
    }
    public Collection<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }
    public void displayAllReservations() {
        reservationService.printAllReservations();
    }
}
