package com.company.manager.domain.user;

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
