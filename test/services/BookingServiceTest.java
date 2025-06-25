package services;


import entities.RoomType;
import entities.User;
import exceptions.InvalidBookingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BookingServiceTest {

    private UserService userService = new UserService();
    private RoomService roomService = new RoomService();
    private BookingService bookingService = new BookingService(userService, roomService);

    @BeforeEach
    void setUp(){
        prepareData();
    }

    void prepareData(){

        userService.setUser(1, 5000);
        userService.setUser(2, 10000);

        roomService.setRoom(1, RoomType.JUNIOR, 1000);
        roomService.setRoom(2, RoomType.STANDARD, 2000);
        roomService.setRoom(3, RoomType.MASTER, 3000);

    }

    @Test
    void bookRoomTest(){

        assertEquals(2, userService.getUsersCount());
        assertEquals(3, roomService.getRoomsCount());

        // 1- create booking - simple test
        bookingService.bookRoom(1, 2, LocalDate.of(2025, 6,30),
                LocalDate.of(2025, 7,2));

        assertEquals( BigDecimal.valueOf(4000), bookingService.getAllBooking().get(0).getTotalAmount());

        User user1 = userService.findUserById(1);
        assertEquals(1000, user1.getBalance());

        bookingService.printAll();
    }

    @Test
    void bookRoomTest_InvalidCheckRoomAvailability(){

        userService.setUser(3, 3000);

        assertEquals(3, userService.getUsersCount());
        assertEquals(3, roomService.getRoomsCount());

        bookingService.bookRoom(1, 1, LocalDate.of(2025, 7,1),
                LocalDate.of(2025, 7,3));

        assertEquals(1, bookingService.getBookingCount());

        // invalid checkIn & checkOut
        assertThrows(InvalidBookingException.class,
                () -> bookingService.bookRoom(3, 1, LocalDate.of(2025, 7,5),
                LocalDate.of(2025, 7,3)));

        // room not available -> already booked on this period
        assertThrows(InvalidBookingException.class,
                () -> bookingService.bookRoom(3, 2, LocalDate.of(2025, 7,2),
                        LocalDate.of(2025, 7,6)));

        // insufficient balance -> totalAmount > current balance
        assertThrows(InvalidBookingException.class,
                () -> bookingService.bookRoom(3, 3, LocalDate.of(2025, 8,3),
                        LocalDate.of(2025, 8,10)));

    }

    @Test
    void bookRoomTest_ExerciseTestCase(){

        assertEquals(2, userService.getUsersCount());
        assertEquals(3, roomService.getRoomsCount());

        // User 1 tries booking Room 2 from 30/06/2026 to 07/07/2026
        assertThrows(InvalidBookingException.class, () -> bookingService.bookRoom(1, 2,
                LocalDate.of(2026, 6,30),
                LocalDate.of(2026, 7,7))
        );

        //User 1 tries booking Room 2 from 07/07/2026 to 30/06/2026.
        assertThrows(InvalidBookingException.class, () -> bookingService.bookRoom(1, 2,
                LocalDate.of(2026, 7,7),
                LocalDate.of(2026, 6,30))
        );


        //User 1 tries booking Room 1 from 07/07/2026 to 08/07/2026 (1 night).
        bookingService.bookRoom(1, 1, LocalDate.of(2026, 7,7),
                LocalDate.of(2026, 7,8));

        //User 2 tries booking Room 1 from 07/07/2026 to 09/07/2026 (2 nights).
        assertThrows(InvalidBookingException.class, () -> bookingService.bookRoom(2, 1,
                LocalDate.of(2026, 7,7),
                LocalDate.of(2026, 7,9))
        );

        //User 2 tries booking Room 3 from 07/07/2026 to 08/07/2026 (1 night).
        bookingService.bookRoom(2, 3, LocalDate.of(2026, 7,7),
                LocalDate.of(2026, 7,8));

        roomService.setRoom(1, RoomType.MASTER, 10000);

        System.out.println("Print All Users :\n");
        userService.printAllUsers();

        System.out.println("Print All Rooms :\n");
        roomService.printAllRooms();

        System.out.println("Print All Booking:\n");
        bookingService.printAll();
    }

}
