package com.company.manager.services.impl;

import com.company.manager.dao.custom.UserDAO;
import com.company.manager.dao.custom.impl.HibernateUserDAO;
import com.company.manager.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import com.company.manager.services.UserService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
public class UserServiceImpl implements UserService {

    private static final UserServiceImpl service;
    private final UserDAO dao;

    private UserServiceImpl(){
        log.info("Initializing {}", UserServiceImpl.class.getName());
        dao = new HibernateUserDAO();
        log.info("{} was initialized", UserServiceImpl.class.getName());
    }

    static {
        service = new UserServiceImpl();
    }

    public static UserServiceImpl getService() {
        return service;
    }

    @Override
    public User getUserById(long userId) throws IllegalStateException {
        return findUserById(userId)
                .orElseThrow(() -> new IllegalStateException("User wasn't received by given id"));
    }

    @Override
    public Optional<User> findUserByEmailAndPassword(String email, String password) {
        int passwordHash = password.hashCode();
        return dao.findUserByEmailAndPasswordHash(email, passwordHash);
    }

    @Override
    public Optional<User> findUserById(long userId) {
        return dao.findById(userId);
    }

    @Override
    public void saveUser(User user) {
        dao.save(user);
    }

    @Override
    public void updateUser(User user) {
        dao.update(user);
    }

}
