package com.company.manager.domain.user;

import com.company.manager.domain.archive.StudentCourseResult;
import lombok.*;

import org.hibernate.annotations.Cascade;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")

@Builder
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "access_info_id")
    private long id;

    @Embedded
    private UserInfo userInfo;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "access_info_id")
    private AccessInfo accessInfo;

    @Builder.Default
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.REMOVE})
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private final Set<StudentCourseResult> courseResults = new HashSet<>();

}
