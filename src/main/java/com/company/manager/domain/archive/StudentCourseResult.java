package com.company.manager.domain.archive;

import com.company.manager.domain.course.Course;
import com.company.manager.domain.user.Student;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

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
    @OneToOne
    @JoinColumn(name = "student_id")
    @EqualsAndHashCode.Include
    private Student student;

    @Id
    @OneToOne
    @JoinColumn(name = "course_id")
    @EqualsAndHashCode.Include
    private Course course;

    @Enumerated(EnumType.STRING)
    private CourseResult result;

}
