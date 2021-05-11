package domain.user;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "access_info")

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccessInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private long id;

    private String email;

    @Column(name = "password_hash")
    private int passwordHash;

}