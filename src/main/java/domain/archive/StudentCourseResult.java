package domain.archive;

import domain.course.Course;
import domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "archive")

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourseResult {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @OneToOne
    private User student;

    @OneToOne
    private Course course;

    private CourseResult result;

}
