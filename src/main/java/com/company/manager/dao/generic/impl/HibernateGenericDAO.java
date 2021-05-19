package com.company.manager.dao.generic.impl;

import com.company.manager.dao.generic.GenericDAO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.company.manager.util.HibernateUtil;

import java.util.Optional;

@Slf4j
public class HibernateGenericDAO<T> implements GenericDAO<T> {

    private final Class<T> type;

    public HibernateGenericDAO(Class<T> type) {
        this.type = type;
    }

    @Override
    public void save(T entity) {
        Transaction tr = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tr = session.beginTransaction();
            session.save(entity);
            tr.commit();
        } catch (HibernateException e) {
            log.error("Error occurred during saving an entity", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
    }

    @Override
    public Optional<T> findById(long id) {
        T entity = null;
        Transaction tr = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tr = session.beginTransaction();
            entity = session.get(type, id);
            tr.commit();
        } catch (HibernateException e) {
            log.error("Error occurred during finding an entity by id", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
        return Optional.ofNullable(entity);
    }

    @Override
    public void update(T entity) {
        Transaction tr = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tr = session.beginTransaction();
            session.update(entity);
            tr.commit();
        } catch (HibernateException e) {
            log.error("Error occurred during updating an entity", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
    }

    @Override
    public void delete(T entity) {
        Transaction tr = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tr = session.beginTransaction();
            session.delete(entity);
            tr.commit();
        } catch (HibernateException e) {
            log.error("Error occurred during deleting an entity", e);
            if (tr != null && tr.isActive()) tr.rollback();
        }
    }

}
