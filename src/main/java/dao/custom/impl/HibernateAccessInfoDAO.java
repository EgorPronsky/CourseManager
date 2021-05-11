package dao.custom.impl;

import dao.custom.AccessInfoDAO;
import dao.generic.impl.HibernateGenericDAO;
import domain.user.AccessInfo;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.util.Optional;

@Slf4j
public class HibernateAccessInfoDAO extends HibernateGenericDAO<AccessInfo> implements AccessInfoDAO {

    public HibernateAccessInfoDAO() {
        super(AccessInfo.class);
    }

    @Override
    public Optional<AccessInfo> findAccessInfoByEmail(String email) {
        AccessInfo accessInfo = null;
        Transaction tr = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tr = session.beginTransaction();

            // Fetch access info by email
            String hqlQuery = "FROM AccessInfo inf WHERE inf.email=:email";

            Query<AccessInfo> query = session.createQuery(hqlQuery);
            query.setParameter("email", email);

            // Try to find access info
            try {
                accessInfo = (AccessInfo) query.getSingleResult();
            } catch (Exception e) {
                log.debug("Access info wasn't found");
            }
            tr.commit();
        } catch (Exception e) {
            log.error("Error occurred while finding access info by email");
            e.printStackTrace();
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return Optional.ofNullable(accessInfo);
    }

    @Override
    public Optional<AccessInfo> findAccessInfoByEmailAndPasswordHash(String email, int passwordHash) {
        AccessInfo usersAccessInfo = null;
        Transaction tr = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tr = session.beginTransaction();

            // Fetch access info by email and password hash
            String hqlQuery = "FROM AccessInfo inf WHERE inf.email=:email AND inf.passwordHash=:password_hash";

            Query<AccessInfo> query = session.createQuery(hqlQuery);
            query.setParameter("email", email);
            query.setParameter("password_hash", passwordHash);

            // Try to find access info
            try {
                usersAccessInfo = (AccessInfo) query.getSingleResult();
            } catch (Exception e) {
                log.debug("Access info wasn't found");
            }
            tr.commit();
        } catch (Exception e) {
            log.error("Error occurred while finding access info by email and password hash");
            e.printStackTrace();
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return Optional.ofNullable(usersAccessInfo);
    }
}
