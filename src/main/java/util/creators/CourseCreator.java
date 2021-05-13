package util.creators;

import domain.course.Course;
import domain.course.CourseInfo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CourseCreator {

    private static final String datePattern = "dd-MM-yyyy";

    public static Course createCourseFromStrParams(
            String name,
            String description,
            String startDateStr,
            String endDateStr,
            String timeTable,
            String uri) {

        // Converting string to date
        LocalDate startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern(datePattern));
        LocalDate endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ofPattern(datePattern));

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
