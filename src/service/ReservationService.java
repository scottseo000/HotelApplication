package service;
import model.*;

import java.util.*;
import java.util.stream.Collectors;

public class ReservationService {

    private static final ReservationService SINGLETON = new ReservationService();

    private final Map<String, IRoom> rooms = new HashMap<>();
    private final Map<String, Reservation> reservations = new HashMap<>();

    public static ReservationService getSingleton() {
        return SINGLETON;
    }

    public void addRoom(IRoom room) {
        rooms.put(room.getRoomNumber(), room);
    }
    public IRoom getARoom(String roomId){
        return rooms.getOrDefault(roomId, null);
    }
    public Collection<IRoom> getAllRooms(){
        return rooms.values();
    }

    //To ensure main program knows if reservation worked or not, bool was used
    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {

        //Make sure room exists
        if (rooms.containsValue(room)) {

            Reservation res = new Reservation(customer, room, checkInDate, checkOutDate);

            //If list is empty, new reservation is allowed
            if (this.reservations.isEmpty()) {
                reservations.put(customer.getEmail(), res);
                return res;
            }

            List<Reservation> roomReservations = new ArrayList<>();
            //Get a list of all reservations with same room as requested room
            for (Map.Entry<String, Reservation> entry : reservations.entrySet()) {
                if (entry.getValue().getRoom().equals(room)) {
                    roomReservations.add(entry.getValue());
                }
            }
            //Check for date overlaps with newly created list
            boolean overlaps = false;
            for (Reservation existingRes : roomReservations) {
                if (dateOverlaps(res, existingRes)) {
                    overlaps = true;
                }
            }
            if (overlaps) {
                return null;
            }
            else {
                reservations.put(customer.getEmail(), res);
                return res;
            }
        }
        else {
            System.out.println("Room not found!");
            return null;
        }
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {

        Collection<IRoom> unavailableRooms = new ArrayList<>();
        Collection<IRoom> allRooms = rooms.values();

        //Dummy reservation with requested checkIn/checkOut dates
        Reservation requestedReservation = new Reservation(null, null, checkInDate, checkOutDate);

        //Make list of unavailable rooms
        for (Map.Entry<String, Reservation> entry : reservations.entrySet()) {
            if(dateOverlaps(requestedReservation, entry.getValue())) {
                unavailableRooms.add(entry.getValue().getRoom());
            }
        }
        //(all rooms) - (unavailable rooms) = available rooms
        return allRooms.stream()
                .filter(e -> !unavailableRooms.contains(e))
                .collect(Collectors.toList());
    }

    public Collection<Reservation> getCustomersReservations(Customer customer) {
        Collection<Reservation> customersReservations = new ArrayList<>();

        //Populate list with reservations where key == customer's email
        for (Map.Entry<String, Reservation> entry : reservations.entrySet()) {
            if(entry.getKey().equals(customer.getEmail())) {
                customersReservations.add(entry.getValue());
            }
        }

        return customersReservations;
    }
    public void printAllReservations() {
        for (Reservation reservation : reservations.values()) {
            System.out.println(reservation.toString());
        }
    }

    private boolean dateOverlaps (Reservation newReservation, Reservation existingReservation) {
        /* To count as "overlapping" date range, there are 2 requirements:
         * 1. newReservation.checkInDate must be BEFORE existing.checkOutDate
         * AND
         * 2. newReservation.checkOutDate must be AFTER existing.checkInDate
         */
        return ((newReservation.getCheckInDate()).before(existingReservation.getCheckOutDate()) &&
                (newReservation.getCheckOutDate()).after(existingReservation.getCheckInDate()));
    }
}
