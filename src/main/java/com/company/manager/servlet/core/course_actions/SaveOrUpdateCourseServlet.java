package com.company.manager.servlet.core.course_actions;

import com.company.manager.domain.course.Course;
import com.company.manager.domain.course.CourseInfo;
import com.company.manager.domain.user.Teacher;
import com.company.manager.servlet.access.SignInServlet;
import com.company.manager.servlet.core.get_courses.GetCourseToEditServlet;
import lombok.extern.slf4j.Slf4j;
import com.company.manager.services.impl.CourseServiceImpl;
import com.company.manager.services.impl.TeacherServiceImpl;
import com.company.manager.handlers.input_handlers.CourseInputHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.company.manager.filter.SessionFilter.APP_DOMAIN_NAME;

@Slf4j
public class SaveOrUpdateCourseServlet extends HttpServlet {

    // Parameter names
    public static final String COURSE_TITLE_PARAM = "course_title";
    public static final String COURSE_START_DATE_PARAM = "course_start_date";
    public static final String COURSE_END_DATE_PARAM = "course_end_date";
    public static final String COURSE_URI_PARAM = "course_uri";
    public static final String COURSE_DESCRIPTION_PARAM = "course_description";

    public static final String COURSE_DATE_PATTERN = "dd-MM-yyyy";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving course info from request");
        String name         = request.getParameter(COURSE_TITLE_PARAM);
        String startDateStr = request.getParameter(COURSE_START_DATE_PARAM);
        String endDateStr   = request.getParameter(COURSE_END_DATE_PARAM);
        String uri          = request.getParameter(COURSE_URI_PARAM);
        String description  = request.getParameter(COURSE_DESCRIPTION_PARAM);
        String timeTable    = CourseInputHandler.getTimeTableFromRequest(request);

        // Converting string date values to local date
        LocalDate startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern(COURSE_DATE_PATTERN));
        LocalDate endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ofPattern(COURSE_DATE_PATTERN));

        Course courseToSaveOrUpdate = null;

        // If this is a new course -> will return null
        String courseIdToUpdate = request.getParameter(GetCourseToEditServlet.COURSE_ID_PARAM);
        if (courseIdToUpdate == null) {

            log.debug("Getting current teacher from DB");
            Long teacherId = (Long)request.getSession(false)
                    .getAttribute(SignInServlet.CURRENT_USER_ID_SESSION_ATTR);
            Teacher currentTeacher = TeacherServiceImpl.getService()
                    .getTeacherById(teacherId);

            log.debug("Creating new course from received params");
            courseToSaveOrUpdate = Course.builder()
                    .courseInfo(CourseInfo.builder()
                            .name(name).description(description)
                            .startDate(startDate).endDate(endDate)
                            .timeTable(timeTable).uri(uri)
                            .build())
                    .teacher(currentTeacher)
                    .build();
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
