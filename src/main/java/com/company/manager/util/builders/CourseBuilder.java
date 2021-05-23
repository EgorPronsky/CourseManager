package com.company.manager.util.builders;

import com.company.manager.domain.course.Course;
import com.company.manager.domain.course.CourseInfo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CourseBuilder {

    public static Course buildCourseFromStringValues (String name, String description,
                                                       String uri, String timeTable,
                                                      String startDateStr, String endDateStr, String datePattern) {
        // Converting string date values to local date
        LocalDate startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern(datePattern));
        LocalDate endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ofPattern(datePattern));

        return buildCourse(name, description, uri, timeTable, startDate, endDate);
    }

    public static Course buildCourse(String name, String description, String uri, String timeTable,
                                     LocalDate startDate, LocalDate endDate) {
        return Course.builder()
                .courseInfo(CourseInfo.builder()
                        .name(name).description(description)
                        .startDate(startDate).endDate(endDate)
                        .timeTable(timeTable).uri(uri)
                        .build()).build();
    }


}
