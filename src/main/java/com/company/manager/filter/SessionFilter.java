package com.company.manager.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static com.company.manager.string_constans.ApplicationConstants.APP_NAME;

@Slf4j
public class SessionFilter implements Filter {

    private String[] allowedPathsWithNoActiveSession;

    public void init(FilterConfig config) throws ServletException {
        allowedPathsWithNoActiveSession = new String[] {
                "/login-page", "/try-sign-in", "/sign-in", "/registration-page", "/sign-up"
        };
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {

        HttpServletRequest httpReq = (HttpServletRequest)req;
        HttpServletResponse httpResp = (HttpServletResponse)resp;

        if (httpReq.getSession(false) == null) {
            log.debug("No active session is attached");
            String uri = httpReq.getRequestURI();
            Optional<String> allowedPath = Arrays.stream(allowedPathsWithNoActiveSession)
                    .filter(uri::endsWith).findAny();

            if (!allowedPath.isPresent()) {
                httpResp.sendRedirect(String.format("/%s/login-page", APP_NAME));
            } else {
                chain.doFilter(req, resp);
            }
        } else {
            chain.doFilter(req, resp);
        }

    }

    public void destroy() { }

}
