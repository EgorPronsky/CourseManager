package com.company.manager.servlet.core.get_courses;

import com.company.manager.domain.course.Course;
import com.company.manager.domain.user.info.UserInfo;
import com.company.manager.domain.user.info.UserRole;
import com.company.manager.servlet.access.SignInServlet;
import lombok.extern.slf4j.Slf4j;
import com.company.manager.services.impl.CourseServiceImpl;
import com.company.manager.handlers.view_handlers.impl.JspViewHandler;
import com.company.manager.handlers.view_handlers.ViewHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class UserFutureCoursesServlet extends HttpServlet {

    public static final String FUTURE_COURSES_STATE_ATTR_VALUE = "future";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving current user id and info from session");
        Long currentUserId = (Long) request.getSession(false)
                .getAttribute(SignInServlet.CURRENT_USER_ID_SESSION_ATTR);
        UserInfo currentUserInfo = (UserInfo) request.getSession(false)
                .getAttribute(SignInServlet.CURRENT_USER_INFO_SESSION_ATTR);

        List<Course> userFutureCourses;
        if (currentUserInfo.getUserRole() == UserRole.STUDENT) {
            log.debug("Getting student future courses");
            userFutureCourses = CourseServiceImpl.getService()
                    .getStudentFutureCourses(currentUserId);
        } else {
            log.debug("Getting teacher future courses");
            userFutureCourses = CourseServiceImpl.getService()
                    .getTeacherFutureCourses(currentUserId);
        }

        log.debug("Preparing attributes for response");
        Map<String, Object> respAttrs = new HashMap<>();
        respAttrs.put(UserCurrentCoursesServlet.COURSES_STATE_ATTR, FUTURE_COURSES_STATE_ATTR_VALUE);
        respAttrs.put(UserCurrentCoursesServlet.COURSES_ATTR, userFutureCourses);

        log.debug("Invoking view handler");
        ViewHandler viewHandler = new JspViewHandler();
        viewHandler.renderView("/view/core/view-selected-courses.jsp", respAttrs, request, response);
    }
}
