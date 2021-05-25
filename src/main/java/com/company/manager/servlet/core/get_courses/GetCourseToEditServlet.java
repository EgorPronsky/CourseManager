package com.company.manager.servlet.core.get_courses;

import com.company.manager.domain.course.Course;
import com.company.manager.handlers.view_handlers.ViewHandler;
import com.company.manager.handlers.view_handlers.impl.JspViewHandler;
import lombok.extern.slf4j.Slf4j;
import com.company.manager.services.impl.CourseServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.company.manager.string_constans.CourseAttrAndParamNames.COURSE;
import static com.company.manager.string_constans.CourseAttrAndParamNames.COURSE_ID;

@Slf4j
public class GetCourseToEditServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving course id to edit");
        long courseId = Long.parseLong(request.getParameter(COURSE_ID));

        log.debug("Finding course by received id");
        Course courseToEdit = CourseServiceImpl.getService()
                .getCourseById(courseId);

        log.debug("Preparing attributes for response");
        Map<String, Object> respAttrs = new HashMap<>();
        respAttrs.put(COURSE, courseToEdit);

        log.debug("Invoking view handler");
        ViewHandler viewHandler = new JspViewHandler();
        viewHandler.renderView("/view/core/create-or-update-course.jsp", respAttrs, request, response);
    }
}
