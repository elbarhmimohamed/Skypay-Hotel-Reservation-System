package services;

import entities.Booking;
import entities.Room;
import entities.User;
import exceptions.InvalidBookingException;
import exceptions.NoEntryFoundException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static utils.Constants.BACK_TO_LINE;
import static utils.Constants.BOOKING_HEADER;
import static utils.Constants.SEPARATOR;
import static utils.Constants.WHITE_SPACE;

public class BookingService {

    UserService userService;
    RoomService roomService;

    public BookingService(UserService userService, RoomService roomService) {
        this.userService = userService;
        this.roomService = roomService;
    }

    List<Booking> bookings = new ArrayList<>();

    public List<Booking> getAllBooking(){
        return bookings;
    }
    public int getBookingCount(){
        return bookings.size();
    }

    public void bookRoom(int userId, int roomId, LocalDate checkIn, LocalDate checkOut){

        if(checkIn.isAfter(checkOut)){
            throw new InvalidBookingException("Invalid booking period: check-out cannot be before check-in.");
        }

        User user = userService.findUserById(userId);

        Room room = roomService.findRoomById(roomId);

        // room availability check
        boolean roomAvailable = isAvailableRoom(roomId, checkIn, checkOut);

        if(!roomAvailable){
            throw new InvalidBookingException(String.format(
                    "Room with id : %d will not be available for this period : %s -> %s", roomId, checkIn, checkOut));
        }

        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);

        BigDecimal totalPrice = BigDecimal.valueOf(room.getPricePerNight()).multiply(BigDecimal.valueOf(nights));

        int newBalance = user.getBalance() - totalPrice.intValue();

        if(newBalance < 0){
            throw new InvalidBookingException(String.format(
                    "Booking failed for user %d and room %d: insufficient balance.", userId, roomId));
        }

        Booking newBooking = new Booking(user, room, checkIn, checkOut, totalPrice);

        bookings.add(newBooking);
        user.setBalance(newBalance);

    }

    public void printAll(){

        if(bookings.isEmpty()){
            throw  new NoEntryFoundException("No Booking Found");
        }

        StringBuilder result = new StringBuilder(BOOKING_HEADER);

        for (int i = bookings.size() - 1 ; i >= 0 ; i--) {

            result.append(String.format("%-7s",bookings.get(i).getRoom().getId())).append(SEPARATOR);
            result.append(String.format("%-7s",bookings.get(i).getUser().getId())).append(SEPARATOR);
            result.append(String.format("%-9s",bookings.get(i).getRoom().getType())).append(SEPARATOR);
            result.append(String.format("%-11s",bookings.get(i).getRoom().getPricePerNight())).append(SEPARATOR);

            result.append(bookings.get(i).getCheckIn()).append(WHITE_SPACE).append(SEPARATOR);
            result.append(bookings.get(i).getCheckOut()).append(WHITE_SPACE).append(SEPARATOR);
            result.append(bookings.get(i).getTotalAmount()).append(WHITE_SPACE).append(BACK_TO_LINE);

        }

        System.out.println(result.toString());
    }

    private boolean isAvailableRoom(int roomId, LocalDate checkIn, LocalDate checkOut){

        return bookings.stream()
                .filter( r -> (r.getRoom() != null && r.getRoom().getId() == roomId))
                .allMatch(
                        r -> checkOut.isBefore(r.getCheckIn()) ||
                                checkIn.isAfter(r.getCheckOut())
        );
    }



}
