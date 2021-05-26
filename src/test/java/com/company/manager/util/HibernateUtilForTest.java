package com.company.manager.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.PersistenceException;

public class HibernateUtilForTest {

    private static final SessionFactory sessionFactory;

    static {
        sessionFactory = new Configuration().configure()
                .buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void clearDBTables() {
        if (sessionFactory != null) {
            Transaction tr = null;
            try (Session session = sessionFactory.openSession()) {
                tr = session.beginTransaction();
                session.createQuery("DELETE FROM StudentCourseResult").executeUpdate();
                session.createQuery("DELETE FROM User").executeUpdate();
                session.createQuery("DELETE FROM AccessInfo ").executeUpdate();
                session.createQuery("DELETE FROM Course").executeUpdate();
                tr.commit();
            } catch (PersistenceException e) {
                e.printStackTrace();
                if (tr != null && tr.isActive()) tr.rollback();
            }
        }
    }

}
