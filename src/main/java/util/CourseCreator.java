package util;

import domain.course.Course;
import domain.course.CourseInfo;

import java.time.LocalDate;

public class CourseCreator {

    public static Course createCourseFromStrParams(
            String name,
            String description,
            String startDateStr,
            String endDateStr,
            String timeTable,
            String uri) {

        // Converting string to date
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);

        return Course.builder()
                .courseInfo(CourseInfo.builder()
                        .name(name)
                        .description(description)
                        .startDate(startDate)
                        .endDate(endDate)
                        .timeTable(timeTable)
                        .uri(uri)
                        .build())
                .build();
    }

}
