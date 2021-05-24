package ru.fenix2k.Chatter.server;

import org.junit.jupiter.api.BeforeAll;
import ru.fenix2k.Chatter.server.Entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServerTest {

    private static List<User> users = new ArrayList<>();

    @BeforeAll
    public static void setUp() {
        LocalDateTime dateTime = LocalDateTime.now();
        users.add(new User(123, "admin", null, "admin@mail.ru", dateTime, dateTime, true, true));
        users.add(new User(0, "", null, "admin@mail.ru", dateTime, dateTime, true, true));
        users.add(new User(123, "admin", null, "admail.ru", dateTime, dateTime, true, true));
        users.add(new User(0, "a", null, "admin@mail", dateTime, dateTime, true, true));
    }

    /*@Test
    public void convertUserToUserView() {
        for (User user : users) {
            try {
                UserView userView = UserService.convertUserToUserView(user);
                Assertions.assertNotNull(userView);
            } catch (EntityValidationException ex) {
                System.out.println(ex.getValidationErrors());
            }
        }
    }

    @Test
    public void convertUserViewToUser() {
        for (UserView userView : userViews) {
            try {
                User user = UserService.convertUserViewToUser(userView);
                Assertions.assertNotNull(user);
            } catch (EntityValidationException ex) {
                System.out.println(ex.getValidationErrors());
            }
        }
    }*/

}
