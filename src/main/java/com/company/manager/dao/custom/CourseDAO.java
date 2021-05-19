package com.company.manager.dao.custom;

import com.company.manager.dao.generic.GenericDAO;
import com.company.manager.domain.course.Course;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface CourseDAO extends GenericDAO<Course> {

    void saveOrUpdate(Course course);
    List<Course> getCoursesById(Collection<Long> idCollection);

    // For students
    List<Course> getStudentCoursesActiveOnDate(long studentId, LocalDate date);
    List<Course> getStudentCoursesStartAfterDate(long studentId, LocalDate date);
    List<Course> getStudentNotSubscribedCoursesStartAfterDate(long studentId, LocalDate date);
    List<Course> getStudentUngradedCoursesEndedBeforeDate(long studentId, LocalDate date);

    // For teachers
    List<Course> getTeacherCoursesActiveOnDate(long teacherId, LocalDate date);
    List<Course> getTeacherCoursesStartAfterDate(long teacherId, LocalDate date);
    List<Course> getTeacherNotGradedCoursesEndedBeforeDate(long teacherId, LocalDate date);

}
