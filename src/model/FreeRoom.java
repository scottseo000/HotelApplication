package model;

public class FreeRoom extends Room {
    public FreeRoom(String roomNumber, Double price, RoomType type) {
        super(roomNumber, 0.0, type);
    }

    @Override
    public String toString() {
        return "Room number: " + this.roomNumber +
                "\nPrice: free" +
                "\nRoom Type: " + this.enumeration;
    }
}
