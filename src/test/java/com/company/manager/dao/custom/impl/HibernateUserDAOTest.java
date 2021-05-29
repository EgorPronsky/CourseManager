package com.company.manager.dao.custom.impl;

import com.company.manager.dao.custom.UserDAO;
import com.company.manager.dao.generic.impl.HibernateGenericDAO;
import com.company.manager.domain.archive.StudentCourseResult;
import com.company.manager.domain.course.Course;
import com.company.manager.domain.course.CourseInfo;
import com.company.manager.domain.user.AccessInfo;
import com.company.manager.domain.user.User;
import com.company.manager.domain.user.UserInfo;
import com.company.manager.util.HibernateUtilForTest;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;

import java.util.Optional;

public class HibernateUserDAOTest {

    private static SessionFactory sessionFactory;

    // DAOs
    private static UserDAO usDAO;

    // Entities for tests
    private User us;

    private static void initDAOs() {
        usDAO = new HibernateUserDAO(sessionFactory);
    }

    @BeforeAll
    public static void prepareForTest() {
        HibernateUtilForTest.clearDBTables();
        sessionFactory = HibernateUtilForTest.getSessionFactory();
        initDAOs();
    }

    private void initEntities() {
        AccessInfo accessInf = AccessInfo.builder()
                .email("email").passwordHash("password".hashCode())
                .build();
        us = User.builder().accessInfo(accessInf).build();
    }

    private void saveEntities() {
        usDAO.save(us);
    }

    @BeforeEach
    public void prepareBeforeEachTest() {
        initEntities();
        saveEntities();
    }

    @AfterEach
    public void clearAfterEachTest() {
        HibernateUtilForTest.clearDBTables();
    }

    @Test
    public void findUserByEmailAndPasswordHash() {
        // Finding saved entity
        Optional<User> usFromDB = usDAO.findUserByEmailAndPasswordHash(
                us.getAccessInfo().getEmail(), us.getAccessInfo().getPasswordHash());

        // Testing
        Assertions.assertEquals(us, usFromDB.get(),
                "Should be found User with requested email and password hash");
    }
}
