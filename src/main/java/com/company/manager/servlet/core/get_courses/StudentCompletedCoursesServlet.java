package com.company.manager.servlet.core.get_courses;

import com.company.manager.domain.archive.StudentCourseResult;
import lombok.extern.slf4j.Slf4j;
import com.company.manager.services.impl.StudentCourseResultServiceImpl;
import com.company.manager.handlers.view_handlers.impl.JspViewHandler;
import com.company.manager.handlers.view_handlers.ViewHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.company.manager.string_constans.CourseAttrAndParamNames.COURSES_RESULTS;
import static com.company.manager.string_constans.UserAttrAndParamNames.SESSION_CURRENT_USER_ID;

@Slf4j
public class StudentCompletedCoursesServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Getting current student id");
        Long studentId = (Long)request.getSession(false)
                .getAttribute(SESSION_CURRENT_USER_ID);

        log.debug("Getting student completed courses");
        List<StudentCourseResult> completedCourses =
                StudentCourseResultServiceImpl.getService()
                        .getCompletedCoursesWithResultsByStudentId(studentId);

        log.debug("Preparing attributes for response");
        Map<String, Object> respAttrs = new HashMap<>();
        respAttrs.put(COURSES_RESULTS, completedCourses);

        log.debug("Invoking view handler");
        ViewHandler viewHandler = new JspViewHandler();
        viewHandler.renderView("/view/core/view-completed-courses.jsp", respAttrs, request, response);
    }
}
