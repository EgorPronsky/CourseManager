package services.impl;

import dao.custom.CourseDAO;
import dao.custom.impl.HibernateCourseDAO;
import domain.course.Course;
import lombok.extern.slf4j.Slf4j;
import services.CourseService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
public class CourseServiceImpl implements CourseService {

    private final CourseDAO dao;

    private CourseServiceImpl() {
        log.info("Initializing {}", CourseServiceImpl.class.getName());
        dao = new HibernateCourseDAO();
        log.info("{} was initialized", CourseServiceImpl.class.getName());
    }

    // Init on-demand
    private static class ServiceHolder {
        static final CourseServiceImpl service = new CourseServiceImpl();
    }

    public static CourseServiceImpl getService() {
        return ServiceHolder.service;
    }

    public List<Course> getUserFutureCourses(long userId) {
        LocalDate today = LocalDate.now();
        return dao.getUserCoursesStartAfterDate(userId, today);
    }

    public List<Course> getAllCoursesById(List<Long> idList) {
        return dao.getAllById(idList);
    }

    public List<Course> getUserCurrentCourses(long userId) {
        LocalDate today = LocalDate.now();
        return dao.getUserCoursesActiveOnDate(userId, today);
    }

    /**
     * @return courses that start tomorrow or later
     */
    @Override
    public List<Course> getAllFutureCourses() {
        LocalDate today = LocalDate.now();
        return dao.getAllStartedAfterDate(today);
    }

    @Override
    public List<Course> getAllCourses() {
        LocalDate date = LocalDate.of(2000, 1, 8);
        return dao.getAllStartedAfterDate(date);
    }

    /**
     * @return user finished and ungraded courses
     */
    @Override
    public List<Course> getUserFinishedUngradedCourses(long userId) {
        LocalDate today = LocalDate.now();
        return dao.getUserUngradedCoursesEndedBeforeDate(userId, today);
    }

    @Override
    public Optional<Course> getCourseById(long id) {
        return dao.findById(id);
    }

    @Override
    public void saveCourse(Course course) {
        dao.save(course);
    }

    @Override
    public void updateCourse(Course course) {
        dao.update(course);
    }

}
