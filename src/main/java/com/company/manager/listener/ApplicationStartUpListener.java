package com.company.manager.listener;

import com.company.manager.services.impl.*;
import lombok.extern.slf4j.Slf4j;
import com.company.manager.util.HibernateUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Slf4j
public class ApplicationStartUpListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            log.debug("Start loading required classes");
            Class.forName(HibernateUtil.class.getName());
            Class.forName(AccessInfoServiceImpl.class.getName());
            Class.forName(CourseServiceImpl.class.getName());
            Class.forName(StudentCourseResultServiceImpl.class.getName());
            Class.forName(UserServiceImpl.class.getName());
            log.debug("Classes were loaded");
        } catch (ClassNotFoundException e) {
            log.error("Class wasn't found",e);
        } catch (Exception e) {
            log.error("Classes weren't loaded successfully", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
