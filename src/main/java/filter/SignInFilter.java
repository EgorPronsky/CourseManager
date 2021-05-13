package filter;

import domain.user.User;
import handlers.view_handlers.ViewHandler;
import handlers.view_handlers.impl.JspViewHandler;
import lombok.extern.slf4j.Slf4j;
import services.impl.UserServiceImpl;

import javax.servlet.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class SignInFilter implements Filter {

    public static final String SIGN_IN_USER_ATTR = "sign_in_user";
    public static final String EMAIL_PARAM = "email";
    public static final String PASSWORD_PARAM = "password";
    public static final String INVALID_EMAIL_OR_PASSWORD_MESSAGE_ATTR = "invalid_email_or_password_message";

    public void init(FilterConfig config) throws ServletException { }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {
        log.debug("Receiving parameters from request");
        String email = req.getParameter(EMAIL_PARAM);
        String password = req.getParameter(PASSWORD_PARAM);

        log.debug("Trying to find user by email and password");
        Optional<User> userOpt = UserServiceImpl.getService()
                .findUserByEmailAndPassword(email, password);

        if (userOpt.isPresent()) {
            log.debug("User was found");
            req.setAttribute(SIGN_IN_USER_ATTR, userOpt.get());
            chain.doFilter(req, resp);
        } else {
            log.debug("User wasn't found");
            String invalidInputMessage = "Invalid email or password";

            Map<String, Object> respAttrs = new HashMap<>();
            respAttrs.put(INVALID_EMAIL_OR_PASSWORD_MESSAGE_ATTR, invalidInputMessage);
            respAttrs.put(EMAIL_PARAM, email);
            respAttrs.put(PASSWORD_PARAM, password);

            log.debug("Invoking view handler");
            ViewHandler viewHandler = new JspViewHandler();
            viewHandler.renderView("/view/access/login.jsp", respAttrs, req, resp);
        }
    }

    public void destroy() { }
}
