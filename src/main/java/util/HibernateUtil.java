package util;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@Slf4j
public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        log.info("Configuring and initializing hibernate session factory");
        sessionFactory =
                new Configuration().configure("META-INF/hibernate.cfg.xml")
                        .buildSessionFactory();
        log.info("Session factory was initialized");
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
