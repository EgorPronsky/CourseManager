package filter;

import handlers.view_handlers.ViewHandler;
import handlers.view_handlers.impl.JspViewHandler;
import lombok.extern.slf4j.Slf4j;
import services.impl.AccessInfoServiceImpl;

import javax.servlet.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static filter.SignInFilter.EMAIL_PARAM;
import static filter.SignInFilter.PASSWORD_PARAM;

@Slf4j
public class RegistrationFilter implements Filter {

    // Parameter and attribute names
    public static final String PASSWORD_CONFIRM_PARAM = "password_confirm";
    public static final String PASSWORD_MISMATCH_MESSAGE_ATTR = "password_mismatch_message";
    public static final String EMAIL_EXISTS_MESSAGE_ATTR = "email_exists_message";

    public void init(FilterConfig config) throws ServletException {}

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {
        log.debug("Receiving user input");
        String email = req.getParameter(EMAIL_PARAM);
        String password = req.getParameter(PASSWORD_PARAM);
        String passwordConfirm = req.getParameter(PASSWORD_CONFIRM_PARAM);

        log.debug("Checking user input on validity");
        boolean isInputValid = true;
        Map<String, Object> respAttrs = new HashMap<>();

        if (!password.equals(passwordConfirm)) {
            isInputValid = false;
            respAttrs.put(PASSWORD_MISMATCH_MESSAGE_ATTR, "Password mismatch");
        }
        if (AccessInfoServiceImpl.getService().isEmailExists(email)) {
            isInputValid = false;
            respAttrs.put(EMAIL_EXISTS_MESSAGE_ATTR, "This email already exists");
        }

        if (isInputValid) {
            log.debug("User input is valid");
            chain.doFilter(req, resp);
        } else {
            log.debug("User input invalid");
            ViewHandler viewHandler = new JspViewHandler();
            viewHandler.renderView("/registration-page", respAttrs, req, resp);
        }
    }

    public void destroy() {}

}
