package dao.custom.impl;

import dao.custom.CourseDAO;
import dao.generic.impl.HibernateGenericDAO;
import domain.archive.StudentCourseResult;
import domain.archive.StudentCourseResult_;
import domain.course.Course;
import domain.course.CourseInfo_;
import domain.course.Course_;
import domain.user.User;
import domain.user.User_;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class HibernateCourseDAO extends HibernateGenericDAO<Course> implements CourseDAO {

    public HibernateCourseDAO() {
        super(Course.class);
    }

    @Override
    public void saveOrUpdate(Course course) {
        Transaction tr = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tr = session.beginTransaction();
            session.saveOrUpdate(course);
            tr.commit();
        } catch (HibernateException e) {
            log.error("Error occurred while saving or updating course", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
    }


    @Override
    public List<Course> getCoursesById(Collection<Long> idCollection) {
        List<Course> courses = new ArrayList<>();
        Transaction tr = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Prepare
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Course> query = criteriaBuilder.createQuery(Course.class);
            Root<Course> course = query.from(Course.class);

            query.select(course)
                    .where(course.get(Course_.id).in(idCollection));

            // Executing query and saving result
            tr = session.beginTransaction();
            courses.addAll(session.createQuery(query).getResultList());
            tr.commit();
        } catch (PersistenceException e) {
            log.error("Error while getting courses by id", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return courses;
    }

    @Override
    public List<Course> getStudentNotSubscribedCoursesStartAfterDate(long userId, LocalDate date) {
        List<Course> courses = new ArrayList<>();
        Transaction tr = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Prepare
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Course> rootQuery = criteriaBuilder.createQuery(Course.class);
            Root<Course> courseRoot = rootQuery.from(Course.class);

            // Sub query to get user subscribed courses
            Subquery<Course> subQuery = rootQuery.subquery(Course.class);
            Root<Course> courseSubRoot = subQuery.from(Course.class);
            Join<Course, User> userSubJoin = courseSubRoot.join(Course_.teacherAndStudents);

            subQuery.select(courseSubRoot)
                    .where(criteriaBuilder.equal(userSubJoin.get(User_.id), userId));

            // Predicates for WHERE clause of root query
            Predicate predicateForUserNotSubscribedCourses = courseRoot.in(subQuery).not();
            Predicate predicateForDate = criteriaBuilder.greaterThan(
                    courseRoot.get(Course_.courseInfo).get(CourseInfo_.startDate), date);

            rootQuery.select(courseRoot)
                    .where(criteriaBuilder
                            .and(predicateForUserNotSubscribedCourses, predicateForDate));

            // Executing query and saving result
            tr = session.beginTransaction();
            courses.addAll(session.createQuery(rootQuery).getResultList());
            tr.commit();
        } catch (PersistenceException e) {
            log.error("Error while getting all courses started after given date", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return courses;
    }

    @Override
    public List<Course> getUserCoursesStartAfterDate(long userId, LocalDate date) {
        List<Course> courses = new ArrayList<>();
        Transaction tr = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Prepare
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Course> query = criteriaBuilder.createQuery(Course.class);
            Root<User> userRoot = query.from(User.class);
            Join<User, Course> courseJoin = userRoot.join(User_.courses);

            // Predicates for WHERE clause
            Predicate predicateForUserId = criteriaBuilder.equal(userRoot.get(User_.id), userId);
            Predicate predicateForDate = criteriaBuilder.greaterThan(
                    courseJoin.get(Course_.courseInfo).get(CourseInfo_.startDate), date);

            query.select(courseJoin)
                    .where(criteriaBuilder.and(predicateForUserId, predicateForDate));

            // Executing query and saving result
            tr = session.beginTransaction();
            courses.addAll(session.createQuery(query).getResultList());
            tr.commit();
        } catch (PersistenceException e) {
            log.error("Error while getting all courses started after given date", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return courses;
    }

    @Override
    public List<Course> getUserCoursesActiveOnDate(long userId, LocalDate date) {
        List<Course> courses = new ArrayList<>();
        Transaction tr = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Prepare
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Course> query = criteriaBuilder.createQuery(Course.class);
            Root<User> userRoot = query.from(User.class);
            Join<User, Course> courseJoin = userRoot.join(User_.courses);

            // Predicates for WHERE clause
            Predicate predicateForUserId = criteriaBuilder.equal(userRoot.get(User_.id), userId);
            Predicate predicateForStartDate = criteriaBuilder.lessThanOrEqualTo(
                    courseJoin.get(Course_.courseInfo).get(CourseInfo_.startDate), date);
            Predicate predicateForEndDate = criteriaBuilder.greaterThanOrEqualTo(
                    courseJoin.get(Course_.courseInfo).get(CourseInfo_.endDate), date);

            query.select(courseJoin)
                    .where(criteriaBuilder
                            .and(predicateForUserId, predicateForStartDate, predicateForEndDate));

            // Executing query and saving result
            tr = session.beginTransaction();
            courses.addAll(session.createQuery(query).getResultList());
            tr.commit();
        } catch (PersistenceException e) {
            log.error("Error while getting all courses started after given date", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return courses;
    }

    @Override
    public List<Course> getStudentUngradedCoursesEndedBeforeDate(long userId, LocalDate date) {
        List<Course> requiredCourses = new ArrayList<>();
        Transaction tr = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Prepare
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Course> rootQuery = criteriaBuilder.createQuery(Course.class);
            Root<User> userRoot = rootQuery.from(User.class);
            Join<User, Course> courseJoin = userRoot.join(User_.courses);

            // Sub query to get student graded courses
            Subquery<Course> subQuery = rootQuery.subquery(Course.class);
            Root<StudentCourseResult> scrSubRoot = subQuery.from(StudentCourseResult.class);

            // Predicate for sub query
            Predicate subPredicateForUserId = criteriaBuilder
                    .equal(scrSubRoot.get(StudentCourseResult_.student).get(User_.id), userId);

            subQuery.select(scrSubRoot.get(StudentCourseResult_.course))
                    .where(subPredicateForUserId);

            // Predicates for WHERE clause of root query
            Predicate predicateForUserId = criteriaBuilder.equal(userRoot.get(User_.id), userId);
            Predicate predicateForUserNotGradedCourses = courseJoin.in(subQuery).not();
            Predicate predicateForDate = criteriaBuilder
                    .lessThan(courseJoin.get(Course_.courseInfo).get(CourseInfo_.endDate), date);

            rootQuery.select(courseJoin)
                    .where(criteriaBuilder
                            .and(predicateForUserId, predicateForUserNotGradedCourses, predicateForDate));

            // Executing query and saving result
            tr = session.beginTransaction();
            requiredCourses.addAll(session.createQuery(rootQuery).getResultList());
            tr.commit();
        } catch (PersistenceException e) {
            log.error("Error while getting user ungraded courses ended before given date", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return requiredCourses;
    }

    @Override
    public List<Course> getTeacherNotGradedCoursesEndedBeforeDate(long userId, LocalDate date) {
        List<Course> requiredCourses = new ArrayList<>();
        Transaction tr = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Prepare
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Course> rootQuery = criteriaBuilder.createQuery(Course.class);
            Root<User> userRoot = rootQuery.from(User.class);
            Join<User, Course> courseJoin = userRoot.join(User_.courses);

            // Sub query to get all graded courses
            Subquery<Course> subQuery = rootQuery.subquery(Course.class);
            Root<StudentCourseResult> scrSubRoot = subQuery.from(StudentCourseResult.class);

            subQuery.select(scrSubRoot.get(StudentCourseResult_.course))
                    .distinct(true);

            // Predicates for WHERE clause of root query
            Predicate predicateForUserId = criteriaBuilder.equal(userRoot.get(User_.id), userId);
            Predicate predicateForUserNotGradedCourses = courseJoin.in(subQuery).not();
            Predicate predicateForDate = criteriaBuilder
                    .lessThan(courseJoin.get(Course_.courseInfo).get(CourseInfo_.endDate), date);

            rootQuery.select(courseJoin)
                    .where(criteriaBuilder
                            .and(predicateForUserId, predicateForUserNotGradedCourses, predicateForDate));

            // Executing query and saving result
            tr = session.beginTransaction();
            requiredCourses.addAll(session.createQuery(rootQuery).getResultList());
            tr.commit();
        } catch (PersistenceException e) {
            log.error("Error while getting user ungraded courses ended before given date", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return requiredCourses;
    }



}
