package services;

import domain.course.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    void saveCourse(Course course);
    void updateCourse(Course course);
    Optional<Course> getCourseById(long courseId);

    List<Course> getUserFutureCourses(long userId);
    List<Course> getUserCurrentCourses(long userId);
    List<Course> getAllCoursesById(List<Long> idList);
    List<Course> getUserFinishedUngradedCourses(long userId);
    List<Course> getAllFutureCourses();
    List<Course> getAllCourses();

}
