package com.company.manager.dao.custom.impl;

import com.company.manager.dao.custom.CourseDAO;
import com.company.manager.dao.custom.StudentCourseResultDAO;
import com.company.manager.dao.custom.UserDAO;
import com.company.manager.domain.archive.CourseResult;
import com.company.manager.domain.archive.StudentCourseResult;
import com.company.manager.domain.archive.StudentCourseResultId;
import com.company.manager.domain.course.Course;
import com.company.manager.domain.course.CourseInfo;
import com.company.manager.domain.user.AccessInfo;
import com.company.manager.domain.user.User;
import com.company.manager.util.HibernateUtilForTest;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;

import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.temporal.TemporalAmount;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class HibernateStudentCourseResultDAOTest {

    private final SessionFactory sessionFactory =
            HibernateUtilForTest.getSessionFactory();

    // Entities for tests
    private AccessInfo accessInf1;
    private AccessInfo accessInf2;
    private User student1;
    private User student2;
    private Course crs1;
    private Course crs2;
    private StudentCourseResult scr1_1;
    private StudentCourseResult scr1_2;
    private StudentCourseResult scr2_1;
    private StudentCourseResult scr2_2;

    // DAOs
    private UserDAO usDAO;
    private CourseDAO crsDAO;
    private StudentCourseResultDAO scrDAO;

    // Init entities
    {
        accessInf1 = AccessInfo.builder()
                .email("email1").passwordHash("password1".hashCode())
                .build();
        accessInf2 = AccessInfo.builder()
                .email("email2").passwordHash("password2".hashCode())
                .build();
        student1 = User.builder().accessInfo(accessInf1).build();
        student2 = User.builder().accessInfo(accessInf2).build();

        crs1 = Course.builder().courseInfo(
                CourseInfo.builder().name("crs1").endDate(LocalDate.now().minusDays(2))
                        .build()).build();
        crs2 = Course.builder().courseInfo(
                CourseInfo.builder().name("crs2").endDate(LocalDate.now().plusDays(2))
                        .build()).build();

        scr1_1 = StudentCourseResult.builder().student(student1).course(crs1).build();
        scr1_2 = StudentCourseResult.builder().student(student1).course(crs2).build();
        scr2_1 = StudentCourseResult.builder().student(student2).course(crs1).build();
        scr2_2 = StudentCourseResult.builder().student(student2).course(crs2).build();
    }

    // Init DAOs
    {
        usDAO = new HibernateUserDAO(sessionFactory);
        crsDAO = new HibernateCourseDAO(sessionFactory);
        scrDAO = new HibernateStudentCourseResultDAO(sessionFactory);
    }

    @BeforeAll
    public static void prepareForTest() {
        HibernateUtilForTest.clearDBTables();
    }

    @BeforeEach
    public void saveEntities() {
        // Saving students
        usDAO.save(student1);
        usDAO.save(student2);

        // Saving courses
        crsDAO.save(crs1);
        crsDAO.save(crs2);

        // Saving SCR entities
        scrDAO.save(scr1_1);
        scrDAO.save(scr1_2);
        scrDAO.save(scr2_1);
        scrDAO.save(scr2_2);
    }

    @AfterEach
    public void clearAfterTest() {
        HibernateUtilForTest.clearDBTables();
    }

    @Test
    public void updateAllTest() {
        // Updating
        scr1_1.setResult(CourseResult.EXCELLENT);
        scr1_2.setResult(CourseResult.EXCELLENT);
        scr2_1.setResult(CourseResult.EXCELLENT);
        scr2_2.setResult(CourseResult.EXCELLENT);

        List<StudentCourseResult> scrList =
                Arrays.asList(scr1_1, scr1_2, scr2_1, scr2_2);
        scrDAO.updateAll(scrList);

        // Retrieving updated SCR entities from DB
        Session session = sessionFactory.openSession();
        TypedQuery<StudentCourseResult> query =
                session.createQuery("FROM StudentCourseResult scr",
                        StudentCourseResult.class);
        List<StudentCourseResult> scrListFromDB = query.getResultList();
        session.close();

        // Testing
        Assertions.assertTrue(scrList.size() == scrListFromDB.size() &&
                scrList.containsAll(scrListFromDB) && scrListFromDB.containsAll(scrList),
                "Method should update all SCR entities correctly");
    }

    @Test
    public void getStudentsResultsByCourseIdTest() {
        // Retrieving SCR entities from DB
        List<StudentCourseResult> scr1 = Arrays.asList(scr1_1, scr2_1);
        List<StudentCourseResult> scr1FromDB =
                scrDAO.getStudentsResultsByCourseId(crs1.getId());

        List<StudentCourseResult> scr2 = Arrays.asList(scr1_2, scr2_2);
        List<StudentCourseResult> scr2FromDB =
                scrDAO.getStudentsResultsByCourseId(crs2.getId());

        // Testing
        Assertions.assertTrue(scr1.size() == scr1FromDB.size() &&
                        scr1.containsAll(scr1FromDB) && scr1.containsAll(scr1FromDB),
                "Method should find all SCR entities with required course id");
        Assertions.assertTrue(scr2.size() == scr2FromDB.size() &&
                        scr2.containsAll(scr2FromDB) && scr2.containsAll(scr2FromDB),
                "Method should find all SCR entities with required course id");
    }

    @Test
    public void getEndedBeforeDateCoursesWithResultsByStudentIdTest() {
        // Retrieving SCR entities from DB
        List<StudentCourseResult> scrFromDB =
                scrDAO.getEndedBeforeDateCoursesWithResultsByStudentId(
                        student1.getId(), LocalDate.now());

        // Testing
        Assertions.assertTrue(scrFromDB.size() == 1 && scrFromDB.contains(scr1_1),
                "Should return SCR entities with given student id and course ended before given date");
    }


}
