package com.company.manager.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;


public class CookieUtil {

    public static void addCookie(String name, String value, String path,
                                 int lifeTime, boolean httpOnly , HttpServletResponse resp) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(path);
        cookie.setMaxAge(lifeTime);
        cookie.setHttpOnly(httpOnly);
        resp.addCookie(cookie);
    }

    public static Optional<Cookie> findCookie(String cookieName, HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) return Optional.empty();
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny();
    }

    public static void deleteCookies(Set<String> cookieNames, String path, HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null)
            Arrays.stream(cookies).forEach(cookie -> {
                        if (cookieNames.contains(cookie.getName())) {
                            removeCookie(cookie.getName(), path, resp);
                        }
                    });
    }

    public static void removeCookie(String cookieName, String path, HttpServletResponse resp) {
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setMaxAge(0);
        cookie.setPath(path);
        resp.addCookie(cookie);
    }
}
