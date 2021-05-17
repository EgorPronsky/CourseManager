package services.impl;

import dao.custom.CourseDAO;
import dao.custom.impl.HibernateCourseDAO;
import domain.course.Course;
import lombok.extern.slf4j.Slf4j;
import services.CourseService;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.*;

@Slf4j
public class CourseServiceImpl implements CourseService {

    private static final CourseServiceImpl service;
    private final CourseDAO dao;

    private CourseServiceImpl() {
        log.info("Initializing {}", CourseServiceImpl.class.getName());
        dao = new HibernateCourseDAO();
        log.info("{} was initialized", CourseServiceImpl.class.getName());
    }

    static {
        service = new CourseServiceImpl();
    }

    public static CourseServiceImpl getService() {
        return service;
    }

    public List<Course> getUserFutureCourses(long userId) {
        LocalDate today = LocalDate.now();
        return dao.getUserCoursesStartAfterDate(userId, today);
    }

    public List<Course> getUserCurrentCourses(long userId) {
        LocalDate today = LocalDate.now();
        return dao.getUserCoursesActiveOnDate(userId, today);
    }

    /**
     * @return student finished but ungraded courses
     */
    @Override
    public List<Course> getStudentFinishedUngradedCourses(long userId) {
        LocalDate today = LocalDate.now();
        return dao.getStudentUngradedCoursesEndedBeforeDate(userId, today);
    }

    /**
     * @return teacher finished but ungraded courses
     */
    @Override
    public List<Course> getTeacherFinishedNotGradedCourses(long userId) {
        LocalDate today = LocalDate.now();
        return dao.getTeacherNotGradedCoursesEndedBeforeDate(userId, today);
    }

    @Override
    public List<Course> getStudentAvailableToJoinCourses(long userId) {
        LocalDate today = LocalDate.now();
        return dao.getStudentNotSubscribedCoursesStartAfterDate(userId, today);
    }

    @Override
    public Course getCourseById_OrThrowEx(long courseId) {
        Optional<Course> courseOpt = getCourseById(courseId);
        if (!courseOpt.isPresent()) throw new InvalidParameterException("Course wasn't found");
        return courseOpt.get();
    }

    @Override
    public Optional<Course> getCourseById(long courseId) {
        return dao.findById(courseId);
    }

    @Override
    public List<Course> getCoursesById(Collection<Long> idCollection) {
        return dao.getCoursesById(idCollection);
    }

    @Override
    public void saveOrUpdateCourse(Course course) {
        dao.saveOrUpdate(course);
    }

    @Override
    public void deleteCourse(Course course) {
        dao.delete(course);
    }

}
