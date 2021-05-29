package com.company.manager.dao.generic.impl;

import com.company.manager.dao.generic.GenericDAO;
import com.company.manager.domain.archive.CourseResult;
import com.company.manager.domain.archive.StudentCourseResult;
import com.company.manager.domain.archive.StudentCourseResultId;
import com.company.manager.domain.course.Course;
import com.company.manager.domain.course.CourseInfo;
import com.company.manager.domain.user.*;
import com.company.manager.util.HibernateUtilForTest;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;
import java.util.Optional;

public class HibernateGenericDAOTest {

    private static SessionFactory sessionFactory;

    // Entity DAOs
    private static GenericDAO<AccessInfo> accessInfDAO;
    private static GenericDAO<User> usDAO;
    private static GenericDAO<Course> crsDAO;
    private static GenericDAO<StudentCourseResult> scrDAO;

    // Entities for tests
    private AccessInfo accessInf;
    private User us;
    private Course crs;
    private StudentCourseResult scr;

    private static void initDAOs() {
        accessInfDAO = new HibernateGenericDAO<>(AccessInfo.class, sessionFactory);
        usDAO = new HibernateGenericDAO<>(User.class, sessionFactory);
        crsDAO = new HibernateGenericDAO<>(Course.class, sessionFactory);
        scrDAO = new HibernateGenericDAO<>(StudentCourseResult.class, sessionFactory);
    }

    @BeforeAll
    public static void prepareForTest() {
        HibernateUtilForTest.clearDBTables();
        sessionFactory = HibernateUtilForTest.getSessionFactory();
        initDAOs();
    }

    private void initEntities() {
        accessInf = AccessInfo.builder().
                email("email").passwordHash("password".hashCode())
                .build();
        us = User.builder().accessInfo(accessInf)
                .userInfo(UserInfo.builder().firstName("firstName")
                        .build()).build();
        crs = Course.builder().courseInfo(
                CourseInfo.builder().name("courseName")
                        .build()).build();
        scr = StudentCourseResult.builder()
                .student(us).course(crs).build();
    }

    private void saveEntities() {
        accessInfDAO.save(accessInf);
        usDAO.save(us);
        crsDAO.save(crs);
        scrDAO.save(scr);
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
    public void saveTest() {
        Session session = sessionFactory.openSession();

        // Finding saved entity
        Transaction tr = session.beginTransaction();
        AccessInfo savedAccessInf =
                session.find(AccessInfo.class, accessInf.getId());
        tr.commit();
        // Testing
        Assertions.assertEquals(accessInf, savedAccessInf,
                "Save method should save AccessInfo entity correctly");

        // Finding saved entity
        User savedUs = session.find(User.class, us.getId());
        // Testing
        Assertions.assertEquals(us, savedUs,
                "Save method should save User entity correctly");

        // Finding saved entity
        Course savedCrs = session.find(Course.class, crs.getId());
        // Testing
        Assertions.assertEquals(crs, savedCrs,
                "Save method should save Course entity correctly");

        // Finding saved entity
        StudentCourseResultId scrId = new StudentCourseResultId(us, crs);
        StudentCourseResult savedSCR =
                session.find(StudentCourseResult.class, scrId);
        // Testing
        Assertions.assertEquals(scr, savedSCR,
                "Save method should save StudentCourseResult entity correctly");

        session.close();
    }

    @Test
    public void updateTest() {
        Session session = sessionFactory.openSession();

        // Updating
        accessInf.setEmail("updatedEmail");
        accessInfDAO.update(accessInf);
        // Finding updated entity
        AccessInfo accessInfFromDB =
                session.find(AccessInfo.class, accessInf.getId());
        // Testing
        Assertions.assertEquals(accessInf, accessInfFromDB,
                "Update method should update AccessInfo entity correctly");

        // Updating
        us.getUserInfo().setFirstName("updatedFirstName");
        usDAO.update(us);
        // Finding updated entity
        User usFromDB = session.find(User.class, us.getId());
        // Testing
        Assertions.assertEquals(us, usFromDB,
                "Update method should update User entity correctly");

        // Updating
        crs.getCourseInfo().setName("updatedCourseName");
        crsDAO.update(crs);
        // Finding updated entity
        Course crsFromDB = session.find(Course.class, crs.getId());
        // Testing
        Assertions.assertEquals(crs, crsFromDB,
                "Update method should update Course entity correctly");

        // Updating
        scr.setResult(CourseResult.EXCELLENT);
        scrDAO.update(scr);
        // Finding updated entity
        StudentCourseResultId scrId = new StudentCourseResultId(us, crs);
        StudentCourseResult scrFromDB =
                session.find(StudentCourseResult.class, scrId);
        // Testing
        Assertions.assertEquals(scr, scrFromDB,
                "Update method should update StudentCourseResult entity correctly");

        session.close();
    }

    @Test
    public void deleteTest() {
        Session session = sessionFactory.openSession();

        // Deleting StudentCourseResult entity
        scrDAO.delete(scr);
        // Trying to find deleted entity
        StudentCourseResultId scrId = new StudentCourseResultId(us, crs);
        StudentCourseResult scrFromDB =
                session.find(StudentCourseResult.class, scrId);
        // Testing
        Assertions.assertNull(scrFromDB,
                "Delete method should delete StudentCourseResult entity");

        // Deleting User and AccessInfo entity
        usDAO.delete(us);
        // Trying to find deleted User entity
        User usFromDB = session.find(User.class, us.getId());
        // Testing
        Assertions.assertNull(usFromDB,
                "Delete method should delete User entity");

        // Trying to find deleted AccessInfo entity
        AccessInfo accessInfFromDB =
                session.find(AccessInfo.class, accessInf.getId());
        // Testing
        Assertions.assertNull(accessInfFromDB,
                "Delete method should delete AccessInfo entity");

        // Deleting Course entity
        crsDAO.delete(crs);
        // Trying to find deleted entity
        Course crsFromDB = session.find(Course.class, crs.getId());
        // Testing
        Assertions.assertNull(crsFromDB,
                "Delete method should delete Course entity");

        session.close();
    }

    @Test
    public void findByIdTest() {
        // Finding saved entity
        Optional<AccessInfo> accessInfFromDB =
                accessInfDAO.findById(accessInf.getId());
        // Testing
        Assertions.assertEquals(accessInf, accessInfFromDB.get(),
                "Find method should find the same AccessInfo object as requested");

        // Finding saved entity
        Optional<User> usFromDB = usDAO.findById(us.getId());
        // Testing
        Assertions.assertEquals(us,usFromDB.get(),
                "Find method should find the same User object as requested");

        // Finding saved entity
        Optional<Course> crsFromDB = crsDAO.findById(crs.getId());
        // Testing
        Assertions.assertEquals(crs,crsFromDB.get(),
                "Find method should find the same Course object as requested");

        // Finding saved entity
        StudentCourseResultId scrId = new StudentCourseResultId(us, crs);
        Optional<StudentCourseResult> scrFromDB = scrDAO.findById(scrId);
        // Testing
        Assertions.assertEquals(scr,scrFromDB.get(),
                "Find method should find the same StudentCourseResult object as requested");
    }
}
