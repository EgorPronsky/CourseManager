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

import static com.company.manager.constans.UserAttrAndParamNames.*;

@Slf4j
public class TrySignInServlet extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("Receiving parameters from request");
        String email = req.getParameter(EMAIL);
        String password = req.getParameter(PASSWORD);

        log.debug("Trying to find user by email and password");
        Optional<User> userOpt = UserServiceImpl.getService()
                .findUserByEmailAndPassword(email, password);

        if (userOpt.isPresent()) {
            log.debug("User was found");
            req.setAttribute(SIGN_IN_USER, userOpt.get());
            req.getRequestDispatcher("/sign-in").forward(req, resp);
        } else {
            log.debug("User wasn't found");
            String invalidInputMessage = "Invalid email or password";

            Map<String, Object> respAttrs = new HashMap<>();
            respAttrs.put(INVALID_EMAIL_OR_PASSWORD_MESSAGE, invalidInputMessage);
            respAttrs.put(EMAIL, email);
            respAttrs.put(PASSWORD, password);

            log.debug("Invoking view handler");
            ViewHandler viewHandler = new JspViewHandler();
            viewHandler.renderView("/view/access/login.jsp", respAttrs, req, resp);
        }
    }
}
