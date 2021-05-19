package com.company.manager.servlet.access;

import com.company.manager.services.impl.UserServiceImpl;
import com.company.manager.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import com.company.manager.domain.user.User;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Optional;

import static com.company.manager.filter.LoginPageFilter.USER_ID_FROM_COOKIE_ATTR;
import static com.company.manager.filter.SessionFilter.APP_DOMAIN_NAME;
import static com.company.manager.servlet.access.TrySignInServlet.SIGN_IN_USER_ATTR;

@Slf4j
public class SignInServlet extends HttpServlet {

    // Attribute and param names
    public static final String CURRENT_USER_ID_SESSION_ATTR = "current_user_id";
    public static final String CURRENT_USER_INFO_SESSION_ATTR = "current_user_info";
    public static final String REMEMBER_USER_PARAM = "remember_user";

    public static final String USER_ID_COOKIE_NAME = "user_id_cookie";

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("Receiving user id from request");
        long userId = (Long)req.getAttribute(USER_ID_FROM_COOKIE_ATTR);

        log.debug("Getting user by received id");
        User user = UserServiceImpl.getService().getUserById(userId);

        req.setAttribute(SIGN_IN_USER_ATTR, user);
        doPost(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("Receiving user from request");
        User user = (User) req.getAttribute(SIGN_IN_USER_ATTR);

        log.debug("Setting attributes into session");
        HttpSession session = req.getSession();
        session.setAttribute(CURRENT_USER_ID_SESSION_ATTR, user.getId());
        session.setAttribute(CURRENT_USER_INFO_SESSION_ATTR, user.getUserInfo());

        // Handling "Remember me" checkbox
        if (req.getParameter(REMEMBER_USER_PARAM) != null) {
            log.debug("Adding user id cookie");
            CookieUtil.addCookie(USER_ID_COOKIE_NAME, String.valueOf(user.getId()),
                    Integer.MAX_VALUE, resp);
        }

        resp.sendRedirect(String.format("/%s/main-menu", APP_DOMAIN_NAME));
    }
}
