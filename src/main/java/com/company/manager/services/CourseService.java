package com.company.manager.services;

import com.company.manager.domain.course.Course;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CourseService {

    void saveOrUpdateCourse(Course course);
    void deleteCourse(Course course);
    Optional<Course> findCourseById(long courseId);
    Course getCourseById(long courseId);
    List<Course> getCoursesById(Collection<Long> idCollection);

    // For students
    List<Course> getStudentFutureCourses(long studentId);
    List<Course> getStudentCurrentCourses(long studentId);
    List<Course> getStudentAvailableToJoinCourses(long studentId);

    // For teachers
    List<Course> getTeacherFutureCourses(long teacherId);
    List<Course> getTeacherCurrentCourses(long teacherId);
    List<Course> getTeacherFinishedNotGradedCourses(long teacherId);


}
