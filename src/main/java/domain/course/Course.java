package domain.course;

import lombok.*;
import domain.user.User;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

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

    @ManyToMany(mappedBy = "courses", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<User> teacherAndStudents;

    public User getTeacher() {
        // Teacher is always first in a list
        return teacherAndStudents.size() == 0 ? null : teacherAndStudents.get(0);
    }

    public List<User> getStudentsList() {
        return teacherAndStudents.size() < 2 ?
                Collections.emptyList() : teacherAndStudents.subList(1, teacherAndStudents.size());
    }
}
