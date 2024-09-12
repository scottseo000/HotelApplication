package model;

import java.util.Date;

public class Reservation {
    Customer customer;
    IRoom room;
    Date checkInDate;
    Date checkOutDate;

    public Reservation(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }
    public Date getCheckInDate() {
        return checkInDate;
    }
    public Date getCheckOutDate() {
        return checkOutDate;
    }
    public IRoom getRoom() {
        return room;
    }

    @Override
    public String toString() {
        return "Customer :" + customer.toString() +
                "\n" + room.toString() +
                "\nCheck in: " + checkInDate +
                "\nCheck out: " + checkOutDate;
    }
}
