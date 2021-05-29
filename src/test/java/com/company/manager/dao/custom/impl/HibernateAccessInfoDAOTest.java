package com.company.manager.dao.custom.impl;

import com.company.manager.dao.custom.AccessInfoDAO;
import com.company.manager.domain.course.Course;
import com.company.manager.domain.user.AccessInfo;
import com.company.manager.util.HibernateUtilForTest;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;

import java.util.Optional;

public class HibernateAccessInfoDAOTest {

    private static SessionFactory sessionFactory;

    // DAOs
    private static AccessInfoDAO accessInfDAO;

    // Entities for tests
    private AccessInfo accessInf;

    private static void initDAOs() {
        accessInfDAO = new HibernateAccessInfoDAO(sessionFactory);
    }

    @BeforeAll
    public static void prepareForTest() {
        HibernateUtilForTest.clearDBTables();
        sessionFactory = HibernateUtilForTest.getSessionFactory();
        initDAOs();
    }

    private void initEntities() {
        accessInf = AccessInfo.builder()
                .email("email").passwordHash("password".hashCode())
                .build();
    }

    private void saveEntities() {
        accessInfDAO.save(accessInf);
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
    public void findAccessInfoByEmailTest() {
        // Finding
        Optional<AccessInfo> accessInfFromDB = accessInfDAO
                .findAccessInfoByEmail(accessInf.getEmail());
        // Testing
        Assertions.assertEquals(accessInf, accessInfFromDB.get(),
                "Should find AccessInfo object with requested email");
    }
}
