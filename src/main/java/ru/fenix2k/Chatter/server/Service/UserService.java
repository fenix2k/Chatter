package ru.fenix2k.Chatter.server.Service;

import jakarta.validation.ConstraintViolation;
import org.apache.log4j.Logger;
import ru.fenix2k.Chatter.server.DAO.UserDAO;
import ru.fenix2k.Chatter.server.DAO.UserDAOImpl;
import ru.fenix2k.Chatter.server.Entity.User;
import ru.fenix2k.Chatter.server.EntityView.UserView;
import ru.fenix2k.Chatter.server.EntityView.ValidationErrorView;
import ru.fenix2k.Chatter.server.Exception.EntityValidationException;
import ru.fenix2k.Chatter.server.Utils.BeanUtil;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserService {
    private static final Logger log = Logger.getLogger(UserService.class);
    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAOImpl();
    }

    public static <T> T validate(T entity) throws EntityValidationException {
        Set<ConstraintViolation<T>> violations = BeanUtil
                .getValidatorFactory().getValidator().validate(entity);
        if (violations == null || !violations.isEmpty()) {
            Set<ValidationErrorView> errors = violations.stream()
                    .map(val -> new ValidationErrorView(val.getPropertyPath().toString(), val.getMessage().toString()))
                    .collect(Collectors.toSet());
            throw new EntityValidationException("", errors);
        }
        return entity;
    }

    public static UserView convertUserToUserView(User user) throws EntityValidationException {
        return validate(UserView.builder()
                .id(user.getId())
                .login(user.getLogin())
                .email(user.getEmail())
                .dtLastLogin(user.getDtLastLogin())
                .dtRegister(user.getDtRegister())
                .isActive(user.getIsActive())
                .isVisible(user.getIsVisible())
                .build());
    }

    public static User convertUserViewToUser(UserView userView) throws EntityValidationException {
        return validate(User.builder()
                .id(userView.getId())
                .login(userView.getLogin())
                .email(userView.getEmail())
                .dtLastLogin(userView.getDtLastLogin())
                .dtRegister(userView.getDtRegister())
                .isActive(userView.getIsActive())
                .isVisible(userView.getIsVisible())
                .build());
    }

    public UserView findById(Long id) throws EntityValidationException {
        return convertUserToUserView(userDAO.findById(id));
    }

    public UserView findByLogin(String login) throws EntityValidationException {
        return convertUserToUserView(userDAO.findByLogin(login));
    }

    public UserView findByEmail(String email) throws EntityValidationException {
        return convertUserToUserView(userDAO.findByEmail(email));
    }

    public UserView save(UserView userView) throws EntityValidationException {
        return convertUserToUserView(userDAO.save(convertUserViewToUser(userView)));
    }

    public UserView update(UserView userView) throws EntityValidationException {
        return convertUserToUserView(userDAO.update(convertUserViewToUser(userView)));
    }

    public void remove(UserView userView) {
        userDAO.remove(convertUserViewToUser(userView));
    }

    public List<UserView> findAll() {
        return userDAO
                .findAll().stream()
                .map(UserService::convertUserToUserView)
                .collect(Collectors.toList());
    }
}
