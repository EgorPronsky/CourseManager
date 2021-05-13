package util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    // Init on-demand
    private static class SessionFactoryHolder {
        public static final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        return SessionFactoryHolder.sessionFactory;
    }

}
