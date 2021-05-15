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
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class HibernateCourseDAO extends HibernateGenericDAO<Course> implements CourseDAO {

    public HibernateCourseDAO() {
        super(Course.class);
    }

    @Override
    public List<Course> getTeacherUngradedCoursesEndedBeforeDate(long userId, LocalDate date) {
        return null;
    }

    public List<Course> getStudentNotSubscribedCoursesStartAfterDate(long userId, LocalDate date) {
        List<Course> courses = new ArrayList<>();
        Transaction tr = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Prepare
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Course> rootQuery = criteriaBuilder.createQuery(Course.class);
            Root<Course> course = rootQuery.from(Course.class);

            // Sub query to get user graded courses
            Subquery<Course> subQuery = rootQuery.subquery(Course.class);
            Root<StudentCourseResult> scr = subQuery.from(StudentCourseResult.class);
            Join<StudentCourseResult, User> user = scr.join(StudentCourseResult_.student);

            subQuery.select(scr.get(StudentCourseResult_.course))
                    .where(criteriaBuilder.equal(user.get(User_.id), userId));

            // Predicates for WHERE clause
            Predicate predicateForUserId = course.in(subQuery).not();
            Predicate predicateForDate = criteriaBuilder.greaterThan(
                    course.get(Course_.courseInfo).get(CourseInfo_.startDate), date);

            rootQuery.select(course)
                    .where(criteriaBuilder.and(predicateForUserId, predicateForDate));

            // Executing query and saving result
            tr = session.beginTransaction();
            courses.addAll(session.createQuery(rootQuery).getResultList());
            tr.commit();
        } catch (PersistenceException e) {
            log.error("Error while getting all courses started after given date");
            e.printStackTrace();
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
            Join<User, Course> course = userRoot.join(User_.courses);

            // Predicates for WHERE clause
            Predicate predicateForUserId = criteriaBuilder.equal(userRoot.get(User_.id), userId);
            Predicate predicateForDate = criteriaBuilder.greaterThan(
                    course.get(Course_.courseInfo).get(CourseInfo_.startDate), date);

            query.select(course)
                    .where(criteriaBuilder.and(predicateForUserId, predicateForDate));

            // Executing query and saving result
            tr = session.beginTransaction();
            courses.addAll(session.createQuery(query).getResultList());
            tr.commit();
        } catch (PersistenceException e) {
            log.error("Error while getting all courses started after given date");
            e.printStackTrace();
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
            Join<User, Course> course = userRoot.join(User_.courses);

            // Predicates for WHERE clause
            Predicate predicateForUserId = criteriaBuilder.equal(userRoot.get(User_.id), userId);
            Predicate predicateForStartDate = criteriaBuilder.lessThanOrEqualTo(
                    course.get(Course_.courseInfo).get(CourseInfo_.startDate), date);
            Predicate predicateForEndDate = criteriaBuilder.greaterThanOrEqualTo(
                    course.get(Course_.courseInfo).get(CourseInfo_.endDate), date);

            query.select(course)
                    .where(criteriaBuilder
                            .and(predicateForUserId, predicateForStartDate, predicateForEndDate));

            // Executing query and saving result
            tr = session.beginTransaction();
            courses.addAll(session.createQuery(query).getResultList());
            tr.commit();
        } catch (PersistenceException e) {
            log.error("Error while getting all courses started after given date");
            e.printStackTrace();
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
            Root<Course> courseRoot = rootQuery.from(Course.class);

            // Sub query to get user graded courses
            Subquery<Course> subQuery = rootQuery.subquery(Course.class);
            Root<StudentCourseResult> scr = subQuery.from(StudentCourseResult.class);

            // Predicate for sub query
            Predicate predicateForUserId = criteriaBuilder
                    .equal(scr.get(StudentCourseResult_.student).get(User_.id), userId);

            subQuery.select(scr.get(StudentCourseResult_.course))
                    .where(predicateForUserId);

            // Predicates for WHERE clause in root query
            Predicate predicateForUserCourses = courseRoot.in(subQuery).not();
            Predicate predicateForDate = criteriaBuilder
                    .lessThan(courseRoot.get(Course_.courseInfo).get(CourseInfo_.endDate), date);

            rootQuery.select(courseRoot)
                    .where(criteriaBuilder.and(predicateForUserCourses, predicateForDate));

            // Executing query and saving result
            tr = session.beginTransaction();
            requiredCourses.addAll(session.createQuery(rootQuery).getResultList());
            tr.commit();
        } catch (PersistenceException e) {
            log.error("Error during getting user ungraded courses ended before given date");
            e.printStackTrace();
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return requiredCourses;
    }

    @Override
    public List<Course> getAllById(List<Long> idList) {
        List<Course> courses = new ArrayList<>();
        Transaction tr = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Prepare
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Course> query = criteriaBuilder.createQuery(Course.class);
            Root<Course> root = query.from(Course.class);

            query.select(root).where(root.<Long>get(Course_.ID).in(idList));

            // Executing query and saving result
            tr = session.beginTransaction();
            courses.addAll(session.createQuery(query).getResultList());
            tr.commit();
        } catch (PersistenceException e) {
            log.error("Error while getting all courses by id");
            e.printStackTrace();
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return courses;
    }



    @Override
    public List<Course> getAllStartedAfterDate(LocalDate date) {
        List<Course> courses = new ArrayList<>();
        Transaction tr = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tr = session.beginTransaction();

            /*// Fetch all courses started after given date
            String hqlQuery = "FROM Course crs WHERE crs.courseInfo.startDate > :date";

            Query<Course> query = session.createQuery(hqlQuery);
            query.setParameter("date", date, TemporalType.DATE);*/


            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

            CriteriaQuery<Course> query = criteriaBuilder.createQuery(Course.class);
            Root<Course> root = query.from(Course.class);

            query.select(root)
                    .where(criteriaBuilder.greaterThan(
                            root.<LocalDate>get(Course_.COURSE_INFO).get(CourseInfo_.START_DATE), date));

            courses.addAll(session.createQuery(query).getResultList());

            tr.commit();
        } catch (PersistenceException e) {
            log.error("Error while getting all courses started after given date");
            e.printStackTrace();
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return courses;
    }
}
