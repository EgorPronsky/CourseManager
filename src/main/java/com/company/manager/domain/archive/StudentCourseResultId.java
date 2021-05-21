package com.company.manager.domain.archive;

import com.company.manager.domain.course.Course;
import com.company.manager.domain.user.User;
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
public class StudentCourseResultId implements Serializable {
    private User student;
    private Course course;
}
