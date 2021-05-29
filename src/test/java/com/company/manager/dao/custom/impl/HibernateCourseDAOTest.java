package com.company.manager.dao.custom.impl;

import com.company.manager.dao.custom.CourseDAO;
import com.company.manager.dao.custom.StudentCourseResultDAO;
import com.company.manager.dao.custom.UserDAO;
import com.company.manager.domain.archive.StudentCourseResult;
import com.company.manager.domain.course.Course;
import com.company.manager.domain.course.CourseInfo;
import com.company.manager.domain.user.AccessInfo;
import com.company.manager.domain.user.User;
import com.company.manager.util.HibernateUtilForTest;
import com.company.manager.util.TestUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class HibernateCourseDAOTest {

    private static SessionFactory sessionFactory;

    // DAOs
    private static UserDAO usDAO;
    private static CourseDAO crsDAO;
    private static StudentCourseResultDAO scrDAO;

    // Entities for tests
    private User student1;
    private User student2;
    private User teacher1;
    private User teacher2;
    private Course endedCrs;
    private Course activeCrs;
    private Course futureCrs;
    private Course noStudentsFutureCrs;
    private StudentCourseResult scrSt1EndedCrs;
    private StudentCourseResult scrSt1FutureCrs;
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
        AccessInfo accessInfStudent1 = AccessInfo.builder()
                .email("emailSt1").passwordHash("password".hashCode())
                .build();
        AccessInfo accessInfStudent2 = AccessInfo.builder()
                .email("emailSt2").passwordHash("password".hashCode())
                .build();
        student1 = User.builder().accessInfo(accessInfStudent1).build();
        student2 = User.builder().accessInfo(accessInfStudent2).build();

        // Init teachers
        AccessInfo accessInfTeacher1 = AccessInfo.builder()
                .email("emailTch1").passwordHash("password".hashCode())
                .build();
        AccessInfo accessInfTeacher2 = AccessInfo.builder()
                .email("emailTch2").passwordHash("password".hashCode())
                .build();
        teacher1 = User.builder().accessInfo(accessInfTeacher1).build();
        teacher2 = User.builder().accessInfo(accessInfTeacher2).build();

        // Init courses
        endedCrs = Course.builder().teacher(teacher1).courseInfo(
                CourseInfo.builder().name("endedCrs")
                        .endDate(LocalDate.now().minusDays(2))
                        .build()).build();
        activeCrs = Course.builder().teacher(teacher2).courseInfo(
                CourseInfo.builder().name("activeCrs")
                        .startDate(LocalDate.now().minusDays(2))
                        .endDate(LocalDate.now().plusDays(2))
                        .build()).build();
        futureCrs = Course.builder().teacher(teacher1).courseInfo(
                CourseInfo.builder().name("futureCrs")
                        .startDate(LocalDate.now().plusDays(2))
                        .build()).build();
        noStudentsFutureCrs = Course.builder().courseInfo(
                CourseInfo.builder().name("noStudentsFutureCrs")
                        .startDate(LocalDate.now().plusDays(2))
                        .build()).build();

        // Init SCR entities
        scrSt1EndedCrs = StudentCourseResult.builder().student(student1).course(endedCrs).build();
        scrSt1FutureCrs = StudentCourseResult.builder().student(student1).course(futureCrs).build();
        scrSt2ActiveCrs = StudentCourseResult.builder().student(student2).course(activeCrs).build();
    }

    private void saveEntities() {
        // Saving students
        usDAO.save(student1);
        usDAO.save(student2);
        usDAO.save(teacher1);
        usDAO.save(teacher2);

        // Saving courses
        crsDAO.save(endedCrs);
        crsDAO.save(futureCrs);
        crsDAO.save(activeCrs);
        crsDAO.save(noStudentsFutureCrs);

        // Saving SCR entities
        scrDAO.save(scrSt1EndedCrs);
        scrDAO.save(scrSt1FutureCrs);
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
    public void saveOrUpdateTest() {
        // Finding saved entity
        Optional<Course> activeCrsFromDB = crsDAO.findById(activeCrs.getId());
        // Testing
        Assertions.assertEquals(activeCrs,activeCrsFromDB.get(),
                "Method should save Course entity correctly");

        // Updating
        activeCrs.getCourseInfo().setName("updatedCourseName");
        crsDAO.saveOrUpdate(activeCrs);
        // Testing
        Assertions.assertEquals(activeCrs,activeCrsFromDB.get(),
                "Method should update Course entity correctly");
    }

    @Test
    public void getCoursesByIdTest() {
        // Prepare
        List<Course> expectedCrsList = Arrays.asList(activeCrs, futureCrs);
        List<Long> crsIdList = Arrays.asList(activeCrs.getId(), futureCrs.getId());
        // Receiving from DB
        List<Course> crsListFromDB = crsDAO.getCoursesById(crsIdList);
        // Testing
        Assertions.assertTrue(
                TestUtil.areListsEqualIgnoringOrder(expectedCrsList, crsListFromDB),
                "Method should correctly find all required courses by id");
    }

    @Test
    public void getStudentCoursesActiveOnDateTest() {
        // Prepare
        List<Course> expectedSt2CrsList = Arrays.asList(activeCrs);

        // Receiving from DB
        List<Course> st1CrsListFromDB = crsDAO
                .getStudentCoursesActiveOnDate(student1.getId(), LocalDate.now());
        List<Course> st2CrsListFromDB = crsDAO
                .getStudentCoursesActiveOnDate(student2.getId(), LocalDate.now());

        // Testing
        Assertions.assertEquals(Collections.emptyList(), st1CrsListFromDB,
                "If student has no active courses, should return empty list");
        Assertions.assertTrue(
                TestUtil.areListsEqualIgnoringOrder(expectedSt2CrsList, st2CrsListFromDB),
                "Should return only active courses of required student");
    }

    @Test
    public void getStudentCoursesStartAfterDateTest() {
        // Prepare
        List<Course> expectedSt1CrsList = Arrays.asList(futureCrs);

        // Receiving from DB
        List<Course> st1CrsListFromDB = crsDAO
                .getStudentCoursesStartAfterDate(student1.getId(), LocalDate.now());
        List<Course> st2CrsListFromDB = crsDAO
                .getStudentCoursesStartAfterDate(student2.getId(), LocalDate.now());

        // Testing
        Assertions.assertTrue(
                TestUtil.areListsEqualIgnoringOrder(expectedSt1CrsList, st1CrsListFromDB),
                "Should return only future courses of required student");
        Assertions.assertEquals(Collections.emptyList(), st2CrsListFromDB,
                "If student has no future courses, should return empty list");
    }

    @Test
    public void getStudentNotSubscribedCoursesStartAfterDateTest() {
        // Prepare
        List<Course> expectedSt1CrsList = Arrays.asList(noStudentsFutureCrs);
        // Receiving from DB
        List<Course> st1CrsListFromDB = crsDAO
                .getStudentNotSubscribedCoursesStartAfterDate(student1.getId(), LocalDate.now());
        // Testing
        Assertions.assertTrue(
                TestUtil.areListsEqualIgnoringOrder(expectedSt1CrsList, st1CrsListFromDB),
                "Should return only not subscribed future courses of required student");
    }

    @Test
    public void getTeacherCoursesActiveOnDateTest() {
        // Prepare
        List<Course> expectedT2CrsList = Arrays.asList(activeCrs);

        // Receiving from DB
        List<Course> t1CrsListFromDB = crsDAO
                .getTeacherCoursesActiveOnDate(teacher1.getId(), LocalDate.now());
        List<Course> t2CrsListFromDB = crsDAO
                .getTeacherCoursesActiveOnDate(teacher2.getId(), LocalDate.now());

        // Testing
        Assertions.assertEquals(Collections.emptyList(), t1CrsListFromDB,
                "If teacher has no active courses, should return empty list");
        Assertions.assertTrue(
                TestUtil.areListsEqualIgnoringOrder(expectedT2CrsList, t2CrsListFromDB),
                "Should return only active courses of required teacher");
    }

    @Test
    public void getTeacherCoursesStartAfterDateTest() {
        // Prepare
        List<Course> expectedT1CrsList = Arrays.asList(futureCrs);

        // Receiving from DB
        List<Course> t1CrsListFromDB = crsDAO
                .getTeacherCoursesStartAfterDate(teacher1.getId(), LocalDate.now());
        List<Course> t2CrsListFromDB = crsDAO
                .getTeacherCoursesStartAfterDate(teacher2.getId(), LocalDate.now());

        // Testing
        Assertions.assertEquals(Collections.emptyList(), t2CrsListFromDB,
                "If teacher has no future courses, should return empty list");
        Assertions.assertTrue(
                TestUtil.areListsEqualIgnoringOrder(expectedT1CrsList, t1CrsListFromDB),
                "Should return only future courses of required teacher");
    }

    @Test
    public void getTeacherNotGradedCoursesEndedBeforeDateTest() {
        // Prepare
        List<Course> expectedT1CrsList = Arrays.asList(endedCrs);
        // Receiving from DB
        List<Course> t1CrsListFromDB = crsDAO
                .getTeacherNotGradedCoursesEndedBeforeDate(teacher1.getId(), LocalDate.now());
        // Testing
        Assertions.assertTrue(
                TestUtil.areListsEqualIgnoringOrder(expectedT1CrsList, t1CrsListFromDB),
                "Should return only not graded courses of required teacher");
    }

}
