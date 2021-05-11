package dao.custom.impl;

import dao.custom.UserDAO;
import dao.generic.impl.HibernateGenericDAO;
import domain.user.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.util.Optional;

@Slf4j
public class HibernateUserDAO extends HibernateGenericDAO<User> implements UserDAO {

    public HibernateUserDAO() {
        super(User.class);
    }

    @Override
    public Optional<User> findUserByAccessInfoId(long accessInfoId) {
        User user = null;
        Transaction tr = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tr = session.beginTransaction();

            // Find user by access info id
            String hqlQuery = "FROM User us WHERE us.accessInfo.id=:access_info_id";

            Query<User> query = session.createQuery(hqlQuery);
            query.setParameter("access_info_id", accessInfoId);

            user = (User) query.getSingleResult();

            tr.commit();
        } catch (Exception e) {
            log.error("Error occurred while finding user by access info id");
            e.printStackTrace();
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return Optional.ofNullable(user);
    }

}
