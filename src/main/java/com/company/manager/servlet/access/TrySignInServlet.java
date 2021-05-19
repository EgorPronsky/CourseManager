package com.company.manager.servlet.access;

import com.company.manager.domain.user.User;
import com.company.manager.handlers.view_handlers.ViewHandler;
import com.company.manager.handlers.view_handlers.impl.JspViewHandler;
import com.company.manager.services.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class TrySignInServlet extends HttpServlet {

    // Parameter and attribute names
    public static final String EMAIL_PARAM = "email";
    public static final String PASSWORD_PARAM = "password";
    public static final String SIGN_IN_USER_ATTR = "sign_in_user";

    public static final String INVALID_EMAIL_OR_PASSWORD_MESSAGE_ATTR = "invalid_email_or_password_message";

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("Receiving parameters from request");
        String email = req.getParameter(EMAIL_PARAM);
        String password = req.getParameter(PASSWORD_PARAM);

        log.debug("Trying to find user by email and password");
        Optional<User> userOpt = UserServiceImpl.getService()
                .findUserByEmailAndPassword(email, password);

        if (userOpt.isPresent()) {
            log.debug("User was found");
            req.setAttribute(SIGN_IN_USER_ATTR, userOpt.get());
            req.getRequestDispatcher("/sign-in").forward(req, resp);
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
}
