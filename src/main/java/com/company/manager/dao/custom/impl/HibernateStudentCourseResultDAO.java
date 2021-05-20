package com.company.manager.dao.custom.impl;

import com.company.manager.dao.custom.StudentCourseResultDAO;
import com.company.manager.dao.generic.impl.HibernateGenericDAO;
import com.company.manager.domain.archive.StudentCourseResult;
import com.company.manager.domain.archive.StudentCourseResult_;
import com.company.manager.domain.course.CourseInfo_;
import com.company.manager.domain.course.Course_;
import com.company.manager.domain.user.UserInfo_;
import com.company.manager.domain.user.User_;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.company.manager.util.HibernateUtil;

import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Slf4j
public class HibernateStudentCourseResultDAO extends HibernateGenericDAO<StudentCourseResult> implements StudentCourseResultDAO {

    public HibernateStudentCourseResultDAO() {
        super(StudentCourseResult.class);
    }

    @Override
    public void updateAll(Collection<StudentCourseResult> scrCollection) {
        Transaction tr = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tr = session.beginTransaction();
            scrCollection.forEach(session::update);
            tr.commit();
        } catch (HibernateException e) {
            log.debug("Error while updating SCR entities", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
    }

    @Override
    public List<StudentCourseResult> getStudentsResultsByCourseId(long courseId) {
        List<StudentCourseResult> scrList = null;
        Transaction tr = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Prepare
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<StudentCourseResult> query = criteriaBuilder.createQuery(StudentCourseResult.class);
            Root<StudentCourseResult> scr = query.from(StudentCourseResult.class);

            query.select(scr)
                    .where(criteriaBuilder
                            .equal(scr.get(StudentCourseResult_.course).get(Course_.id), courseId))
                    .orderBy(criteriaBuilder
                            .desc(scr.get(StudentCourseResult_.student).get(User_.userInfo).get(UserInfo_.lastName)));

            // Executing query and saving result
            tr = session.beginTransaction();
            scrList = session.createQuery(query).getResultList();
            tr.commit();
        } catch (PersistenceException e) {
            log.debug("Error while finding course students results", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return scrList;
    }

    @Override
    public List<StudentCourseResult> getEndedBeforeDateCoursesWithResultsByStudentId(long studentId, LocalDate date) {
        List<StudentCourseResult> scrList = null;
        Transaction tr = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Prepare
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<StudentCourseResult> query = criteriaBuilder.createQuery(StudentCourseResult.class);
            Root<StudentCourseResult> scrRoot = query.from(StudentCourseResult.class);

            Predicate predicateForStudentId =  criteriaBuilder.equal(
                    scrRoot.get(StudentCourseResult_.student).get(User_.id), studentId);
            Predicate predicateForDate = criteriaBuilder.lessThan(
                    scrRoot.get(StudentCourseResult_.course).get(Course_.courseInfo).get(CourseInfo_.endDate), date);

            query.select(scrRoot)
                    .where(criteriaBuilder
                            .and(predicateForStudentId, predicateForDate))
                    .orderBy(criteriaBuilder
                            .desc(scrRoot.get(StudentCourseResult_.course).get(Course_.courseInfo).get(CourseInfo_.endDate)));

            // Executing query and saving result
            tr = session.beginTransaction();
            scrList = session.createQuery(query).getResultList();
            tr.commit();
        } catch (PersistenceException e) {
            log.debug("Error while finding student ended before given date courses with results", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return scrList;
    }
}
