package com.company.manager.servlet.access;

import com.company.manager.services.impl.UserServiceImpl;
import com.company.manager.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import com.company.manager.domain.user.User;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

import static com.company.manager.string_constans.ApplicationConstants.APP_NAME;
import static com.company.manager.string_constans.UserAttrAndParamNames.*;

@Slf4j
public class SignInServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("Receiving user id");
        long userId = (Long)req.getAttribute(USER_ID_COOKIE_NAME);

        log.debug("Getting user by received id");
        User user = UserServiceImpl.getService().getUserById(userId);

        req.setAttribute(SIGN_IN_USER, user);
        doPost(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("Receiving user");
        User user = (User) req.getAttribute(SIGN_IN_USER);

        log.debug("Setting attributes into session");
        HttpSession session = req.getSession();
        session.setAttribute(SESSION_CURRENT_USER_ID, user.getId());
        session.setAttribute(SESSION_CURRENT_USER_INFO, user.getUserInfo());

        // Handling "Remember me" checkbox
        if (req.getParameter(REMEMBER_USER) != null) {
            log.debug("Adding user id cookie");
            CookieUtil.addCookie(USER_ID_COOKIE_NAME, String.valueOf(user.getId()),
                    String.format("/%s/login-page", APP_NAME), Integer.MAX_VALUE, true, resp);
        }

        resp.sendRedirect(String.format("/%s/main-menu", APP_NAME));
    }
}
