package util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    public static final SessionFactory sessionFactory =
            new Configuration().configure("META-INF/hibernate.cfg.xml").buildSessionFactory();

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
