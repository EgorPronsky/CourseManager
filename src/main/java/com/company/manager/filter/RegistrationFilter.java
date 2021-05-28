package com.company.manager.filter;

import com.company.manager.handlers.view_handlers.ViewHandler;
import com.company.manager.handlers.view_handlers.impl.JspViewHandler;
import lombok.extern.slf4j.Slf4j;
import com.company.manager.services.impl.AccessInfoServiceImpl;

import javax.servlet.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.company.manager.string_constans.UserAttrAndParamNames.*;

@Slf4j
public class RegistrationFilter implements Filter {

    public static final String EMAIL_EXISTS_MESSAGE_ATTR_NAME = "email_exists_message";

    public void init(FilterConfig config) throws ServletException {}

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {
        log.debug("Receiving user input");
        String email = req.getParameter(EMAIL);

        log.debug("Checking user entered email on existence");
        if (!AccessInfoServiceImpl.getService().isEmailExists(email)) {
            log.debug("User input is valid");
            chain.doFilter(req, resp);
        } else {
            log.debug("Entered email already exists");
            Map<String, Object> respAttrs = new HashMap<>();
            respAttrs.put(EMAIL_EXISTS_MESSAGE_ATTR_NAME, "This email already exists");
            ViewHandler viewHandler = new JspViewHandler();
            viewHandler.renderView("/view/access/registration.jsp", respAttrs, req, resp);
        }
    }

    public void destroy() {}

}
