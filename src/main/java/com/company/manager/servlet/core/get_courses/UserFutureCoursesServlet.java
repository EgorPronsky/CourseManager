package com.company.manager.servlet.core.get_courses;

import com.company.manager.domain.course.Course;
import com.company.manager.domain.user.UserInfo;
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

import static com.company.manager.string_constans.ApplicationConstants.APP_NAME;
import static com.company.manager.string_constans.ApplicationConstants.FROM_URI;
import static com.company.manager.string_constans.CourseAttrAndParamNames.COURSES;
import static com.company.manager.string_constans.CourseAttrAndParamNames.COURSES_STATE;
import static com.company.manager.string_constans.UserAttrAndParamNames.CURRENT_USER_ID_SESSION;
import static com.company.manager.string_constans.UserAttrAndParamNames.CURRENT_USER_INFO_SESSION;

@Slf4j
public class UserFutureCoursesServlet extends HttpServlet {

    public static final String FUTURE_COURSES_STATE_ATTR_VALUE = "future";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving current user id and info from session");
        Long currentUserId = (Long) request.getSession(false)
                .getAttribute(CURRENT_USER_ID_SESSION);
        UserInfo currentUserInfo = (UserInfo) request.getSession(false)
                .getAttribute(CURRENT_USER_INFO_SESSION);

        List<Course> userFutureCourses = null;
        switch (currentUserInfo.getUserRole()) {
            case STUDENT: {
                log.debug("Getting student future courses");
                userFutureCourses = CourseServiceImpl.getService()
                        .getStudentFutureCourses(currentUserId);
                break;
            }
            case TEACHER: {
                log.debug("Getting teacher future courses");
                userFutureCourses = CourseServiceImpl.getService()
                        .getTeacherFutureCourses(currentUserId);
                break;
            }
            default: {
                throw new IllegalArgumentException("No action is defined for given user role");
            }
        }

        log.debug("Preparing attributes for response");
        Map<String, Object> respAttrs = new HashMap<>();
        respAttrs.put(COURSES_STATE, FUTURE_COURSES_STATE_ATTR_VALUE);
        respAttrs.put(COURSES, userFutureCourses);
        respAttrs.put(FROM_URI, String.format("/%s/main-menu/select-courses/my-future-courses", APP_NAME));

        log.debug("Invoking view handler");
        ViewHandler viewHandler = new JspViewHandler();
        viewHandler.renderView("/view/core/view-selected-courses.jsp", respAttrs, request, response);
    }
}
