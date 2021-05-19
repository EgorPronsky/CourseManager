package com.company.manager.domain.user;

import com.company.manager.domain.course.Course;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "teachers")

@Builder
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "user_id")
    private long id;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
    private final Set<Course> courses = new HashSet<>();

}
