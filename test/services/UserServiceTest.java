package services;

import entities.User;
import exceptions.NoEntryFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceTest {

    private UserService userService = new UserService();


    @Test
    void createAndUpdateUserTest(){

        // create
        userService.setUser(1, 1000);
        userService.setUser(2, 500);
        userService.setUser(3, 900);

        assertEquals(3, userService.getUsersCount());

        //update
        userService.setUser(2, 1200);

        assertEquals(3, userService.getUsersCount());

        User user = userService.findUserById(2);
        assertNotNull(user);

        assertEquals(1200, user.getBalance());

        assertThrows(NoEntryFoundException.class, () -> userService.findUserById(4));

        userService.printAllUsers();

    }

}
