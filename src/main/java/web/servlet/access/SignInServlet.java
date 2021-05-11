package web.servlet.access;

import lombok.extern.slf4j.Slf4j;
import service.UserService;
import domain.user.User;
import view_handlers.JspViewHandler;
import view_handlers.ViewHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Slf4j
public class SignInServlet extends HttpServlet {

    // Attribute and parameter names
    public static final String CURRENT_USER_SESSION_ATTR = "current_user";
    public static final String EMAIL_PARAM = "email";
    public static final String PASSWORD_PARAM = "password";
    public static final String INVALID_EMAIL_OR_PASSWORD_MESSAGE_ATTR = "invalid_email_or_password_message";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving parameters from request");
        String email = request.getParameter(EMAIL_PARAM);
        String password = request.getParameter(PASSWORD_PARAM);

        log.debug("Trying to find user by email and password");
        Optional<User> userOpt = UserService.getService()
                .findUserByEmailAndPassword(email, password);

        log.debug("Preparing attributes for response");
        Map<String, Object> respAttrs = new HashMap<>();
        String redirectURI = null;

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            request.getSession().setAttribute(CURRENT_USER_SESSION_ATTR, user);
            redirectURI = "/view/core/main-menu.jsp";
        } else {
            String invalidInputMessage = "Invalid email or password";
            request.setAttribute(INVALID_EMAIL_OR_PASSWORD_MESSAGE_ATTR, invalidInputMessage);
            request.setAttribute(EMAIL_PARAM, email);
            request.setAttribute(PASSWORD_PARAM, password);
            redirectURI = "/view/access/login.jsp";
        }

        log.debug("Invoking view handler");
        ViewHandler viewHandler = new JspViewHandler();
        viewHandler.renderView(redirectURI, respAttrs, request, response);
    }
}
