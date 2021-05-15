package filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.stream;
import static servlet.access.SignInServlet.CURRENT_USER_ID_SESSION_ATTR;
import static servlet.access.SignInServlet.CURRENT_USER_INFO_SESSION_ATTR;

public class SessionFilter implements Filter {

    public static final String APP_DOMAIN_NAME = "web_4_war_exploded";
    private String[] allowedPathsWithNoSession;

    public void init(FilterConfig config) throws ServletException {
        allowedPathsWithNoSession = new String[] {
                "/login-page", "/sign-in", "/registration-page", "/sign-up"
        };
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {

        HttpServletRequest httpReq = (HttpServletRequest)req;
        if (httpReq.getSession(false) == null) {

            String uri = httpReq.getRequestURI();
            Optional<String> allowedPath = Arrays.stream(allowedPathsWithNoSession)
                    .filter(uri::contains).findAny();

            if (!allowedPath.isPresent()) {
                ((HttpServletResponse)resp).sendRedirect(String.format("/%s/login-page", APP_DOMAIN_NAME));
            } else {
                chain.doFilter(req, resp);
            }
        } else {
            chain.doFilter(req, resp);
        }

    }

    public void destroy() { }

}
