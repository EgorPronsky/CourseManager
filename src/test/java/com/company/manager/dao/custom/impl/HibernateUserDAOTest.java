package com.company.manager.dao.custom.impl;

import com.company.manager.dao.custom.UserDAO;
import com.company.manager.domain.user.AccessInfo;
import com.company.manager.domain.user.User;
import com.company.manager.util.HibernateUtilForTest;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;

import java.util.Optional;

public class HibernateUserDAOTest {

    private final SessionFactory sessionFactory =
            HibernateUtilForTest.getSessionFactory();

    // Entities for tests
    private AccessInfo accessInf;
    private User us;

    // DAOs
    private UserDAO usDAO;

    // Init entities
    {
        accessInf = AccessInfo.builder()
                .email("email").passwordHash("password".hashCode())
                .build();
        us = User.builder().accessInfo(accessInf).build();
    }

    // Init DAOs
    {
        usDAO = new HibernateUserDAO(sessionFactory);
    }

    @BeforeAll
    public static void prepareForTest() {
        HibernateUtilForTest.clearDBTables();
    }

    @AfterEach
    public void clearAfterTest() {
        HibernateUtilForTest.clearDBTables();
    }

    @Test
    public void findUserByEmailAndPasswordHash() {
        // Saving User entity
        usDAO.save(us);
        // Finding saved entity
        Optional<User> usFromDB = usDAO.findUserByEmailAndPasswordHash(
                us.getAccessInfo().getEmail(), us.getAccessInfo().getPasswordHash());

        // Testing
        Assertions.assertEquals(us, usFromDB.get(),
                "Should be found User with requested email and password hash");
    }
}
