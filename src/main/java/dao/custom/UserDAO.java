package dao.custom;

import dao.generic.GenericDAO;
import domain.user.User;

import java.util.Optional;

public interface UserDAO extends GenericDAO<User> {

    Optional<User> findUserByEmailAndPasswordHash(String email, int passwordHash);
}
