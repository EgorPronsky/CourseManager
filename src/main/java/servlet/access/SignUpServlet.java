package servlet.access;

import services.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import domain.user.User;
import util.creators.UserCreator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static filter.SessionFilter.APP_DOMAIN_NAME;
import static filter.SignInFilter.EMAIL_PARAM;
import static filter.SignInFilter.PASSWORD_PARAM;

@Slf4j
public class SignUpServlet extends HttpServlet {

    // Parameter names
    public static final String FIRST_NAME_PARAM = "first_name";
    public static final String LAST_NAME_PARAM = "last_name";
    public static final String IS_TEACHER_PARAM = "is_teacher";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving parameters from request");
        String firstName = request.getParameter(FIRST_NAME_PARAM);
        String lastName  = request.getParameter(LAST_NAME_PARAM);
        String isTeacher = request.getParameter(IS_TEACHER_PARAM);
        String email     = request.getParameter(EMAIL_PARAM);
        String password  = request.getParameter(PASSWORD_PARAM);

        log.debug("Creating new user from received params");
        User user = UserCreator.createUserFromStrParams(
                firstName, lastName, isTeacher,
                email, password
        );

        log.debug("Saving new user");
        UserServiceImpl.getService().saveUser(user);

        response.sendRedirect(String.format("/%s/login-page", APP_DOMAIN_NAME));
    }

}
