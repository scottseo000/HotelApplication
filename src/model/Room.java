package model;

public class Room implements IRoom {

    String roomNumber;
    Double price;
    RoomType enumeration;

    public Room(String roomNumber, Double price, RoomType enumeration) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.enumeration = enumeration;
    }
    @Override
    public String getRoomNumber() {
        return roomNumber;
    }

    @Override
    public Double getRoomPrice() {
        return price;
    }

    @Override
    public RoomType getRoomType() {
        return enumeration;
    }

    @Override
    public boolean isFree() {
        return false;
    }

    @Override
    public String toString() {
        return "Room Number: " + roomNumber +
                "\n Price: " + price +
                "\n Type: " + enumeration;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof Room)) {
            return false;
        }

        Room r = (Room) obj;
        return roomNumber.equals(r.getRoomNumber());
    }

    @Override
    public int hashCode() {
        return roomNumber.hashCode();
    }
}
