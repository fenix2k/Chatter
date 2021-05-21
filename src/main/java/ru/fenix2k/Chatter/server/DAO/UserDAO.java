package ru.fenix2k.Chatter.server.DAO;

import ru.fenix2k.Chatter.server.Entity.User;

import java.util.List;

public interface UserDAO {

    User findById(Long id);

    User findByLogin(String login);

    User findByEmail(String email);

    User save(User user);

    User update(User user);

    void remove(User user);

    void delete(Long id);

    List<User> findAll();

    List<User> findAll(int offset, int pageSize);
}
