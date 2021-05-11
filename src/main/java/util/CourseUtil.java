package util;

import domain.course.Course;
import domain.course.CourseInfo;
import domain.course.CourseState;
import domain.user.User;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public class CourseUtil {

    public static List<Course> getFilteredCourses(Collection<Course> courses, CourseState state) {
        LocalDate today = LocalDate.now();
        Predicate<Course> filterStrategy;

        // Define filter strategy
        switch (state) {
            case CURRENT: {
                filterStrategy = (course ->
                        today.isAfter(course.getCourseInfo().getStartDate()) &&
                                today.isBefore(course.getCourseInfo().getEndDate()));
                break;
            }
            case FUTURE: {
                filterStrategy = (course ->
                        today.isBefore(course.getCourseInfo().getStartDate()));
                break;
            }
            case FINISHED: {
                filterStrategy = (course ->
                        today.isAfter(course.getCourseInfo().getStartDate()));
                break;
            }
            default: {
                throw new IllegalArgumentException("Can't define filter strategy to the given course state");
            }
        }

        return courses.stream()
                .filter(filterStrategy)
                .collect(Collectors.toList());
    }

}
