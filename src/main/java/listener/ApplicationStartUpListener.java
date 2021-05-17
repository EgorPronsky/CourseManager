package listener;

import lombok.extern.slf4j.Slf4j;
import services.impl.AccessInfoServiceImpl;
import services.impl.CourseServiceImpl;
import services.impl.StudentCourseResultServiceImpl;
import services.impl.UserServiceImpl;
import util.HibernateUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Slf4j
public class ApplicationStartUpListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            log.debug("Start initializing required classes");
            Class.forName(HibernateUtil.class.getName());
            Class.forName(AccessInfoServiceImpl.class.getName());
            Class.forName(CourseServiceImpl.class.getName());
            Class.forName(StudentCourseResultServiceImpl.class.getName());
            Class.forName(UserServiceImpl.class.getName());
            log.debug("Classes were initialized");
        } catch (ClassNotFoundException e) {
            log.error("Classes weren't initialized successfully", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
