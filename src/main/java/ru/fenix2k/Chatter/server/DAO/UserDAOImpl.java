package ru.fenix2k.Chatter.server.DAO;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.fenix2k.Chatter.server.ClientWorker;
import ru.fenix2k.Chatter.server.Entity.User;
import ru.fenix2k.Chatter.server.Utils.HibernateUtil;

import java.time.LocalDateTime;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    private static final Logger log = Logger.getLogger(UserDAOImpl.class);

    @Override
    public User findById(Long id) {
        return HibernateUtil.getSessionFactory()
                .openSession()
                .get(User.class, id);
    }

    @Override
    public User findByLogin(String login) {
        return HibernateUtil.getSessionFactory()
                .openSession()
                .get(User.class, login);
    }

    @Override
    public User findByEmail(String email) {
        return HibernateUtil.getSessionFactory()
                .openSession()
                .get(User.class, email);
    }

    @Override
    public User save(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        user.setSys_created(LocalDateTime.now());
        session.saveOrUpdate(user);
        session.close();
        return user;
    }

    @Override
    public User update(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        user.setSys_modified(LocalDateTime.now());
        session.update(user);
        session.close();
        return user;
    }

    @Override
    public void remove(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        user.setSys_removed(true);
        session.update(user);
        session.close();
    }

    @Override
    public void delete(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.createQuery("delete User where id = " + id).executeUpdate();
        session.close();
    }

    @Override
    public List<User> findAll() {
        return findAll(0, 20);
    }

    @Override
    public List<User> findAll(int offset, int pageSize) {
        List<User> users = (List<User>) HibernateUtil
                .getSessionFactory()
                .openSession()
                .createQuery("from User")
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .list();
        return users;
    }


}
