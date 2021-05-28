package com.company.manager.servlet.core.course_actions;

import com.company.manager.domain.archive.StudentCourseResult;
import com.company.manager.domain.course.Course;
import com.company.manager.domain.user.User;
import com.company.manager.handlers.view_handlers.ViewHandler;
import com.company.manager.handlers.view_handlers.impl.JspViewHandler;
import com.company.manager.services.impl.StudentCourseResultServiceImpl;
import com.company.manager.services.impl.UserServiceImpl;
import com.company.manager.util.StudentCourseResultConverter;
import lombok.extern.slf4j.Slf4j;
import com.company.manager.services.impl.CourseServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.company.manager.string_constans.ApplicationConstants.FROM_URI;
import static com.company.manager.string_constans.CourseAttrAndParamNames.*;
import static com.company.manager.string_constans.UserAttrAndParamNames.SESSION_CURRENT_USER_ID;
import static com.company.manager.servlet.core.students_actions.GetCourseStudentsServlet.GET_STUDENTS_TO_KICK;

@Slf4j
public class LeaveCourseServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving course id to leave");
        long courseIdToLeave = Long.parseLong(request.getParameter(COURSE_ID));

        log.debug("Getting course by received id from DB");
        Optional<Course> courseToLeaveOpt = CourseServiceImpl.getService()
                .findCourseById(courseIdToLeave);

        Map<String, Object> respAttrs = null;

        // Course could be already deleted
        if (courseToLeaveOpt.isPresent()) {
            long studentId;

            // If teacher kicking student -> will receive student id
            String studentIdStr = request.getParameter(COURSE_STUDENT_ID);
            if (studentIdStr != null) {
                studentId = Long.parseLong(studentIdStr);

                // Attr to stay on the same page (if teacher kicking students)
                respAttrs = new HashMap<>();
                respAttrs.put(COURSE_STUDENTS_VIEW_TARGET, GET_STUDENTS_TO_KICK);
            } else {
                // If student leaving course on his own -> getting his id from session
                studentId = (Long)request.getSession(false)
                        .getAttribute(SESSION_CURRENT_USER_ID);
            }

            log.debug("Getting current student by id from DB");
            User currentStudent = UserServiceImpl.getService()
                    .getUserById(studentId);

            log.debug("Mapping course to SCR entity");
            StudentCourseResult scrToLeave = StudentCourseResultConverter
                    .convertToStudentCourseResult(courseToLeaveOpt.get(), currentStudent, null);

            log.debug("Deleting user from course");
            StudentCourseResultServiceImpl.getService()
                    .deleteStudentCourseResult(scrToLeave);
        }

        if (respAttrs != null) {
            ViewHandler viewHandler = new JspViewHandler();
            viewHandler.renderView("/main-menu/select-courses/students",
                    respAttrs, request, response);
        } else {
            response.sendRedirect(request.getParameter(FROM_URI));
        }

    }
}
