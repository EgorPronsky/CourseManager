package servlet.access;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static filter.SessionFilter.APP_DOMAIN_NAME;
import static servlet.access.SignInServlet.*;

@Slf4j
public class LogoutServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Log out...");

        // Invalidating session
        request.getSession().invalidate();

        // Deleting user id cookie
        Cookie cookie = new Cookie(USER_ID_COOKIE_NAME, "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        response.sendRedirect(String.format("/%s/login-page", APP_DOMAIN_NAME));
    }
}
