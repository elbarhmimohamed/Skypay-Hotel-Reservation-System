package services;

import entities.Room;
import entities.RoomType;
import exceptions.NoEntryFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RoomServiceTest {

    private RoomService roomService = new RoomService();


    @Test
    void createAndUpdateRoomTest(){

        // create
        roomService.setRoom(1, RoomType.JUNIOR,  200);
        roomService.setRoom(2, RoomType.STANDARD,  300);
        roomService.setRoom(3, RoomType.MASTER,  500);

        assertEquals(3, roomService.getRoomsCount());

        //update
        roomService.setRoom(2, RoomType.MASTER, 600);

        assertEquals(3, roomService.getRoomsCount());

        Room room = roomService.findRoomById(2);
        assertNotNull(room);

        assertEquals(RoomType.MASTER, room.getType());
        assertEquals(600, room.getPricePerNight());

        assertThrows(NoEntryFoundException.class, () -> roomService.findRoomById(4));

        roomService.printAllRooms();

    }

}
