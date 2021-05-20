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
    User getUserById(long userId);

    Optional<User> findUserByEmailAndPassword(String email, String password);

}
