package com.company.manager.servlet.access;

import com.company.manager.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.company.manager.constans.ApplicationConstants.APP_NAME;
import static com.company.manager.constans.UserAttrAndParamNames.*;

@Slf4j
public class LogoutServlet extends HttpServlet {

    private Set<String> cookieNamesToDeleteWhenLogout;

    @Override
    public void init() throws ServletException {
        cookieNamesToDeleteWhenLogout = new HashSet<>(Arrays.asList(
                USER_ID_COOKIE_NAME, USER_SESSION_ID_COOKIE_NAME
        ));
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("Logout...");

        // Invalidating session
        req.getSession().invalidate();

        // Deleting required cookies
        CookieUtil.deleteCookies(cookieNamesToDeleteWhenLogout,
                String.format("/%s", APP_NAME) ,req, resp);

        resp.sendRedirect(String.format("/%s/login-page", APP_NAME));
    }
}
