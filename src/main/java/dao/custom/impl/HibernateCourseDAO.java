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

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class HibernateCourseDAO extends HibernateGenericDAO<Course> implements CourseDAO {

    public HibernateCourseDAO() {
        super(Course.class);
    }

    public List<Course> getUserNotSubscribedCoursesAfterDate(long userId, LocalDate date) {
        List<Course> courses = new ArrayList<>();
        Transaction tr = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Prepare
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Course> query = criteriaBuilder.createQuery(Course.class);
            Root<Course> course = query.from(Course.class);

            // Predicates for WHERE clause
            Predicate predicateForUserId = criteriaBuilder.isNotMember(userId, course.get(Course_.TEACHER_AND_STUDENTS));
            Predicate predicateForStartDate = criteriaBuilder.greaterThan(
                    course.<LocalDate>get(Course_.COURSE_INFO).get(CourseInfo_.START_DATE), date);

            query.select(course)
                    .where(criteriaBuilder.and(predicateForUserId, predicateForStartDate));

            // Executing query and saving result
            tr = session.beginTransaction();
            courses.addAll(session.createQuery(query).getResultList());
            tr.commit();
        } catch (Exception e) {
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
            Root<Course> course = query.from(Course.class);

            // Predicates for WHERE clause
            Predicate predicateForUserId = criteriaBuilder.isMember(userId, course.get(Course_.TEACHER_AND_STUDENTS));
            Predicate predicateForStartDate = criteriaBuilder.greaterThan(
                    course.<LocalDate>get(Course_.COURSE_INFO).get(CourseInfo_.START_DATE), date);

            query.select(course)
                    .where(criteriaBuilder.and(predicateForUserId, predicateForStartDate));

            // Executing query and saving result
            tr = session.beginTransaction();
            courses.addAll(session.createQuery(query).getResultList());
            tr.commit();
        } catch (Exception e) {
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
            Root<Course> course = query.from(Course.class);

            // Predicates for WHERE clause
            Predicate predicateForUserId = criteriaBuilder.isMember(userId, course.get(Course_.TEACHER_AND_STUDENTS));
            Predicate predicateForStartDate = criteriaBuilder.lessThanOrEqualTo(
                    course.<LocalDate>get(Course_.COURSE_INFO).get(CourseInfo_.START_DATE), date);
            Predicate predicateForEndDate = criteriaBuilder.greaterThanOrEqualTo(
                    course.<LocalDate>get(Course_.COURSE_INFO).get(CourseInfo_.END_DATE), date);

            query.select(course)
                    .where(criteriaBuilder.and(predicateForUserId, predicateForStartDate, predicateForEndDate));

            // Executing query and saving result
            tr = session.beginTransaction();
            courses.addAll(session.createQuery(query).getResultList());
            tr.commit();
        } catch (Exception e) {
            log.error("Error while getting all courses started after given date");
            e.printStackTrace();
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return courses;
    }

    @Override
    public List<Course> getUserUngradedCoursesEndedBeforeDate(long userId, LocalDate date) {
        List<Course> requiredCourses = new ArrayList<>();
        Transaction tr = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tr = session.beginTransaction();

           /* // Fetch user ungraded courses ended before given date
            String hqlQuery = "FROM Course crs " +
                    "LEFT JOIN crs.teacherAndStudents tas WITH tas.userInfo = :user_id " +
                    "LEFT JOIN StudentCourseResult scr ON scr.course=crs WITH scr IS NULL " +
                    "WHERE crs.courseInfo.endDate < :date";

            Query<Course> query = session.createQuery(hqlQuery);
            query.setParameter("user_id", userId);
            query.setParameter("date", date, TemporalType.DATE);
*/

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

            CriteriaQuery<Course> query = criteriaBuilder.createQuery(Course.class);
            Root<Course> course = query.from(Course.class);

            // Getting user graded courses id
            Subquery<Long> subQuery = query.subquery(Long.class);
            Root<StudentCourseResult> scr = subQuery.from(StudentCourseResult.class);

            Join<StudentCourseResult, User> user = scr.join(StudentCourseResult_.STUDENT);
            user.on(criteriaBuilder.equal(user.get(User_.ID), userId));

            subQuery.select(scr.get(StudentCourseResult_.COURSE).get(Course_.ID));

            // Getting user not-graded courses
            query.select(course)
                    .where(course.get(Course_.ID).in(subQuery).not());

            requiredCourses.addAll(session.createQuery(query).getResultList());

            tr.commit();
        } catch (Exception e) {
            log.error("Error while getting user ungraded courses ended before given date");
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

            // Predicate for WHERE clause
            Predicate predicateForCourseId = root.get(Course_.ID).in(idList);

            query.select(root).where(predicateForCourseId);

            // Executing query and saving result
            tr = session.beginTransaction();
            courses.addAll(session.createQuery(query).getResultList());
            tr.commit();
        } catch (Exception e) {
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
        } catch (Exception e) {
            log.error("Error while getting all courses started after given date");
            e.printStackTrace();
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return courses;
    }
}
