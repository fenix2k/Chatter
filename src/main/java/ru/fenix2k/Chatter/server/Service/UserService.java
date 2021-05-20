package ru.fenix2k.Chatter.server.Service;

import org.apache.log4j.Logger;
import ru.fenix2k.Chatter.server.DAO.UserDAO;
import ru.fenix2k.Chatter.server.DAO.UserDAOImpl;
import ru.fenix2k.Chatter.server.Entity.User;
import ru.fenix2k.Chatter.server.EntityView.UserView;

import java.util.List;
import java.util.stream.Collectors;

public class UserService {
    private static final Logger log = Logger.getLogger(UserService.class);
    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAOImpl();
    }

    public static UserView convertUserToUserView(User user) {
        return UserView.builder()
                .id(user.getId())
                .login(user.getLogin())
                .email(user.getEmail())
                .dtLastLogin(user.getDtLastLogin())
                .dtRegister(user.getDtRegister())
                .isActive(user.isActive())
                .isVisible(user.isVisible())
                .build();
    }

    public static User convertUserViewToUser(UserView userView) {
        return User.builder()
                .id(userView.getId())
                .login(userView.getLogin())
                .email(userView.getEmail())
                .dtLastLogin(userView.getDtLastLogin())
                .dtRegister(userView.getDtRegister())
                .isActive(userView.isActive())
                .isVisible(userView.isVisible())
                .build();
    }

    public UserView findById(Long id) {
        return convertUserToUserView(userDAO.findById(id));
    }

    public UserView findByLogin(String login) {
        return convertUserToUserView(userDAO.findByLogin(login));
    }

    public UserView findByEmail(String email) {
        return convertUserToUserView(userDAO.findByEmail(email));
    }

    public UserView save(UserView userView) {
        return convertUserToUserView(userDAO.save(convertUserViewToUser(userView)));
    }

    public void update(UserView userView) {

    }

    public void delete(UserView userView) {
        userDAO.delete(userView.getId());
    }

    public List<UserView> findAll() {
        return userDAO
                .findAll().stream()
                .map(UserService::convertUserToUserView)
                .collect(Collectors.toList());
    }
}
