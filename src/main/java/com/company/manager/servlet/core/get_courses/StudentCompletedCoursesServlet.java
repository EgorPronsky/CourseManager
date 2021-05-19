package com.company.manager.servlet.core.get_courses;

import com.company.manager.domain.archive.CourseResult;
import com.company.manager.domain.archive.StudentCourseResult;
import com.company.manager.domain.user.Student;
import com.company.manager.servlet.access.SignInServlet;
import lombok.extern.slf4j.Slf4j;
import com.company.manager.services.impl.CourseServiceImpl;
import com.company.manager.services.impl.StudentCourseResultServiceImpl;
import com.company.manager.services.impl.StudentServiceImpl;
import com.company.manager.handlers.view_handlers.impl.JspViewHandler;
import com.company.manager.handlers.view_handlers.ViewHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class StudentCompletedCoursesServlet extends HttpServlet {

    public static final String COURSES_RESULTS_ATTR = "courses_results";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Getting current student from DB");
        Long studentId = (Long)request.getSession(false)
                .getAttribute(SignInServlet.CURRENT_USER_ID_SESSION_ATTR);
        Student currentStudent = StudentServiceImpl.getService()
                .getStudentById(studentId);

        log.debug("Getting student finished graded courses");
        List<StudentCourseResult> gradedCourses =
                StudentCourseResultServiceImpl.getService()
                        .getCoursesResultsByStudentId(studentId);

        log.debug("Getting student finished ungraded courses");
        List<StudentCourseResult> ungradedCourses = CourseServiceImpl.getService()
                .getStudentFinishedUngradedCourses(studentId).stream()
                .map(course -> StudentCourseResult.builder()
                        .student(currentStudent).course(course).result(CourseResult.NOT_GRADED)
                        .build())
                .collect(Collectors.toList());

        gradedCourses.addAll(ungradedCourses);
        List<StudentCourseResult> allStudentFinishedCourses = gradedCourses;

        log.debug("Preparing attributes for response");
        Map<String, Object> respAttrs = new HashMap<>();
        respAttrs.put(COURSES_RESULTS_ATTR, allStudentFinishedCourses);

        log.debug("Invoking view handler");
        ViewHandler viewHandler = new JspViewHandler();
        viewHandler.renderView("/view/core/view-completed-courses.jsp", respAttrs, request, response);
    }
}
