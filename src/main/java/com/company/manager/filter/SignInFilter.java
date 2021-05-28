package com.company.manager.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.company.manager.string_constans.ApplicationConstants.APP_NAME;

@Slf4j
public class SignInFilter implements Filter {

    public void init(FilterConfig config) throws ServletException { }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {

        HttpServletRequest httpReq = (HttpServletRequest)req;
        HttpServletResponse httpResp = (HttpServletResponse)resp;

        if (httpReq.getSession(false) != null) {
            log.debug("Log out before sign in");
            httpResp.sendRedirect(String.format("/%s/sign-in-declined", APP_NAME));
        } else {
            chain.doFilter(req, resp);
        }
    }

    public void destroy() { }

}
