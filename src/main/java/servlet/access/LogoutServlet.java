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

import static servlet.access.SignInServlet.*;

@Slf4j
public class LogoutServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Log out...");

        // Removing session attrs
        request.getSession().removeAttribute(CURRENT_USER_ID_SESSION_ATTR);
        request.getSession().removeAttribute(CURRENT_USER_INFO_SESSION_ATTR);

        // Deleting user id cookie
        Cookie cookie = new Cookie(USER_ID_COOKIE_NAME, "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        response.sendRedirect("login-page");
    }
}
