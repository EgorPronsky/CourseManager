package com.company.manager.servlet.core.get_courses;

import com.company.manager.domain.course.Course;
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
import static com.company.manager.string_constans.CourseAttrAndParamNames.*;
import static com.company.manager.string_constans.UserAttrAndParamNames.SESSION_CURRENT_USER_ID;

@Slf4j
public class TeacherNotGradedCoursesServlet extends HttpServlet {

    public static final String NOT_GRADED_COURSES_STATE_ATTR_VALUE = "not graded";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving current teacher id from session");
        Long teacherId = (Long)request.getSession(false)
                .getAttribute(SESSION_CURRENT_USER_ID);

        log.debug("Getting teacher not graded courses");
        List<Course> notGradedCourses = CourseServiceImpl.getService()
                .getTeacherFinishedNotGradedCourses(teacherId);

        log.debug("Preparing attributes for response");
        Map<String, Object> respAttrs = new HashMap<>();
        respAttrs.put(COURSES_STATE, NOT_GRADED_COURSES_STATE_ATTR_VALUE);
        respAttrs.put(COURSES, notGradedCourses);
        respAttrs.put(FROM_URI, String.format("/%s/main-menu/select-courses/my-not-graded-courses", APP_NAME));

        log.debug("Invoking view handler");
        ViewHandler viewHandler = new JspViewHandler();
        viewHandler.renderView("/view/core/view-selected-courses.jsp", respAttrs, request, response);
    }
}
