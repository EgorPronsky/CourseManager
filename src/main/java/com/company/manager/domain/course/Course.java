package com.company.manager.domain.course;

import com.company.manager.domain.user.Student;
import com.company.manager.domain.user.Teacher;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "courses")

@Builder
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @EqualsAndHashCode.Include
    private long id;

    @Embedded
    private CourseInfo courseInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "students_courses",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private final Set<Student> students = new HashSet<>();

}
