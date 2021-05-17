package dao.custom;

import dao.generic.GenericDAO;
import domain.course.Course;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface CourseDAO extends GenericDAO<Course> {

    void saveOrUpdate(Course course);
    List<Course> getCoursesById(Collection<Long> idCollection);

    List<Course> getUserCoursesActiveOnDate(long userId, LocalDate date);
    List<Course> getUserCoursesStartAfterDate(long userId, LocalDate date);
    List<Course> getStudentNotSubscribedCoursesStartAfterDate(long userId, LocalDate date);
    List<Course> getStudentUngradedCoursesEndedBeforeDate(long userId, LocalDate date);
    List<Course> getTeacherNotGradedCoursesEndedBeforeDate(long userId, LocalDate date);

}
