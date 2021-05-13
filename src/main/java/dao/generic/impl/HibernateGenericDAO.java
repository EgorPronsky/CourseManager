package dao.generic.impl;

import dao.generic.GenericDAO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

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
        } catch (Exception e) {
            log.error("Error occurred while saving an entity");
            e.printStackTrace();
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
        } catch (Exception e) {
            log.error("Error occurred while finding an entity by id");
            e.printStackTrace();
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
        } catch (Exception e) {
            log.error("Error occurred while updating an entity");
            e.printStackTrace();
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
        } catch (Exception e) {
            log.error("Error occurred while deleting an entity");
            e.printStackTrace();
            if (tr != null && tr.isActive()) tr.rollback();
        }
    }

}
