package com.company.manager.servlet.access;

import com.company.manager.domain.user.AccessInfo;
import com.company.manager.domain.user.UserInfo;
import com.company.manager.domain.user.UserRole;
import com.company.manager.services.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import com.company.manager.domain.user.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.company.manager.constans.ApplicationConstants.APP_DOMAIN_NAME;
import static com.company.manager.constans.UserAttrAndParamNames.*;

@Slf4j
public class SignUpServlet extends HttpServlet {



    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving parameters from request");
        String firstName = request.getParameter(FIRST_NAME);
        String lastName  = request.getParameter(LAST_NAME);
        String isTeacher = request.getParameter(IS_TEACHER);
        String email     = request.getParameter(EMAIL);
        String password  = request.getParameter(PASSWORD);

        // Define user role
        UserRole userRole = isTeacher == null ? UserRole.STUDENT : UserRole.TEACHER;

        log.debug("Creating new user from received params");
        User newUser = User.builder()
                .accessInfo(AccessInfo.builder()
                        .email(email)
                        .passwordHash(password.hashCode())
                        .build())
                .userInfo(UserInfo.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .userRole(userRole)
                        .build())
                .build();

        log.debug("Saving new user in DB");
        UserServiceImpl.getService().saveUser(newUser);

        response.sendRedirect(String.format("/%s/login-page", APP_DOMAIN_NAME));
    }

}
