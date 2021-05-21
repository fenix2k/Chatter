package ru.fenix2k.Chatter.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.fenix2k.Chatter.server.Entity.User;
import ru.fenix2k.Chatter.server.EntityView.UserView;
import ru.fenix2k.Chatter.server.Exception.EntityValidationException;
import ru.fenix2k.Chatter.server.Service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServerTest {

    private static List<User> users = new ArrayList<>();
    private static List<UserView> userViews = new ArrayList<>();

    @BeforeAll
    public static void setUp() {
        LocalDateTime dateTime = LocalDateTime.now();
        users.add(new User(123, "admin", null, "admin@mail.ru", dateTime, dateTime, true, true));
        users.add(new User(0, "", null, "admin@mail.ru", dateTime, dateTime, true, true));
        users.add(new User(123, "admin", null, "admail.ru", dateTime, dateTime, true, true));
        users.add(new User(0, "a", null, "admin@mail", dateTime, dateTime, true, true));
        userViews.add(new UserView(0, "admin", "admin@mail.ru", dateTime, dateTime, true, true));
        userViews.add(new UserView(123, "a", "admin@mail", dateTime, dateTime, true, true));
        userViews.add(new UserView(123, "admin", "adminmail.ru", dateTime, dateTime, true, true));
        userViews.add(new UserView(123, "admn", "ad@mail.ru", dateTime, dateTime, true, true));
    }

    @Test
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
    }

}
