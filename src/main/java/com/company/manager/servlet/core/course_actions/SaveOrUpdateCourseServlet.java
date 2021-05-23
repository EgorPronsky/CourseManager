package com.company.manager.servlet.core.course_actions;

import com.company.manager.domain.course.Course;
import com.company.manager.domain.course.CourseInfo;
import com.company.manager.domain.user.User;
import com.company.manager.services.impl.UserServiceImpl;
import com.company.manager.util.builders.CourseBuilder;
import lombok.extern.slf4j.Slf4j;
import com.company.manager.services.impl.CourseServiceImpl;
import com.company.manager.handlers.input_handlers.CourseInputHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.company.manager.constans.ApplicationConstants.APP_NAME;
import static com.company.manager.constans.ApplicationConstants.FROM_URI;
import static com.company.manager.constans.CourseAttrAndParamNames.*;
import static com.company.manager.constans.UserAttrAndParamNames.CURRENT_USER_ID_SESSION;

@Slf4j
public class SaveOrUpdateCourseServlet extends HttpServlet {

    public static final String COURSE_DATE_PATTERN = "dd-MM-yyyy";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving course info from request");
        String name         = request.getParameter(COURSE_TITLE);
        String startDateStr = request.getParameter(COURSE_START_DATE);
        String endDateStr   = request.getParameter(COURSE_END_DATE);
        String uri          = request.getParameter(COURSE_URI);
        String description  = request.getParameter(COURSE_DESCRIPTION);
        String timeTable    = CourseInputHandler.getTimeTableFromRequest(request);

        log.debug("Getting current teacher from DB");
        Long teacherId = (Long)request.getSession(false)
                .getAttribute(CURRENT_USER_ID_SESSION);
        User currentTeacher = UserServiceImpl.getService()
                .getUserById(teacherId);

        log.debug("Building course");
        Course courseToSaveOrUpdate = CourseBuilder.buildCourseFromStringValues(
                name, description, uri, timeTable,
                startDateStr, endDateStr, COURSE_DATE_PATTERN);

        courseToSaveOrUpdate.setTeacher(currentTeacher);

        // Course id was received == course to update
        String courseIdStr = request.getParameter(COURSE_ID);
        if (courseIdStr != null) {
            courseToSaveOrUpdate.setId(Long.parseLong(courseIdStr));
        }

        log.debug("Saving or updating course");
        CourseServiceImpl.getService().saveOrUpdateCourse(courseToSaveOrUpdate);

        String fromURI = request.getParameter(FROM_URI);
        String redirectURI = fromURI != null ? fromURI : String.format("/%s/main-menu", APP_NAME);

        response.sendRedirect(redirectURI);
    }
}
