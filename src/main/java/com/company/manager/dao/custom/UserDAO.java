package com.company.manager.dao.custom;

import com.company.manager.dao.generic.GenericDAO;
import com.company.manager.domain.user.User;

import java.util.Optional;

public interface UserDAO extends GenericDAO<User> {

    Optional<User> findUserByEmailAndPasswordHash(String email, int passwordHash);
}
