package com.company.manager.domain.archive;

import com.company.manager.domain.course.Course;
import com.company.manager.domain.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "archive")

@Builder
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@IdClass(StudentCourseResultId.class)
public class StudentCourseResult {

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    @EqualsAndHashCode.Include
    private User student;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    @EqualsAndHashCode.Include
    private Course course;

    @Enumerated(EnumType.STRING)
    private CourseResult result;

}
