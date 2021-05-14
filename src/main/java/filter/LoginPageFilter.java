package filter;

import domain.user.User;
import services.UserService;
import services.impl.UserServiceImpl;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static filter.SignInFilter.SIGN_IN_USER_ATTR;
import static servlet.access.SignInServlet.USER_ID_COOKIE_NAME;

public class LoginPageFilter implements Filter {

    public void init(FilterConfig config) throws ServletException { }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {
         Optional<Cookie> userIdCookie = Arrays.stream(((HttpServletRequest)req).getCookies())
                 .filter(cookie -> cookie.getName().equals(USER_ID_COOKIE_NAME))
                .findAny();

         if (userIdCookie.isPresent()) {
             User user = UserServiceImpl.getService()
                     .findUserById_OrThrowEx(Long.parseLong(userIdCookie.get().getValue()));
             req.setAttribute(SIGN_IN_USER_ATTR, user);
             req.getRequestDispatcher("/sign-in").forward(req, resp);
         } else {
             chain.doFilter(req, resp);
         }

    }

    public void destroy() { }
}
