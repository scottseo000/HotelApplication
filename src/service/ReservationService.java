package service;
import model.*;

import java.util.*;
import java.util.stream.Collectors;

public class ReservationService {

    private static final ReservationService SINGLETON = new ReservationService();

    private final Map<String, IRoom> rooms = new HashMap<>();
    private final Map<String, Collection<Reservation>> reservations = new HashMap<>();

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
    public void reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {

        //Make sure room exists
        if (rooms.containsValue(room)) {
            Reservation newReservation = new Reservation(customer, room, checkInDate, checkOutDate);
            Collection<Reservation> customersReservations = new ArrayList<>();

            //If list is empty, new reservation is allowed
            if (this.reservations.isEmpty()) {
                customersReservations.add(newReservation);
                reservations.put(customer.getEmail(), customersReservations);
            } else {
                List<Reservation> roomReservations = new ArrayList<>();
                //Get a list of all reservations with same room as requested room
                //For each entry in the map
                for (Map.Entry<String, Collection<Reservation>> entry : reservations.entrySet()) {
                    //For each reservation in the entry's collection
                    for (Reservation reservation : entry.getValue()) {
                        if (reservation.getRoom().equals(room)) {
                            System.out.println("roomReservations populated");
                            System.out.println(reservation.getRoom().toString());
                            System.out.println(room);
                            roomReservations.add(reservation);
                        }
                    }
                }

                //Check for date overlaps with newly created list
                boolean overlaps = false;
                for (Reservation existingRes : roomReservations) {
                    if (dateOverlaps(newReservation, existingRes)) {
                        System.out.println("overlaps set to true");
                        overlaps = true;
                    }
                }
                if (!overlaps) {
                    System.out.println("\nadded reservation for room" + room.getRoomNumber());
                    if (reservations.containsKey(customer.getEmail())) {
                        //If customer email key exists, add new reservation to their list
                        customersReservations = reservations.get(customer.getEmail());
                        customersReservations.add(newReservation);
                        reservations.put(customer.getEmail(), customersReservations);
                    } else {
                        customersReservations.add(newReservation);
                        reservations.put(customer.getEmail(), customersReservations);
                    }
                }
            }
        }
        else {
            System.out.println("Room not found!");
        }
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {

        Collection<IRoom> unavailableRooms = new ArrayList<>();
        Collection<IRoom> allRooms = rooms.values();

        //Dummy reservation with requested checkIn/checkOut dates
        Reservation requestedReservation = new Reservation(null, null, checkInDate, checkOutDate);

        //Make list of unavailable rooms
        for (Map.Entry<String, Collection<Reservation>> entry : reservations.entrySet()) {
            for (Reservation reservation : entry.getValue()) {
                if (dateOverlaps(requestedReservation, reservation)) {
                    unavailableRooms.add(reservation.getRoom());
                }
            }
        }
        //(all rooms) - (unavailable rooms) = available rooms
        return allRooms.stream()
                .filter(e -> !unavailableRooms.contains(e))
                .collect(Collectors.toList());
    }

    public Collection<Reservation> getCustomersReservations(Customer customer) {
        return reservations.get(customer.getEmail());
    }
    public void printAllReservations() {
        for (Map.Entry<String, Collection<Reservation>> entry : reservations.entrySet()) {
            for (Reservation reservation : entry.getValue()) {
                System.out.println(reservation);
            }
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
