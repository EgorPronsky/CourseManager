package com.company.manager.dao.custom.impl;

import com.company.manager.dao.custom.CourseDAO;
import com.company.manager.dao.custom.StudentCourseResultDAO;
import com.company.manager.dao.custom.UserDAO;
import com.company.manager.domain.archive.CourseResult;
import com.company.manager.domain.archive.StudentCourseResult;
import com.company.manager.domain.course.Course;
import com.company.manager.domain.course.CourseInfo;
import com.company.manager.domain.user.AccessInfo;
import com.company.manager.domain.user.User;
import com.company.manager.util.HibernateUtilForTest;
import com.company.manager.util.TestUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;

import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class HibernateStudentCourseResultDAOTest {

    private static SessionFactory sessionFactory;

    // DAOs
    private static UserDAO usDAO;
    private static CourseDAO crsDAO;
    private static StudentCourseResultDAO scrDAO;

    // Entities for tests
    private User student1;
    private User student2;
    private Course endedCrs;
    private Course activeCrs;
    private StudentCourseResult scrSt1EndedCrs;
    private StudentCourseResult scrSt1ActiveCrs;
    private StudentCourseResult scrSt2EndedCrs;
    private StudentCourseResult scrSt2ActiveCrs;

    private static void initDAOs() {
        usDAO = new HibernateUserDAO(sessionFactory);
        crsDAO = new HibernateCourseDAO(sessionFactory);
        scrDAO = new HibernateStudentCourseResultDAO(sessionFactory);
    }

    @BeforeAll
    public static void prepareForTest() {
        HibernateUtilForTest.clearDBTables();
        sessionFactory = HibernateUtilForTest.getSessionFactory();
        initDAOs();
    }

    private void initEntities() {
        // Init students
        AccessInfo accessInf1 = AccessInfo.builder()
                .email("email1").passwordHash("password1".hashCode())
                .build();
        AccessInfo accessInf2 = AccessInfo.builder()
                .email("email2").passwordHash("password2".hashCode())
                .build();
        student1 = User.builder().accessInfo(accessInf1).build();
        student2 = User.builder().accessInfo(accessInf2).build();

        // Init courses
        endedCrs = Course.builder().courseInfo(
                CourseInfo.builder().name("endedCrs").endDate(LocalDate.now().minusDays(2))
                        .build()).build();
        activeCrs = Course.builder().courseInfo(
                CourseInfo.builder().name("activeCrs").endDate(LocalDate.now().plusDays(2))
                        .build()).build();

        // Init SCR entities
        scrSt1EndedCrs = StudentCourseResult.builder().student(student1).course(endedCrs).build();
        scrSt1ActiveCrs = StudentCourseResult.builder().student(student1).course(activeCrs).build();
        scrSt2EndedCrs = StudentCourseResult.builder().student(student2).course(endedCrs).build();
        scrSt2ActiveCrs = StudentCourseResult.builder().student(student2).course(activeCrs).build();
    }

    private void saveEntities() {
        // Saving students
        usDAO.save(student1);
        usDAO.save(student2);

        // Saving courses
        crsDAO.save(endedCrs);
        crsDAO.save(activeCrs);

        // Saving SCR entities
        scrDAO.save(scrSt1EndedCrs);
        scrDAO.save(scrSt1ActiveCrs);
        scrDAO.save(scrSt2EndedCrs);
        scrDAO.save(scrSt2ActiveCrs);
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
    public void updateAllTest() {
        // Updating
        scrSt1EndedCrs.setResult(CourseResult.EXCELLENT);
        scrSt1ActiveCrs.setResult(CourseResult.EXCELLENT);
        scrSt2EndedCrs.setResult(CourseResult.EXCELLENT);
        scrSt2ActiveCrs.setResult(CourseResult.EXCELLENT);

        List<StudentCourseResult> expectedSCRList =
                Arrays.asList(scrSt1EndedCrs, scrSt1ActiveCrs, scrSt2EndedCrs, scrSt2ActiveCrs);
        scrDAO.updateAll(expectedSCRList);

        // Retrieving updated SCR entities from DB
        Session session = sessionFactory.openSession();
        TypedQuery<StudentCourseResult> query =
                session.createQuery("FROM StudentCourseResult scr",
                        StudentCourseResult.class);
        List<StudentCourseResult> scrListFromDB = query.getResultList();
        session.close();

        // Testing
        Assertions.assertTrue(
                TestUtil.areListsEqualIgnoringOrder(expectedSCRList, scrListFromDB),
                "Method should update all SCR entities correctly");
    }

    @Test
    public void getStudentsResultsByCourseIdTest() {
        // Retrieving SCR entities from DB
        List<StudentCourseResult> expectedSCRListStudent1 = Arrays.asList(scrSt1EndedCrs, scrSt2EndedCrs);
        List<StudentCourseResult> scrListFromDBStudent1 =
                scrDAO.getStudentsResultsByCourseId(endedCrs.getId());

        List<StudentCourseResult> expectedSCRListStudent2 = Arrays.asList(scrSt1ActiveCrs, scrSt2ActiveCrs);
        List<StudentCourseResult> scrListFromDBStudent2 =
                scrDAO.getStudentsResultsByCourseId(activeCrs.getId());

        // Testing
        Assertions.assertTrue(
                TestUtil.areListsEqualIgnoringOrder(expectedSCRListStudent1, scrListFromDBStudent1),
                "Method should find all SCR entities with required course id");
        Assertions.assertTrue(
                TestUtil.areListsEqualIgnoringOrder(expectedSCRListStudent2, scrListFromDBStudent2),
                "Method should find all SCR entities with required course id");
    }

    @Test
    public void getEndedBeforeDateCoursesWithResultsByStudentIdTest() {
        // Retrieving SCR entities from DB
        List<StudentCourseResult> scrFromDB =
                scrDAO.getEndedBeforeDateCoursesWithResultsByStudentId(
                        student1.getId(), LocalDate.now());

        // Testing
        Assertions.assertTrue(scrFromDB.size() == 1 && scrFromDB.contains(scrSt1EndedCrs),
                "Should return SCR entities with given student id and course ended before given date");
    }


}
