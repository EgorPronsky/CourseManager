package com.company.manager.domain.archive;

import com.company.manager.domain.course.Course;
import com.company.manager.domain.user.User;
import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter @Setter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourseResultId implements Serializable {
    private User student;
    private Course course;
}
