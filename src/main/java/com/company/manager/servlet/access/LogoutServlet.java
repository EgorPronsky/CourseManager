package com.company.manager.servlet.access;

import com.company.manager.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.company.manager.string_constans.ApplicationConstants.APP_NAME;
import static com.company.manager.string_constans.UserAttrAndParamNames.*;

@Slf4j
public class LogoutServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("Logout...");

        // Invalidating session
        req.getSession().invalidate();

        // Deleting user id cookie
        CookieUtil.removeCookie(USER_ID_COOKIE_NAME,
                String.format("/%s/login-page", APP_NAME), resp);

        resp.sendRedirect(String.format("/%s/login-page", APP_NAME));
    }
}
