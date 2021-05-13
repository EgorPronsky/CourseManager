package util.creators;

import domain.user.AccessInfo;
import domain.user.User;
import domain.user.UserInfo;
import domain.user.UserRole;

public class UserCreator {

    public static User createUserFromStrParams(
            String firstName,
            String lastName,
            String isTeacher,
            String email,
            String password) {

        // Get password hash
        int passwordHash = password.hashCode();

        // Define user role
        UserRole userRole = isTeacher != null ? UserRole.TEACHER : UserRole.STUDENT ;

        return User.builder()
                .userInfo(UserInfo.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .userRole(userRole)
                        .build())
                .accessInfo(AccessInfo.builder()
                        .email(email)
                        .passwordHash(passwordHash)
                        .build())
                .build();
    }

}
