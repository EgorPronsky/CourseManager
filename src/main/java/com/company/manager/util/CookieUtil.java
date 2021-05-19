package com.company.manager.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static com.company.manager.constans.ApplicationConstants.APP_DOMAIN_NAME;


public class CookieUtil {

    public static void addCookie(String name, String value, int lifeTime, HttpServletResponse resp) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(lifeTime);
        resp.addCookie(cookie);
    }

    public static Optional<Cookie> findCookie(String cookieName, HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) return Optional.empty();
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny();
    }

    public static void deleteCookies(Set<String> cookieNames, HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            Arrays.stream(cookies)
                    .forEach(cookie -> {
                        if (cookieNames.contains(cookie.getName())) {
                            removeCookie(cookie.getName(), resp);
                        }
                    });
        }
    }

    public static void removeCookie(String cookieName, HttpServletResponse resp) {
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setMaxAge(0);
        cookie.setPath(String.format("/%s", APP_DOMAIN_NAME));
        resp.addCookie(cookie);
    }
}
