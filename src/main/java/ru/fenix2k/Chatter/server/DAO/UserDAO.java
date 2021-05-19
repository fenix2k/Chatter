package ru.fenix2k.Chatter.server.DAO;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.fenix2k.Chatter.server.ClientWorker;
import ru.fenix2k.Chatter.server.Entity.User;
import ru.fenix2k.Chatter.server.Utils.HibernateUtil;

import java.util.List;

public class UserDAO {
    private static final Logger log = Logger.getLogger(ClientWorker.class);

    public UserDAO() {
    }

    public User findById(Long id) {
        return HibernateUtil.getSessionFactory()
                .openSession()
                .get(User.class, id);
    }

    public User findByLogin(String login) {
        return HibernateUtil.getSessionFactory()
                .openSession()
                .get(User.class, login);
    }

    public User findByEmail(String email) {
        return HibernateUtil.getSessionFactory()
                .openSession()
                .get(User.class, email);
    }

    public User save(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(user);
        transaction.commit();
        session.close();
        return user;
    }

    public void update(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(user);
        transaction.commit();
        session.close();
    }

    public void delete(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(user);
        transaction.commit();
        session.close();
    }

    public List<User> findAll() {
        List<User> users = (List<User>) HibernateUtil
                .getSessionFactory()
                .openSession()
                .createQuery("from User").list();
        return users;
    }


}
