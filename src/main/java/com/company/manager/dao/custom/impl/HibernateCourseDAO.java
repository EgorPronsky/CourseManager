package com.company.manager.dao.custom.impl;

import com.company.manager.dao.generic.impl.HibernateGenericDAO;
import com.company.manager.dao.custom.CourseDAO;
import com.company.manager.domain.archive.StudentCourseResult;
import com.company.manager.domain.archive.StudentCourseResult_;
import com.company.manager.domain.course.Course;
import com.company.manager.domain.course.CourseInfo_;
import com.company.manager.domain.course.Course_;
import com.company.manager.domain.user.User_;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.PersistenceException;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Slf4j
public class HibernateCourseDAO extends HibernateGenericDAO<Course> implements CourseDAO {

    public HibernateCourseDAO(SessionFactory sessionFactory) {
        super(Course.class, sessionFactory);
    }

    @Override
    public void saveOrUpdate(Course course) {
        Transaction tr = null;
        try (Session session = super.sessionFactory.openSession()) {
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
        List<Course> courses = null;
        Transaction tr = null;

        try (Session session = super.sessionFactory.openSession()) {
            // Prepare
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Course> query = criteriaBuilder.createQuery(Course.class);
            Root<Course> course = query.from(Course.class);

            query.select(course)
                    .where(course.get(Course_.id).in(idCollection));

            // Executing query and saving result
            tr = session.beginTransaction();
            courses = session.createQuery(query).getResultList();
            tr.commit();
        } catch (PersistenceException e) {
            log.error("Error while getting courses by id", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return courses;
    }

    @Override
    public List<Course> getStudentCoursesStartAfterDate(long studentId, LocalDate date) {
        List<Course> courses = null;
        Transaction tr = null;

        try (Session session = super.sessionFactory.openSession()) {
            // Prepare
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Course> query = criteriaBuilder.createQuery(Course.class);
            Root<Course> courseRoot = query.from(Course.class);

            // Joining SCR where student id == required student id
            Join<Course, StudentCourseResult> scrJoin = courseRoot.join(Course_.studentResults);
            scrJoin.on(criteriaBuilder.equal(
                    scrJoin.get(StudentCourseResult_.student).get(User_.id), studentId));

            Predicate predicateForDate = criteriaBuilder.greaterThan(
                    courseRoot.get(Course_.courseInfo).get(CourseInfo_.startDate), date);

            query.select(courseRoot)
                    .where(predicateForDate)
                    .orderBy(criteriaBuilder.asc(
                            courseRoot.get(Course_.courseInfo).get(CourseInfo_.startDate)));

            // Executing query and saving result
            tr = session.beginTransaction();
            courses = session.createQuery(query).getResultList();
            tr.commit();
        } catch (PersistenceException e) {
            log.error("Error while getting student courses that start after given date", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return courses;
    }

    @Override
    public List<Course> getStudentCoursesActiveOnDate(long studentId, LocalDate date) {
        List<Course> courses = null;
        Transaction tr = null;

        try (Session session = super.sessionFactory.openSession()) {
            // Prepare
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Course> query = criteriaBuilder.createQuery(Course.class);
            Root<Course> courseRoot = query.from(Course.class);

            // Joining SCR where student id == required student id
            Join<Course, StudentCourseResult> scrJoin = courseRoot.join(Course_.studentResults);
            scrJoin.on(criteriaBuilder
                    .equal(scrJoin.get(StudentCourseResult_.student).get(User_.id), studentId));

            // Date predicates
            Predicate predicateForStartDate = criteriaBuilder.lessThanOrEqualTo(
                    courseRoot.get(Course_.courseInfo).get(CourseInfo_.startDate), date);
            Predicate predicateForEndDate = criteriaBuilder.greaterThanOrEqualTo(
                    courseRoot.get(Course_.courseInfo).get(CourseInfo_.endDate), date);

            query.select(courseRoot)
                    .where(criteriaBuilder.and(predicateForStartDate, predicateForEndDate))
                    .orderBy(criteriaBuilder.asc((
                            courseRoot.get(Course_.courseInfo).get(CourseInfo_.endDate))));

            // Executing query and saving result
            tr = session.beginTransaction();
            courses = session.createQuery(query).getResultList();
            tr.commit();
        } catch (PersistenceException e) {
            log.error("Error while getting student courses active on given date", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return courses;
    }

    @Override
    public List<Course> getStudentNotSubscribedCoursesStartAfterDate(long studentId, LocalDate date) {
        List<Course> courses = null;
        Transaction tr = null;

        try (Session session = super.sessionFactory.openSession()) {
            // Prepare
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Course> rootQuery = criteriaBuilder.createQuery(Course.class);
            Root<Course> courseRoot = rootQuery.from(Course.class);

            // Sub query to get student subscribed courses
            Subquery<Course> subQuery = rootQuery.subquery(Course.class);
            Root<Course> courseSubRoot = subQuery.from(Course.class);

            // Sub query joining SCR to get student SUBSCRIBED courses
            Join<Course, StudentCourseResult> scrSubJoin = courseSubRoot.join(Course_.studentResults);
            scrSubJoin.on(criteriaBuilder.equal(
                    scrSubJoin.get(StudentCourseResult_.student).get(User_.id), studentId));

            subQuery.select(courseSubRoot);

            // Predicates for WHERE clause of root query
            Predicate predicateForUserNotSubscribedCourses = courseRoot.in(subQuery).not();
            Predicate predicateForDate = criteriaBuilder.greaterThan(
                    courseRoot.get(Course_.courseInfo).get(CourseInfo_.startDate), date);

            rootQuery.select(courseRoot)
                    .where(criteriaBuilder
                            .and(predicateForUserNotSubscribedCourses, predicateForDate))
                    .orderBy(criteriaBuilder.asc(
                            courseRoot.get(Course_.courseInfo).get(CourseInfo_.startDate)));

            // Executing query and saving result
            tr = session.beginTransaction();
            courses = session.createQuery(rootQuery).getResultList();
            tr.commit();
        } catch (PersistenceException e) {
            log.error("Error while getting student not subscribed courses that start after given date", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return courses;
    }

    @Override
    public List<Course> getTeacherCoursesStartAfterDate(long teacherId, LocalDate date) {
        List<Course> courses = null;
        Transaction tr = null;

        try (Session session = super.sessionFactory.openSession()) {
            // Prepare
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Course> query = criteriaBuilder.createQuery(Course.class);
            Root<Course> courseRoot = query.from(Course.class);

            // Predicates for WHERE clause
            Predicate predicateForUserId = criteriaBuilder
                    .equal(courseRoot.get(Course_.teacher).get(User_.id), teacherId);
            Predicate predicateForDate = criteriaBuilder.greaterThan(
                    courseRoot.get(Course_.courseInfo).get(CourseInfo_.startDate), date);

            query.select(courseRoot)
                    .where(criteriaBuilder.and(predicateForUserId, predicateForDate))
                    .orderBy(criteriaBuilder.asc(
                            courseRoot.get(Course_.courseInfo).get(CourseInfo_.startDate)));

            // Executing query and saving result
            tr = session.beginTransaction();
            courses = session.createQuery(query).getResultList();
            tr.commit();
        } catch (PersistenceException e) {
            log.error("Error while getting teacher courses that start after given date", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return courses;
    }

    @Override
    public List<Course> getTeacherCoursesActiveOnDate(long teacherId, LocalDate date) {
        List<Course> courses = null;
        Transaction tr = null;

        try (Session session = super.sessionFactory.openSession()) {
            // Prepare
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Course> query = criteriaBuilder.createQuery(Course.class);
            Root<Course> courseRoot = query.from(Course.class);

            // Predicates for WHERE clause
            Predicate predicateForUserId = criteriaBuilder
                    .equal(courseRoot.get(Course_.teacher).get(User_.id), teacherId);
            Predicate predicateForStartDate = criteriaBuilder.lessThanOrEqualTo(
                    courseRoot.get(Course_.courseInfo).get(CourseInfo_.startDate), date);
            Predicate predicateForEndDate = criteriaBuilder.greaterThanOrEqualTo(
                    courseRoot.get(Course_.courseInfo).get(CourseInfo_.endDate), date);

            query.select(courseRoot)
                    .where(criteriaBuilder
                            .and(predicateForUserId, predicateForStartDate, predicateForEndDate))
                    .orderBy(criteriaBuilder.asc(
                            courseRoot.get(Course_.courseInfo).get(CourseInfo_.endDate)));

            // Executing query and saving result
            tr = session.beginTransaction();
            courses = session.createQuery(query).getResultList();
            tr.commit();
        } catch (PersistenceException e) {
            log.error("Error while getting teacher courses active on given date", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return courses;
    }

    @Override
    public List<Course> getTeacherNotGradedCoursesEndedBeforeDate(long teacherId, LocalDate date) {
        List<Course> courses = null;
        Transaction tr = null;

        try (Session session = super.sessionFactory.openSession()) {
            // Prepare
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Course> rootQuery = criteriaBuilder.createQuery(Course.class);
            Root<Course> courseRoot = rootQuery.from(Course.class);

            // Join to get required teacher courses
            Join<Course, StudentCourseResult> scrJoin = courseRoot.join(Course_.studentResults);
            scrJoin.on(criteriaBuilder.equal(courseRoot.get(Course_.teacher).get(User_.id), teacherId))
                    .on(criteriaBuilder.isNull(scrJoin.get(StudentCourseResult_.result)));

            Predicate predicateForDate = criteriaBuilder
                    .lessThan(courseRoot.get(Course_.courseInfo).get(CourseInfo_.endDate), date);

            rootQuery.select(courseRoot)
                    .where(predicateForDate).distinct(true)
                    .orderBy(criteriaBuilder.asc(
                            courseRoot.get(Course_.courseInfo).get(CourseInfo_.endDate)));

            // Executing query and saving result
            tr = session.beginTransaction();
            courses = session.createQuery(rootQuery).getResultList();
            tr.commit();
        } catch (PersistenceException e) {
            log.error("Error while getting teacher not graded courses ended before given date", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return courses;
    }

}
