package com.company.manager.servlet.core.students_actions;

import com.company.manager.domain.course.Course;
import com.company.manager.servlet.core.get_courses.GetCourseToEditServlet;
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
import java.util.Map;

import static com.company.manager.servlet.core.get_courses.GetCourseToEditServlet.COURSE_ID_PARAM;

@Slf4j
public class GetCourseStudentsServlet extends HttpServlet {

    public static final String COURSE_ATTR = "course";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving course id from request");
        long courseId = Long.parseLong(request.getParameter(COURSE_ID_PARAM));

        log.debug("Getting course by received id");
        Course course = CourseServiceImpl.getService().getCourseById(courseId);

        log.debug("Preparing attributes for response");
        Map<String, Object> respAttrs = new HashMap<>();
        respAttrs.put(COURSE_ATTR, course);

        log.debug("Invoking view handler");
        ViewHandler viewHandler = new JspViewHandler();
        viewHandler.renderView("/view/core/course-students.jsp", respAttrs, request, response);
    }
}
