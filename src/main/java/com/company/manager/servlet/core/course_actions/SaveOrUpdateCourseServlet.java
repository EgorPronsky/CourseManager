package com.company.manager.servlet.core.course_actions;

import com.company.manager.domain.course.Course;
import com.company.manager.domain.course.CourseInfo;
import com.company.manager.domain.user.User;
import com.company.manager.services.impl.UserServiceImpl;
import com.company.manager.servlet.core.get_courses.GetCourseToEditServlet;
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

import static com.company.manager.constans.ApplicationConstants.APP_DOMAIN_NAME;
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

        // Converting string date values to local date
        LocalDate startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern(COURSE_DATE_PATTERN));
        LocalDate endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ofPattern(COURSE_DATE_PATTERN));

        Course courseToSaveOrUpdate = null;

        // If this is a new course -> will return null
        String courseIdToUpdate = request.getParameter(COURSE_ID);
        if (courseIdToUpdate == null) {

            log.debug("Getting current teacher from DB");
            Long teacherId = (Long)request.getSession(false)
                    .getAttribute(CURRENT_USER_ID_SESSION);
            User currentTeacher = UserServiceImpl.getService()
                    .getUserById(teacherId);

            log.debug("Creating new course from received params");
            courseToSaveOrUpdate = Course.builder()
                    .courseInfo(CourseInfo.builder()
                            .name(name).description(description)
                            .startDate(startDate).endDate(endDate)
                            .timeTable(timeTable).uri(uri)
                            .build())
                    .teacher(currentTeacher).build();
        } else {
            log.debug("Getting course by received id from DB");
            courseToSaveOrUpdate = CourseServiceImpl.getService()
                    .getCourseById(Long.parseLong(courseIdToUpdate));

            log.debug("Updating course");
            courseToSaveOrUpdate.getCourseInfo().setName(name);
            courseToSaveOrUpdate.getCourseInfo().setDescription(description);
            courseToSaveOrUpdate.getCourseInfo().setStartDate(startDate);
            courseToSaveOrUpdate.getCourseInfo().setEndDate(endDate);
            courseToSaveOrUpdate.getCourseInfo().setTimeTable(timeTable);
            courseToSaveOrUpdate.getCourseInfo().setUri(uri);
        }

        log.debug("Saving or updating course");
        CourseServiceImpl.getService().saveOrUpdateCourse(courseToSaveOrUpdate);

        response.sendRedirect(String.format("/%s/main-menu/select-courses", APP_DOMAIN_NAME));
    }
}
