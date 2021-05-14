package filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static servlet.access.SignInServlet.CURRENT_USER_ID_SESSION_ATTR;
import static servlet.access.SignInServlet.CURRENT_USER_INFO_SESSION_ATTR;

public class GeneralFilter implements Filter {

    public void init(FilterConfig config) throws ServletException { }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {

        if (!((HttpServletRequest) req).getRequestURI().endsWith("/login-page") &&
                !((HttpServletRequest) req).getRequestURI().endsWith("/sign-in") &&
                !((HttpServletRequest) req).getRequestURI().endsWith("/registration-page") &&
                !((HttpServletRequest) req).getRequestURI().endsWith("/sign-up")) {

            // Necessary attrs in session
            Object userId = ((HttpServletRequest)req).getSession().
                    getAttribute(CURRENT_USER_ID_SESSION_ATTR);
            Object userInfo = ((HttpServletRequest)req).getSession()
                    .getAttribute(CURRENT_USER_INFO_SESSION_ATTR);

            if ((userId == null || userInfo == null)) {
                ((HttpServletResponse)resp).sendRedirect("login-page");
            } else {
                chain.doFilter(req, resp);
            }

        } else {
            chain.doFilter(req, resp);
        }

    }

    public void destroy() { }

}
