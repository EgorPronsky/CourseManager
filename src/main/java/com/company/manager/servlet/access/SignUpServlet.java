package com.company.manager.servlet.access;

import com.company.manager.domain.user.Student;
import com.company.manager.domain.user.Teacher;
import com.company.manager.domain.user.info.AccessInfo;
import com.company.manager.domain.user.info.UserInfo;
import com.company.manager.domain.user.info.UserRole;
import com.company.manager.services.impl.StudentServiceImpl;
import com.company.manager.services.impl.TeacherServiceImpl;
import lombok.extern.slf4j.Slf4j;
import com.company.manager.domain.user.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.company.manager.filter.SessionFilter.APP_DOMAIN_NAME;
import static com.company.manager.servlet.access.TrySignInServlet.EMAIL_PARAM;
import static com.company.manager.servlet.access.TrySignInServlet.PASSWORD_PARAM;

@Slf4j
public class SignUpServlet extends HttpServlet {

    // Parameter names
    public static final String FIRST_NAME_PARAM = "first_name";
    public static final String LAST_NAME_PARAM = "last_name";
    public static final String IS_TEACHER_PARAM = "is_teacher";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving parameters from request");
        String firstName = request.getParameter(FIRST_NAME_PARAM);
        String lastName  = request.getParameter(LAST_NAME_PARAM);
        String isTeacher = request.getParameter(IS_TEACHER_PARAM);
        String email     = request.getParameter(EMAIL_PARAM);
        String password  = request.getParameter(PASSWORD_PARAM);

        if (isTeacher == null) {
            log.debug("Creating new student from received params");
            Student st = Student.builder()
                    .user(User.builder()
                            .userInfo(UserInfo.builder()
                                    .firstName(firstName)
                                    .lastName(lastName)
                                    .userRole(UserRole.STUDENT)
                                    .build())
                            .accessInfo(AccessInfo.builder()
                                    .email(email)
                                    .passwordHash(password.hashCode())
                                    .build()).build()).build();

            log.debug("Saving new student in DB");
            StudentServiceImpl.getService().saveStudent(st);
        } else {
            log.debug("Creating new teacher from received params");
            Teacher tch = Teacher.builder()
                    .user(User.builder()
                            .userInfo(UserInfo.builder()
                                    .firstName(firstName)
                                    .lastName(lastName)
                                    .userRole(UserRole.TEACHER)
                                    .build())
                            .accessInfo(AccessInfo.builder()
                                    .email(email)
                                    .passwordHash(password.hashCode())
                                    .build()).build()).build();

            log.debug("Saving new teacher in DB");
            TeacherServiceImpl.getService().saveTeacher(tch);
        }

        response.sendRedirect(String.format("/%s/login-page", APP_DOMAIN_NAME));
    }

}
