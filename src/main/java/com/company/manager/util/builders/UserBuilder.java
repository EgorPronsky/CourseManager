package com.company.manager.util.builders;

import com.company.manager.domain.user.AccessInfo;
import com.company.manager.domain.user.User;
import com.company.manager.domain.user.UserInfo;
import com.company.manager.domain.user.UserRole;

public class UserBuilder {

    public static User buildUser(String firstName, String lastName,
                                 UserRole userRole, String email, int passwordHash) {
        return User.builder()
                .accessInfo(AccessInfo.builder()
                        .email(email)
                        .passwordHash(passwordHash)
                        .build())
                .userInfo(UserInfo.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .userRole(userRole)
                        .build())
                .build();
    }

}
