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

import static com.company.manager.servlet.access.SignInServlet.CURRENT_USER_ID_SESSION_ATTR;
import static com.company.manager.servlet.core.get_courses.UserCurrentCoursesServlet.*;

@Slf4j
public class TeacherNotGradedCoursesServlet extends HttpServlet {

    public static final String NOT_GRADED_COURSES_STATE_ATTR_VALUE = "not graded";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving current teacher id from session");
        Long teacherId = (Long)request.getSession(false)
                .getAttribute(CURRENT_USER_ID_SESSION_ATTR);

        log.debug("Getting teacher not graded courses");
        List<Course> notGradedCourses = CourseServiceImpl.getService()
                .getTeacherFinishedNotGradedCourses(teacherId);

        log.debug("Preparing attributes for response");
        Map<String, Object> respAttrs = new HashMap<>();
        respAttrs.put(COURSES_STATE_ATTR, NOT_GRADED_COURSES_STATE_ATTR_VALUE);
        respAttrs.put(COURSES_ATTR, notGradedCourses);

        log.debug("Invoking view handler");
        ViewHandler viewHandler = new JspViewHandler();
        viewHandler.renderView("/view/core/view-selected-courses.jsp", respAttrs, request, response);
    }
}
