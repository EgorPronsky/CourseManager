package com.company.manager.domain.user;

import com.company.manager.domain.user.info.AccessInfo;
import com.company.manager.domain.user.info.UserInfo;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "users")

@Builder
@Getter
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

}
