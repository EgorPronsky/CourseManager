package com.company.manager.domain.course;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Embeddable

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseInfo {

    private String name;

    @Type(type="text")
    private String description;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "time_table")
    @Type(type="text")
    private String timeTable;

    @Type(type="text")
    private String uri;

}
