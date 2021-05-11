package dao.custom.impl;

import dao.custom.CourseDAO;
import dao.generic.impl.HibernateGenericDAO;
import domain.course.Course;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import javax.persistence.TemporalType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class HibernateCourseDAO extends HibernateGenericDAO<Course> implements CourseDAO {

    public HibernateCourseDAO() {
        super(Course.class);
    }

    @Override
    public List<Course> getUserUngradedCoursesEndedBeforeDate(long userId, LocalDate date) {
        List<Course> courses = new ArrayList<>();
        Transaction tr = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tr = session.beginTransaction();

            // Fetch user ungraded courses ended before given date
            String hqlQuery = "FROM Course crs " +
                    "LEFT JOIN crs.teacherAndStudents tas WITH tas.userInfo = :user_id " +
                    "LEFT JOIN StudentCourseResult scr ON scr.course=crs WITH scr IS NULL " +
                    "WHERE crs.courseInfo.endDate < :date";

            Query<Course> query = session.createQuery(hqlQuery);
            query.setParameter("user_id", userId);
            query.setParameter("date", date, TemporalType.DATE);

            courses.addAll(query.getResultList());

            tr.commit();
        } catch (Exception e) {
            log.error("Error while getting user ungraded courses ended before given date");
            e.printStackTrace();
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return courses;
    }

    @Override
    public List<Course> getUserCoursesEndedBeforeDate(long userId, LocalDate date) {
        List<Course> courses = new ArrayList<>();
        Transaction tr = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tr = session.beginTransaction();

            // Fetch user courses ended before given date
            String hqlQuery = "FROM Course crs " +
                    "LEFT JOIN crs.teacherAndStudents tas WITH tas.id = :user_id " +
                    "WHERE crs.courseInfo.endDate < :date";

            Query<Course> query = session.createQuery(hqlQuery);
            query.setParameter("user_id", userId);
            query.setParameter("date", date, TemporalType.DATE);

            courses.addAll(query.getResultList());

            tr.commit();
        } catch (Exception e) {
            log.error("Error while getting user courses ended before given date");
            e.printStackTrace();
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return courses;
    }

    @Override
    public List<Course> getAllById(List<Long> idList) {
        List<Course> courses = new ArrayList<>();
        Transaction tr = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tr = session.beginTransaction();

            // Fetch all courses by id
            String hqlQuery = "FROM Course crs WHERE crs.id IN (:courses)";

            Query<Course> query = session.createQuery(hqlQuery);
            query.setParameter("courses", idList);

            courses.addAll(query.getResultList());

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

            // Fetch all courses started after given date
            String hqlQuery = "FROM Course crs WHERE crs.courseInfo.startDate > :date";

            Query<Course> query = session.createQuery(hqlQuery);
            query.setParameter("date", date, TemporalType.DATE);

            courses.addAll(query.getResultList());

            tr.commit();
        } catch (Exception e) {
            log.error("Error while getting all courses started after given date");
            e.printStackTrace();
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return courses;
    }
}
