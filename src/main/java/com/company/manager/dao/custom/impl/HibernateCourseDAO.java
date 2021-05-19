package com.company.manager.dao.custom.impl;

import com.company.manager.dao.generic.impl.HibernateGenericDAO;
import com.company.manager.dao.custom.CourseDAO;
import com.company.manager.domain.archive.StudentCourseResult;
import com.company.manager.domain.archive.StudentCourseResult_;
import com.company.manager.domain.course.CourseInfo_;
import com.company.manager.domain.course.Course_;
import com.company.manager.domain.user.Student;
import com.company.manager.domain.user.Student_;
import com.company.manager.domain.user.Teacher;
import com.company.manager.domain.course.Course;
import com.company.manager.domain.user.Teacher_;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.company.manager.util.HibernateUtil;

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
    public List<Course> getStudentCoursesStartAfterDate(long teacherId, LocalDate date) {
        List<Course> courses = new ArrayList<>();
        Transaction tr = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Prepare
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Course> query = criteriaBuilder.createQuery(Course.class);
            Root<Course> courseRoot = query.from(Course.class);
            Join<Course, Student> studentJoin = courseRoot.join(Course_.students);

            // Predicates for WHERE clause
            Predicate predicateForUserId = criteriaBuilder.equal(studentJoin.get(Student_.id), teacherId);
            Predicate predicateForDate = criteriaBuilder.greaterThan(
                    courseRoot.get(Course_.courseInfo).get(CourseInfo_.startDate), date);

            query.select(courseRoot)
                    .where(criteriaBuilder.and(predicateForUserId, predicateForDate));

            // Executing query and saving result
            tr = session.beginTransaction();
            courses.addAll(session.createQuery(query).getResultList());
            tr.commit();
        } catch (PersistenceException e) {
            log.error("Error while getting student courses that start after given date", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return courses;
    }

    @Override
    public List<Course> getStudentCoursesActiveOnDate(long studentId, LocalDate date) {
        List<Course> courses = new ArrayList<>();
        Transaction tr = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Prepare
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Course> query = criteriaBuilder.createQuery(Course.class);
            Root<Course> courseRoot = query.from(Course.class);
            Join<Course, Student> studentJoin = courseRoot.join(Course_.students);

            // Predicates for WHERE clause
            Predicate predicateForUserId = criteriaBuilder.equal(studentJoin.get(Student_.id), studentId);
            Predicate predicateForStartDate = criteriaBuilder.lessThanOrEqualTo(
                    courseRoot.get(Course_.courseInfo).get(CourseInfo_.startDate), date);
            Predicate predicateForEndDate = criteriaBuilder.greaterThanOrEqualTo(
                    courseRoot.get(Course_.courseInfo).get(CourseInfo_.endDate), date);

            query.select(courseRoot)
                    .where(criteriaBuilder
                            .and(predicateForUserId, predicateForStartDate, predicateForEndDate));

            // Executing query and saving result
            tr = session.beginTransaction();
            courses.addAll(session.createQuery(query).getResultList());
            tr.commit();
        } catch (PersistenceException e) {
            log.error("Error while getting student courses active on given date", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return courses;
    }

    @Override
    public List<Course> getStudentNotSubscribedCoursesStartAfterDate(long studentId, LocalDate date) {
        List<Course> courses = new ArrayList<>();
        Transaction tr = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Prepare
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Course> rootQuery = criteriaBuilder.createQuery(Course.class);
            Root<Course> courseRoot = rootQuery.from(Course.class);

            // Sub query to get student subscribed courses
            Subquery<Course> subQuery = rootQuery.subquery(Course.class);
            Root<Course> courseSubRoot = subQuery.from(Course.class);
            Join<Course, Student> studentSubJoin = courseSubRoot.join(Course_.students);

            subQuery.select(courseSubRoot)
                    .where(criteriaBuilder.equal(studentSubJoin.get(Student_.id), studentId));

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
            log.error("Error while getting student not subscribed courses that start after given date", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return courses;
    }

    @Override
    public List<Course> getStudentUngradedCoursesEndedBeforeDate(long studentId, LocalDate date) {
        List<Course> requiredCourses = new ArrayList<>();
        Transaction tr = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Prepare
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Course> rootQuery = criteriaBuilder.createQuery(Course.class);
            Root<Student> userRoot = rootQuery.from(Student.class);
            Join<Student, Course> courseJoin = userRoot.join(Student_.courses);

            // Sub query to get student graded courses
            Subquery<Course> subQuery = rootQuery.subquery(Course.class);
            Root<StudentCourseResult> scrSubRoot = subQuery.from(StudentCourseResult.class);

            // Predicate for sub query
            Predicate subPredicateForStudentId = criteriaBuilder
                    .equal(scrSubRoot.get(StudentCourseResult_.student).get(Student_.id), studentId);

            subQuery.select(scrSubRoot.get(StudentCourseResult_.course))
                    .where(subPredicateForStudentId);

            // Predicates for WHERE clause of root query
            Predicate predicateForUserId = criteriaBuilder.equal(userRoot.get(Student_.id), studentId);
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
            log.error("Error while getting student ungraded courses ended before given date", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return requiredCourses;
    }

    @Override
    public List<Course> getTeacherCoursesStartAfterDate(long teacherId, LocalDate date) {
        List<Course> courses = new ArrayList<>();
        Transaction tr = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Prepare
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Course> query = criteriaBuilder.createQuery(Course.class);
            Root<Course> courseRoot = query.from(Course.class);

            // Predicates for WHERE clause
            Predicate predicateForUserId = criteriaBuilder
                    .equal(courseRoot.get(Course_.teacher).get(Teacher_.id), teacherId);
            Predicate predicateForDate = criteriaBuilder.greaterThan(
                    courseRoot.get(Course_.courseInfo).get(CourseInfo_.startDate), date);

            query.select(courseRoot)
                    .where(criteriaBuilder.and(predicateForUserId, predicateForDate));

            // Executing query and saving result
            tr = session.beginTransaction();
            courses.addAll(session.createQuery(query).getResultList());
            tr.commit();
        } catch (PersistenceException e) {
            log.error("Error while getting teacher courses that start after given date", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return courses;
    }

    @Override
    public List<Course> getTeacherCoursesActiveOnDate(long teacherId, LocalDate date) {
        List<Course> courses = new ArrayList<>();
        Transaction tr = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Prepare
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Course> query = criteriaBuilder.createQuery(Course.class);
            Root<Course> courseRoot = query.from(Course.class);

            // Predicates for WHERE clause
            Predicate predicateForUserId = criteriaBuilder
                    .equal(courseRoot.get(Course_.teacher).get(Teacher_.id), teacherId);
            Predicate predicateForStartDate = criteriaBuilder.lessThanOrEqualTo(
                    courseRoot.get(Course_.courseInfo).get(CourseInfo_.startDate), date);
            Predicate predicateForEndDate = criteriaBuilder.greaterThanOrEqualTo(
                    courseRoot.get(Course_.courseInfo).get(CourseInfo_.endDate), date);

            query.select(courseRoot)
                    .where(criteriaBuilder
                            .and(predicateForUserId, predicateForStartDate, predicateForEndDate));

            // Executing query and saving result
            tr = session.beginTransaction();
            courses.addAll(session.createQuery(query).getResultList());
            tr.commit();
        } catch (PersistenceException e) {
            log.error("Error while getting teacher courses active on given date", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return courses;
    }

    @Override
    public List<Course> getTeacherNotGradedCoursesEndedBeforeDate(long teacherId, LocalDate date) {
        List<Course> requiredCourses = new ArrayList<>();
        Transaction tr = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Prepare
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Course> rootQuery = criteriaBuilder.createQuery(Course.class);
            Root<Teacher> teacherRoot = rootQuery.from(Teacher.class);
            Join<Teacher, Course> courseJoin = teacherRoot.join(Teacher_.courses);

            // Sub query to get all graded courses
            Subquery<Course> subQuery = rootQuery.subquery(Course.class);
            Root<StudentCourseResult> scrSubRoot = subQuery.from(StudentCourseResult.class);

            subQuery.select(scrSubRoot.get(StudentCourseResult_.course)).distinct(true);

            // Predicates for WHERE clause of root query
            Predicate predicateForUserId = criteriaBuilder.equal(teacherRoot.get(Teacher_.id), teacherId);
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
            log.error("Error while getting teacher not graded courses ended before given date", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return requiredCourses;
    }

}
