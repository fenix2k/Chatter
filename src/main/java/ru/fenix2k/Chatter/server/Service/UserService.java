package ru.fenix2k.Chatter.server.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import org.apache.log4j.Logger;
import ru.fenix2k.Chatter.server.DAO.UserDAO;
import ru.fenix2k.Chatter.server.DAO.UserDAOImpl;
import ru.fenix2k.Chatter.server.Entity.User;
import ru.fenix2k.Chatter.server.View.ValidationErrorView;
import ru.fenix2k.Chatter.server.Exception.EntityValidationException;
import ru.fenix2k.Chatter.server.Utils.BeanUtil;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserService {
    private static final Logger log = Logger.getLogger(UserService.class);
    private final UserDAO userDAO;
    /** JSON Mapper */
    private ObjectMapper jsonMapper = new ObjectMapper();

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

    public User findById(Long id) {
        return userDAO.findById(id);
    }

    public User findByLogin(String login) {
        return userDAO.findByLogin(login);
    }

    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    public User save(User user) throws EntityValidationException {
        return userDAO.save(validate(user));
    }

    public User update(User user) throws EntityValidationException {
        return userDAO.update(validate(user));
    }

    public void remove(User user) throws EntityValidationException {
        userDAO.remove(validate(user));
    }

    public List<User> findAll() {
        return userDAO.findAll();
    }

}
