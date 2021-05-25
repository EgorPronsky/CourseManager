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

import static com.company.manager.string_constans.CourseAttrAndParamNames.COURSES;
import static com.company.manager.string_constans.UserAttrAndParamNames.CURRENT_USER_ID_SESSION;

@Slf4j
public class GetAvailableCoursesServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving current student id from session");
        Long studentId = (Long)request.getSession(false)
                .getAttribute(CURRENT_USER_ID_SESSION);

        log.debug("Getting available courses for the student");
        List<Course> availableCourses = CourseServiceImpl.getService()
                .getStudentAvailableToJoinCourses(studentId);

        log.debug("Preparing attributes for response");
        Map<String, Object> respAttrs = new HashMap<>();
        respAttrs.put(COURSES, availableCourses);

        log.debug("Invoking view handler");
        ViewHandler viewHandler = new JspViewHandler();
        viewHandler.renderView("/view/core/join-new-courses.jsp", respAttrs, request, response);
    }
}
