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
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static servlet.access.SignInServlet.CURRENT_USER_ID_SESSION_ATTR;

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
    public User findUserById_OrThrowEx(long userId) {
        return findUserById(userId)
                .orElseThrow(() -> new IllegalStateException("User wasn't found by id"));
    }

    @Override
    public User getCurrentUserFromDB_OrThrowEx(HttpServletRequest request) throws IllegalStateException{
        return getCurrentUserFromDB(request)
                .orElseThrow(() -> new IllegalStateException("User wasn't received"));
    }

    @Override
    public long getCurrentUserIdFromSession_OrThrowEx(HttpServletRequest request) throws IllegalStateException{
        return getCurrentUserIdFromSession(request)
                .orElseThrow(() -> new IllegalStateException("User id wasn't received from session"));
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
        return Optional.ofNullable((Long)request.getSession(false)
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

    @Override
    public void updateAllUsers(Collection<User> users) {
        dao.updateUsers(users);
    }

    @Override
    public List<User> getUsersById(Collection<Long> usersId) {
        return dao.getUsersById(usersId);
    }

}
