package servlet.access;

import lombok.extern.slf4j.Slf4j;
import domain.user.User;
import handlers.view_handlers.impl.JspViewHandler;
import handlers.view_handlers.ViewHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static filter.SignInFilter.SIGN_IN_USER_ATTR;

@Slf4j
public class SignInServlet extends HttpServlet {

    // Attribute and parameter names
    public static final String CURRENT_USER_ID_SESSION_ATTR = "current_user_id";
    public static final String CURRENT_USER_INFO_SESSION_ATTR = "current_user_info";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving user from filter");
        User user = (User) request.getAttribute(SIGN_IN_USER_ATTR);

        log.debug("Setting attributes into session");
        HttpSession session = request.getSession();
        session.setAttribute(CURRENT_USER_ID_SESSION_ATTR, user.getId());
        session.setAttribute(CURRENT_USER_INFO_SESSION_ATTR, user.getUserInfo());

        log.debug("Invoking view handler");
        ViewHandler viewHandler = new JspViewHandler();
        viewHandler.renderView("/view/access/login.jsp", null, request, response);
    }
}
