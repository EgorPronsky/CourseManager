package com.company.manager.dao.custom.impl;

import com.company.manager.dao.custom.StudentCourseResultDAO;
import com.company.manager.dao.generic.impl.HibernateGenericDAO;
import com.company.manager.domain.archive.StudentCourseResult;
import com.company.manager.domain.archive.StudentCourseResult_;
import com.company.manager.domain.course.Course_;
import com.company.manager.domain.user.Student_;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.company.manager.util.HibernateUtil;

import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class HibernateStudentCourseResultDAO extends HibernateGenericDAO<StudentCourseResult> implements StudentCourseResultDAO {

    public HibernateStudentCourseResultDAO() {
        super(StudentCourseResult.class);
    }

    @Override
    public void saveAll(Collection<StudentCourseResult> scrCollection) {
        Transaction tr = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tr = session.beginTransaction();
            scrCollection.forEach(session::save);
            tr.commit();
        } catch (HibernateException e) {
            log.debug("Error while saving SCR entities", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
    }

    @Override
    public List<StudentCourseResult> getStudentsResultsByCourseId(long courseId) {
        List<StudentCourseResult> scrList = new ArrayList<>();
        Transaction tr = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Prepare
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<StudentCourseResult> query = criteriaBuilder.createQuery(StudentCourseResult.class);
            Root<StudentCourseResult> scr = query.from(StudentCourseResult.class);

            query.select(scr)
                    .where(criteriaBuilder
                            .equal(scr.get(StudentCourseResult_.course).get(Course_.id), courseId));

            // Executing query and saving result
            tr = session.beginTransaction();
            scrList.addAll(session.createQuery(query).getResultList());
            tr.commit();
        } catch (PersistenceException e) {
            log.debug("Error while finding course students results", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return scrList;
    }

    @Override
    public List<StudentCourseResult> getCoursesResultsByStudentId(long studentId) {
        List<StudentCourseResult> scrList = new ArrayList<>();
        Transaction tr = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Prepare
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<StudentCourseResult> query = criteriaBuilder.createQuery(StudentCourseResult.class);
            Root<StudentCourseResult> scr = query.from(StudentCourseResult.class);

            query.select(scr)
                    .where(criteriaBuilder
                            .equal(scr.get(StudentCourseResult_.student).get(Student_.id), studentId));

            // Executing query and saving result
            tr = session.beginTransaction();
            scrList.addAll(session.createQuery(query).getResultList());
            tr.commit();
        } catch (PersistenceException e) {
            log.debug("Error while finding student courses results", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return scrList;
    }
}
