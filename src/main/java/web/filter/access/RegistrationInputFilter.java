package web.filter.access;

import lombok.extern.slf4j.Slf4j;
import service.AccessInfoService;

import javax.servlet.*;
import java.io.IOException;

import static web.servlet.access.SignInServlet.EMAIL_PARAM;
import static web.servlet.access.SignInServlet.PASSWORD_PARAM;
import static web.servlet.access.SignUpServlet.*;

@Slf4j
public class RegistrationInputFilter implements Filter {

    public static final String PASSWORD_MISMATCH_MESSAGE_ATTR = "password_mismatch_message";
    public static final String EMAIL_EXISTS_MESSAGE_ATTR = "email_exists_message";

    public void init(FilterConfig config) throws ServletException {}

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        log.debug("Receiving user input");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String passwordConfirm = req.getParameter("password_confirm");

        log.debug("Checking user input on validity");
        boolean isInputValid = true;

        if (!password.equals(passwordConfirm)) {
            isInputValid = false;
            req.setAttribute(PASSWORD_MISMATCH_MESSAGE_ATTR, "Password mismatch");
        }
        if (AccessInfoService.getService().isEmailExists(email)) {
            isInputValid = false;
            req.setAttribute(EMAIL_EXISTS_MESSAGE_ATTR, "This email already exists");
        }

        // If all input is valid
        if (isInputValid) {
            chain.doFilter(req, resp);
        } else {
            // Setting user input
            req.setAttribute(FIRST_NAME_PARAM, req.getParameter(FIRST_NAME_PARAM));
            req.setAttribute(LAST_NAME_PARAM, req.getParameter(LAST_NAME_PARAM));
            req.setAttribute(EMAIL_PARAM, req.getParameter(EMAIL_PARAM));
            req.setAttribute(PASSWORD_PARAM, req.getParameter(PASSWORD_PARAM));
            req.setAttribute(PASSWORD_CONFIRM_PARAM, req.getParameter(PASSWORD_CONFIRM_PARAM));

            req.getRequestDispatcher("/view/access/registration.jsp").forward(req, resp);
        }
    }

    public void destroy() {}

}
