package service;

import dao.custom.UserDAO;
import dao.custom.impl.HibernateUserDAO;
import domain.user.AccessInfo;
import domain.user.User;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class UserService {

    private final UserDAO dao;

    private UserService(){
        dao = new HibernateUserDAO();
    }

    // Lazy init
    private static class ServiceHolder {
        static final UserService service = new UserService();
    }

    public static UserService getService() {
        return ServiceHolder.service;
    }

    public Optional<User> findUserByEmailAndPassword(String email, String password) {
        // Try to find access info
        Optional<AccessInfo> accessInfo = AccessInfoService.getService()
                .findAccessInfoByEmailAndPassword(email, password);

        if (!accessInfo.isPresent()) return Optional.empty();

        return dao.findUserByAccessInfoId(accessInfo.get().getId());
    }

    public void save(User user) {
        dao.save(user);
    }

    public void merge(User user) {
        dao.merge(user);
    }

}
