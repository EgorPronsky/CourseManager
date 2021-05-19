package com.company.manager.filter;

import com.company.manager.handlers.view_handlers.ViewHandler;
import com.company.manager.handlers.view_handlers.impl.JspViewHandler;
import lombok.extern.slf4j.Slf4j;
import com.company.manager.services.impl.AccessInfoServiceImpl;

import javax.servlet.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.company.manager.constans.UserAttrAndParamNames.*;

@Slf4j
public class RegistrationFilter implements Filter {

    public void init(FilterConfig config) throws ServletException {}

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {
        log.debug("Receiving user input");
        String email = req.getParameter(EMAIL);
        String password = req.getParameter(PASSWORD);
        String passwordConfirm = req.getParameter(PASSWORD_CONFIRM);

        log.debug("Checking user input on validity");
        boolean isInputValid = true;
        Map<String, Object> respAttrs = new HashMap<>();

        if (!password.equals(passwordConfirm)) {
            isInputValid = false;
            respAttrs.put(PASSWORD_MISMATCH_MESSAGE, "Password mismatch");
        }
        if (AccessInfoServiceImpl.getService().isEmailExists(email)) {
            isInputValid = false;
            respAttrs.put(EMAIL_EXISTS_MESSAGE, "This email already exists");
        }

        if (isInputValid) {
            log.debug("User input is valid");
            chain.doFilter(req, resp);
        } else {
            log.debug("User input invalid");
            ViewHandler viewHandler = new JspViewHandler();
            viewHandler.renderView("/view/access/registration.jsp", respAttrs, req, resp);
        }
    }

    public void destroy() {}

}
