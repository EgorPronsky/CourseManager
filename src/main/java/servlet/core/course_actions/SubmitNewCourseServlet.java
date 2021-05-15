package servlet.core.course_actions;

import domain.course.Course;
import domain.user.User;
import lombok.extern.slf4j.Slf4j;
import services.impl.UserServiceImpl;
import util.creators.CourseCreator;
import handlers.input_handlers.CourseInputHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class SubmitNewCourseServlet extends HttpServlet {

    // Parameter names
    public static final String COURSE_TITLE_PARAM = "course_title";
    public static final String COURSE_START_DATE_PARAM = "course_start_date";
    public static final String COURSE_END_DATE_PARAM = "course_end_date";
    public static final String COURSE_URI_PARAM = "course_uri";
    public static final String COURSE_DESCRIPTION_PARAM = "course_description";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        log.debug("Receiving course info from request");
        String name         = request.getParameter(COURSE_TITLE_PARAM);
        String startDateStr = request.getParameter(COURSE_START_DATE_PARAM);
        String endDateStr   = request.getParameter(COURSE_END_DATE_PARAM);
        String uri          = request.getParameter(COURSE_URI_PARAM);
        String description  = request.getParameter(COURSE_DESCRIPTION_PARAM);
        String timeTable    = CourseInputHandler.getTimeTableFromRequest(request);

        log.debug("Getting current user from DB");
        User currentUser = UserServiceImpl.getService()
                .getCurrentUserFromDB_OrThrowEx(request);

        log.debug("Creating new course from received params");
        Course course = CourseCreator.createCourseFromStrParams(
                name, description,
                startDateStr, endDateStr,
                timeTable,
                uri
        );

        log.debug("Updating teacher in database (and saving new course with it)");
        currentUser.getCourses().add(course);
        UserServiceImpl.getService().updateUser(currentUser);

        response.sendRedirect("main-menu");
    }
}
