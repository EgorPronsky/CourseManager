package servlet.access;

import lombok.extern.slf4j.Slf4j;
import domain.user.User;
import handlers.view_handlers.impl.JspViewHandler;
import handlers.view_handlers.ViewHandler;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

import static filter.SignInFilter.SIGN_IN_USER_ATTR;

@Slf4j
public class SignInServlet extends HttpServlet {

    // Attribute and param names
    public static final String CURRENT_USER_ID_SESSION_ATTR = "current_user_id";
    public static final String CURRENT_USER_INFO_SESSION_ATTR = "current_user_info";
    public static final String REMEMBER_USER_PARAM = "remember_user";

    public static final String USER_ID_COOKIE_NAME = "user_id_cookie";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving user from filter");
        User user = (User) request.getAttribute(SIGN_IN_USER_ATTR);

        log.debug("Setting attributes into session");
        HttpSession session = request.getSession();
        session.setAttribute(CURRENT_USER_ID_SESSION_ATTR, user.getId());
        session.setAttribute(CURRENT_USER_INFO_SESSION_ATTR, user.getUserInfo());

        String rememberUser = request.getParameter(REMEMBER_USER_PARAM);
        if (rememberUser != null) {
            log.debug("Adding user id cookie");
            Cookie cookie = new Cookie(USER_ID_COOKIE_NAME, String.valueOf(user.getId()));
            response.addCookie(cookie);
        }

        response.sendRedirect("main-menu");
    }
}
