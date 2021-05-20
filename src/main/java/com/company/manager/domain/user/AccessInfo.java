package com.company.manager.domain.user;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "access_info")

@Builder
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class AccessInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @EqualsAndHashCode.Include
    private long id;

    private String email;

    @Column(name = "password_hash")
    private int passwordHash;

}
