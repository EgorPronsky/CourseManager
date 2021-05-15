package dao.custom;

import dao.generic.GenericDAO;
import domain.course.Course;

import java.time.LocalDate;
import java.util.List;

public interface CourseDAO extends GenericDAO<Course> {

    List<Course> getAllById(List<Long> idList);
    List<Course> getAllStartedAfterDate(LocalDate date);
    List<Course> getUserCoursesActiveOnDate(long userId, LocalDate date);
    List<Course> getUserCoursesStartAfterDate(long userId, LocalDate date);

    List<Course> getStudentUngradedCoursesEndedBeforeDate(long userId, LocalDate date);
    List<Course> getTeacherUngradedCoursesEndedBeforeDate(long userId, LocalDate date);

}
