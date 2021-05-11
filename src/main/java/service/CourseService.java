package service;

import dao.custom.CourseDAO;
import dao.custom.impl.HibernateCourseDAO;
import domain.course.Course;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
public class CourseService {

    private final CourseDAO dao;

    private CourseService() {
        dao = new HibernateCourseDAO();
    }

    // Lazy init
    private static class ServiceHolder {
        static final CourseService service = new CourseService();
    }

    public static CourseService getService() {
        return ServiceHolder.service;
    }

    public List<Course> getAllCoursesById(List<Long> idList) {
        return dao.getAllById(idList);
    }

    /**
     * @return courses that start tomorrow or later
     */
    public List<Course> getAllFutureCourses() {
        LocalDate today = LocalDate.now();
        return dao.getAllStartedAfterDate(today);
    }

    /**
     * @return user finished and ungraded courses
     */
    public List<Course> getUserFinishedUngradedCourses(long userId) {
        LocalDate today = LocalDate.now();
        return dao.getUserUngradedCoursesEndedBeforeDate(userId, today);
    }

    public Optional<Course> getCourseById(long id) {
        return dao.findById(id);
    }

    public void save(Course course) {
        dao.save(course);
    }

    public void merge(Course course) {
        dao.merge(course);
    }

}
