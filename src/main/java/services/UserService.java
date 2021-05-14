package services;

import domain.user.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface UserService {

    void saveUser(User user);
    void updateUser(User user);
    Optional<User> findUserById(long userId);

    Optional<User> getCurrentUserFromDB(HttpServletRequest request);
    Optional<Long> getCurrentUserIdFromSession(HttpServletRequest request);
    Optional<User> findUserByEmailAndPassword(String email, String password);

    User findUserById_OrThrowEx(long userId);
    User getCurrentUserFromDB_OrThrowEx(HttpServletRequest request);
    long getCurrentUserIdFromSession_OrThrowEx(HttpServletRequest request);

}
