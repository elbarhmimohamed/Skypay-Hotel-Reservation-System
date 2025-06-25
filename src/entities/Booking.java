package entities;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


@Getter
@Setter
public class Booking {

    private String reference;

    private User user;

    private Room room;

    private LocalDate checkIn;

    private LocalDate checkOut;

    private BigDecimal totalAmount;

    public Booking(User user, Room room, LocalDate checkIn, LocalDate checkOut, BigDecimal totalAmount) {

        this.reference = UUID.randomUUID().toString();
        this.user = new User(user);
        this.room = new Room(room);
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalAmount = totalAmount;
    }
}
