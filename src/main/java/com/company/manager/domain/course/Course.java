package com.company.manager.domain.course;

import com.company.manager.domain.archive.StudentCourseResult;
import com.company.manager.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "courses")

@Builder
@Getter
@Setter
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
    private User teacher;

    @Builder.Default
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final Set<StudentCourseResult> studentResults = new HashSet<>();




}
