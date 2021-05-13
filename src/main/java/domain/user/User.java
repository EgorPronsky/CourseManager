package domain.user;

import domain.course.Course;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Embedded
    private UserInfo userInfo;

    @OneToOne(cascade = CascadeType.ALL)
    private AccessInfo accessInfo;

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<Course> courses;

}
