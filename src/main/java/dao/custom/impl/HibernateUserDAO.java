package dao.custom.impl;

import dao.custom.UserDAO;
import dao.generic.impl.HibernateGenericDAO;
import domain.archive.StudentCourseResult;
import domain.archive.StudentCourseResult_;
import domain.course.Course;
import domain.course.Course_;
import domain.user.AccessInfo;
import domain.user.AccessInfo_;
import domain.user.User;
import domain.user.User_;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
public class HibernateUserDAO extends HibernateGenericDAO<User> implements UserDAO {

    public HibernateUserDAO() {
        super(User.class);
    }

    @Override
    public void updateUsers(Collection<User> users) {
        Transaction tr = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tr = session.beginTransaction();
            users.forEach(session::update);
            tr.commit();
        } catch (HibernateException e) {
            log.error("Error occurred during updating users", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
    }

    @Override
    public Optional<User> findUserByEmailAndPasswordHash(String email, int passwordHash) {
        User requiredUser = null;
        Transaction tr = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Prepare
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
            Root<User> user = query.from(User.class);

            // Predicates for WHERE clause
            Predicate predicateForEmail =
                    criteriaBuilder.equal(user.get(User_.accessInfo).get(AccessInfo_.email), email);
            Predicate predicateForPasswordHash =
                    criteriaBuilder.equal(user.get(User_.accessInfo).get(AccessInfo_.passwordHash), passwordHash);

            query.select(user)
                    .where(criteriaBuilder.and(predicateForEmail, predicateForPasswordHash));

            // Executing query and saving result
            tr = session.beginTransaction();
            try {
                requiredUser = session.createQuery(query).getSingleResult();
            } catch (NoResultException e) {
                log.debug("User wasn't found by given email and password hash");
            }
            tr.commit();
        } catch (PersistenceException e) {
            log.error("Error occurred during finding user by email and password hash", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return Optional.ofNullable(requiredUser);
    }

    @Override
    public List<User> getUsersById(Collection<Long> usersId) {
        List<User> users = new ArrayList<>();
        Transaction tr = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Prepare
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
            Root<User> course = query.from(User.class);

            query.select(course)
                    .where(course.get(User_.id).in(usersId));

            // Executing query and saving result
            tr = session.beginTransaction();
            users.addAll(session.createQuery(query).getResultList());
            tr.commit();
        } catch (PersistenceException e) {
            log.error("Error while getting courses by id", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return users;

    }

}
