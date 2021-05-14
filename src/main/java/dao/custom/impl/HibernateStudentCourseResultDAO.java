package dao.custom.impl;

import dao.custom.StudentCourseResultDAO;
import dao.generic.impl.HibernateGenericDAO;
import domain.archive.StudentCourseResult;
import domain.archive.StudentCourseResult_;
import domain.course.Course;
import domain.course.Course_;
import domain.user.User_;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class HibernateStudentCourseResultDAO extends HibernateGenericDAO<StudentCourseResult> implements StudentCourseResultDAO {

    public HibernateStudentCourseResultDAO() {
        super(StudentCourseResult.class);
    }

    @Override
    public void saveAll(List<StudentCourseResult> scrList) {
        Transaction tr = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tr = session.beginTransaction();
            scrList.forEach(session::save);
            tr.commit();
        } catch (HibernateException e) {
            log.debug("Error while saving SCR entities");
            e.printStackTrace();
            if (tr != null && tr.isActive()) tr.rollback();
        }
    }

    @Override
    public List<StudentCourseResult> getStudentsResultsByCourseId(long courseId) {
        List<StudentCourseResult> scrList = new ArrayList<>();
        Transaction tr = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tr = session.beginTransaction();

            /*// Fetch all students results by course id
            String hqlQuery = "FROM StudentCourseResult scr WHERE scr.course.id=:course_id";

            Query<StudentCourseResult> query = session.createQuery(hqlQuery);
            query.setParameter("course_id", courseId);*/

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

            CriteriaQuery<StudentCourseResult> query = criteriaBuilder.createQuery(StudentCourseResult.class);
            Root<StudentCourseResult> scr = query.from(StudentCourseResult.class);

            query.select(scr)
                    .where(criteriaBuilder.equal(scr.get(StudentCourseResult_.COURSE).get(Course_.ID), courseId));

            scrList.addAll(session.createQuery(query).getResultList());

            tr.commit();
        } catch (PersistenceException e) {
            log.debug("Error while finding student course result by course id");
            e.printStackTrace();
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return scrList;
    }

    @Override
    public List<StudentCourseResult> getCoursesResultsByStudentId(long studentId) {
        List<StudentCourseResult> scrList = new ArrayList<>();
        Transaction tr = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tr = session.beginTransaction();

            /*// Fetch all courses results by student id
            String hqlQuery = "FROM StudentCourseResult scr WHERE scr.student.id=:student_id";

            Query query = session.createQuery(hqlQuery);
            query.setParameter("student_id", studentId);*/

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

            CriteriaQuery<StudentCourseResult> query = criteriaBuilder.createQuery(StudentCourseResult.class);
            Root<StudentCourseResult> scr = query.from(StudentCourseResult.class);

            query.select(scr)
                    .where(criteriaBuilder.equal(scr.get(StudentCourseResult_.STUDENT).get(Course_.ID), studentId));

            scrList.addAll(session.createQuery(query).getResultList());

            tr.commit();
        } catch (PersistenceException e) {
            log.debug("Error while finding student courses with results");
            e.printStackTrace();
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return scrList;
    }
}
