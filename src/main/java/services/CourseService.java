package services;

import domain.course.Course;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CourseService {

    void saveOrUpdateCourse(Course course);
    void deleteCourse(Course course);
    Optional<Course> getCourseById(long courseId);
    List<Course> getCoursesById(Collection<Long> idCollection);

    List<Course> getUserFutureCourses(long userId);
    List<Course> getUserCurrentCourses(long userId);
    List<Course> getStudentFinishedUngradedCourses(long userId);
    List<Course> getTeacherFinishedNotGradedCourses(long userId);
    List<Course> getStudentAvailableToJoinCourses(long userId);

    Course getCourseById_OrThrowEx(long courseId);

}
