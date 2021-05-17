package dao.custom.impl;

import dao.custom.AccessInfoDAO;
import dao.generic.impl.HibernateGenericDAO;
import domain.user.AccessInfo;
import domain.user.AccessInfo_;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
            // Prepare
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<AccessInfo> query = criteriaBuilder.createQuery(AccessInfo.class);
            Root<AccessInfo> root = query.from(AccessInfo.class);

            // Predicate for WHERE clause
            Predicate predicateForEmail =
                    criteriaBuilder.equal(root.get(AccessInfo_.email), email);

            query.select(root)
                    .where(predicateForEmail);

            // Executing query and saving result
            tr = session.beginTransaction();
            try {
                accessInfo = session.createQuery(query).getSingleResult();
            } catch (NoResultException e) {
                log.debug("User wasn't found by given email and password hash");
            }
            tr.commit();
        } catch (PersistenceException e) {
            log.error("Error occurred during finding access info by email", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return Optional.ofNullable(accessInfo);
    }
}
