package com.company.manager.services;

import com.company.manager.domain.user.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserService {

    void saveUser(User user);
    void updateUser(User user);
    void updateAllUsers(Collection<User> users);
    Optional<User> findUserById(long userId);

    User getUserById(long userId);
    List<User> getUsersById(Collection<Long> usersId);

    Optional<User> findUserByEmailAndPassword(String email, String password);


}
