package com.company.manager.domain.user;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "access_info")

@Builder
@Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class AccessInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @EqualsAndHashCode.Include
    private long id;

    @Column(unique = true)
    private String email;

    @Column(name = "password_hash")
    private int passwordHash;

}
