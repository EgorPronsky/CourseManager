package com.company.manager.filter;

import com.company.manager.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.company.manager.constans.ApplicationConstants.APP_NAME;
import static com.company.manager.constans.UserAttrAndParamNames.USER_ID_COOKIE_NAME;

@Slf4j
public class LoginPageFilter implements Filter {

    public void init(FilterConfig config) throws ServletException { }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {

        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpServletResponse httpResp = (HttpServletResponse) resp;

        if (httpReq.getSession(false) != null) {
            log.debug("Session is active, access to login page denied");
            httpResp.sendRedirect(String.format("/%s/main-menu", APP_NAME));
        } else {
            Optional<Cookie> userIdCookie = CookieUtil
                    .findCookie(USER_ID_COOKIE_NAME, httpReq);

            if (userIdCookie.isPresent()) {
                req.setAttribute(USER_ID_COOKIE_NAME, Long.parseLong(userIdCookie.get().getValue()));
                req.getRequestDispatcher("/sign-in").forward(req, resp);
            } else {
                chain.doFilter(req, resp);
            }
        }

    }

    public void destroy() { }
}
