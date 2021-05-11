package web.servlet.core.course_actions;

import domain.course.Course;
import domain.user.User;
import lombok.extern.slf4j.Slf4j;
import service.CourseService;
import service.UserService;
import util.CourseCreator;
import util.CourseInputHandler;
import web.servlet.access.SignInServlet;

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
        String name = request.getParameter(COURSE_TITLE_PARAM);
        String startDateStr = request.getParameter(COURSE_START_DATE_PARAM);
        String endDateStr = request.getParameter(COURSE_END_DATE_PARAM);
        String uri = request.getParameter(COURSE_URI_PARAM);
        String description = request.getParameter(COURSE_DESCRIPTION_PARAM);
        String timeTable = CourseInputHandler.getTimeTableFromRequest(request);

        // Current user == teacher
        User courseTeacher = (User)request.getSession()
                .getAttribute(SignInServlet.CURRENT_USER_SESSION_ATTR);

        log.debug("Creating new course from received params");
        Course course = CourseCreator.createCourseFromStrParams(
                name,
                description,
                startDateStr,
                endDateStr,
                timeTable,
                uri
        );

        log.debug("Saving course in database");
        CourseService.getService().save(course);

        log.debug("Updating teacher in session");
        courseTeacher.getCourses().add(course);
        request.getSession().setAttribute(SignInServlet.CURRENT_USER_SESSION_ATTR, courseTeacher);

        log.debug("Updating teacher in database");
        UserService.getService().merge(courseTeacher);

        response.sendRedirect("view/success.jsp");
    }
}
