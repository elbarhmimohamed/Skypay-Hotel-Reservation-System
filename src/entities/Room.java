package entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Room {

    private int id;

    private RoomType type;

    private int pricePerNight;

    public Room(int id, RoomType type, int pricePerNight) {
        this.id = id;
        this.type = type;
        this.pricePerNight = pricePerNight;
    }
    public Room(Room room) {
        this.id = room.getId();
        this.type = room.getType();
        this.pricePerNight = room.getPricePerNight();
    }

    public void update(RoomType type, int pricePerNight){

        if(type != null) {
            this.setType(type);
        }

        this.setPricePerNight(pricePerNight);

    }

}
