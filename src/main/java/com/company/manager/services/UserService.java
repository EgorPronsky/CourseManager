package com.company.manager.services;

import com.company.manager.domain.user.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserService {

    void saveUser(User user);
    void updateUser(User user);
    Optional<User> findUserById(long userId);
    void updateAllUsers(Collection<User> users);
    List<User> getUsersById(Collection<Long> usersId);

    Optional<User> getCurrentUserFromDB(HttpServletRequest request);
    Optional<Long> getCurrentUserIdFromSession(HttpServletRequest request);
    Optional<User> findUserByEmailAndPassword(String email, String password);

    User getUserById(long userId);
    User getCurrentUserFromDB_OrThrowEx(HttpServletRequest request);
    long getCurrentUserIdFromSession_OrThrowEx(HttpServletRequest request);

}
