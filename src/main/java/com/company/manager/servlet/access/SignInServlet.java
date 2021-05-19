package com.company.manager.servlet.access;

import com.company.manager.services.impl.UserServiceImpl;
import com.company.manager.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import com.company.manager.domain.user.User;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Optional;

import static com.company.manager.constans.ApplicationConstants.APP_DOMAIN_NAME;
import static com.company.manager.constans.UserAttrAndParamNames.*;

@Slf4j
public class SignInServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("Receiving user id from request");
        long userId = (Long)req.getAttribute(USER_ID_COOKIE_NAME);

        log.debug("Getting user by received id");
        User user = UserServiceImpl.getService().getUserById(userId);

        req.setAttribute(SIGN_IN_USER, user);
        doPost(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("Receiving user from request");
        User user = (User) req.getAttribute(SIGN_IN_USER);

        log.debug("Setting attributes into session");
        HttpSession session = req.getSession();
        session.setAttribute(CURRENT_USER_ID_SESSION, user.getId());
        session.setAttribute(CURRENT_USER_INFO_SESSION, user.getUserInfo());

        // Handling "Remember me" checkbox
        if (req.getParameter(REMEMBER_USER) != null) {
            log.debug("Adding user id cookie");
            CookieUtil.addCookie(USER_ID_COOKIE_NAME, String.valueOf(user.getId()),
                    Integer.MAX_VALUE, resp);
        }

        resp.sendRedirect(String.format("/%s/main-menu", APP_DOMAIN_NAME));
    }
}
