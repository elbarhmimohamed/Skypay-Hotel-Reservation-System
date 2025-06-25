package services;

import entities.Room;
import entities.RoomType;
import exceptions.DuplicateEntryException;
import exceptions.NoEntryFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static utils.Constants.BACK_TO_LINE;
import static utils.Constants.ROOM_HEADER;
import static utils.Constants.SEPARATOR;

public class RoomService {

    private List<Room> rooms = new ArrayList<>();

    public List<Room> getAllRooms(){
        return rooms;
    }
    public int getRoomsCount(){
        return rooms.size();
    }

    public Room findRoomById(int roomId){

        Optional<Room> room = rooms.stream().filter(u -> u.getId() == roomId).findFirst();

        if (!room.isPresent()){
            throw new NoEntryFoundException(String.format("Room with id %d not found.", roomId));
        }

        return room.get();
    }

    void setRoom(int roomId, RoomType roomType, int roomPricePerNight){

        Room room = rooms.stream().filter(r -> r.getId() == roomId)
                .findFirst().orElse(null);

        if(room == null){
            room = new Room(roomId, roomType, roomPricePerNight);
            rooms.add(room);
        }
        else{
            room.update(roomType, roomPricePerNight);
        }

    }

    void printAllRooms(){

        if(this.rooms.isEmpty()){
            throw new NoEntryFoundException("No Room Found");
        }

        StringBuilder result = new StringBuilder(ROOM_HEADER);

        for (int i = rooms.size() - 1 ; i >= 0 ; i--) {

            result.append(String.format("%-7s",rooms.get(i).getId())).append(SEPARATOR);
            result.append(String.format("%-9s",rooms.get(i).getType())).append(SEPARATOR);
            result.append(String.format("%-11s",rooms.get(i).getPricePerNight())).append(BACK_TO_LINE);
        }

        System.out.println(result.toString());

    }

}
