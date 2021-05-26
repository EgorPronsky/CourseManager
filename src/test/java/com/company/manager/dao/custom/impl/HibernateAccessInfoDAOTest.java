package com.company.manager.dao.custom.impl;

import com.company.manager.dao.custom.AccessInfoDAO;
import com.company.manager.domain.user.AccessInfo;
import com.company.manager.util.HibernateUtilForTest;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;

import java.util.Optional;

public class HibernateAccessInfoDAOTest {

    private final SessionFactory sessionFactory =
            HibernateUtilForTest.getSessionFactory();

    // Entities for tests
    private AccessInfo accessInf;

    // DAOs
    private AccessInfoDAO accessInfDAO;

    // Init entities
    {
        accessInf = AccessInfo.builder()
                .email("email").passwordHash("password".hashCode())
                .build();
    }

    // Init DAOs
    {
        accessInfDAO = new HibernateAccessInfoDAO(sessionFactory);
    }

    @BeforeAll
    public static void prepareForTest() {
        HibernateUtilForTest.clearDBTables();
    }

    @BeforeEach
    public void saveEntities() {
        accessInfDAO.save(accessInf);
    }

    @AfterEach
    public void clearAfterTest() {
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
