package services.impl;

import dao.custom.UserDAO;
import dao.custom.impl.HibernateStudentCourseResultDAO;
import dao.custom.impl.HibernateUserDAO;
import domain.user.AccessInfo;
import domain.user.User;
import lombok.extern.slf4j.Slf4j;
import services.StudentCourseResultService;
import services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.Option;
import java.util.Optional;

import static servlet.access.SignInServlet.CURRENT_USER_ID_SESSION_ATTR;

@Slf4j
public class UserServiceImpl implements UserService {

    private final UserDAO dao;

    private UserServiceImpl(){
        log.info("Initializing {}", UserServiceImpl.class.getName());
        dao = new HibernateUserDAO();
        log.info("{} was initialized", UserServiceImpl.class.getName());
    }

    // Init on-demand
    private static class ServiceHolder {
        static final UserServiceImpl service = new UserServiceImpl();
    }

    public static UserServiceImpl getService() {
        return ServiceHolder.service;
    }

    @Override
    public User findUserById_OrThrowEx(long userId) {
        Optional<User> userOpt = findUserById(userId);
        if (!userOpt.isPresent()) throw new IllegalStateException("User wasn't received");
        return userOpt.get();
    }

    @Override
    public User getCurrentUserFromDB_OrThrowEx(HttpServletRequest request) throws IllegalStateException{
        Optional<User> userOpt = getCurrentUserFromDB(request);
        if (!userOpt.isPresent()) throw new IllegalStateException("User wasn't received");
        return userOpt.get();
    }

    @Override
    public long getCurrentUserIdFromSession_OrThrowEx(HttpServletRequest request) throws IllegalStateException{
        Optional<Long> userIdOpt = getCurrentUserIdFromSession(request);
        if (!userIdOpt.isPresent()) throw new IllegalStateException("User id wasn't received");
        return userIdOpt.get();
    }

    @Override
    public Optional<User> findUserByEmailAndPassword(String email, String password) {
        int passwordHash = password.hashCode();
        return dao.findUserByEmailAndPasswordHash(email, passwordHash);
    }

    @Override
    public Optional<User> getCurrentUserFromDB(HttpServletRequest request) {
        Optional<Long> currentUserIdOpt = getCurrentUserIdFromSession(request);
        if (!currentUserIdOpt.isPresent()) return Optional.empty();
        return findUserById(currentUserIdOpt.get());
    }

    @Override
    public Optional<Long> getCurrentUserIdFromSession(HttpServletRequest request) {
        return Optional.ofNullable((Long)request.getSession()
                .getAttribute(CURRENT_USER_ID_SESSION_ATTR));
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
