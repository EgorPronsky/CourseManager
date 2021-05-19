package com.company.manager.domain.archive;

import com.company.manager.domain.course.Course;
import com.company.manager.domain.user.Student;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourseResultId implements Serializable {
    private Student student;
    private Course course;
}
