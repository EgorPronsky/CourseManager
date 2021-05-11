package web.servlet.core.course_actions;

import domain.course.Course;
import domain.user.User;
import lombok.extern.slf4j.Slf4j;
import service.CourseService;
import web.servlet.access.SignInServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Optional;

import static web.servlet.access.SignInServlet.CURRENT_USER_SESSION_ATTR;

@Slf4j
public class LeaveCourseServlet extends HttpServlet {

    public static final String COURSE_TO_LEAVE_ID_PARAM = "course_to_leave_id";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User currentUser = (User)request.getSession()
                .getAttribute(CURRENT_USER_SESSION_ATTR);

        log.debug("Receiving course to leave id");
        long courseIdToLeave =
                Long.parseLong(request.getParameter(COURSE_TO_LEAVE_ID_PARAM));
        Optional<Course> courseToLeaveOpt = CourseService.getService().getCourseById(courseIdToLeave);

        if (!courseToLeaveOpt.isPresent()) {
            throw new InvalidParameterException("Invalid course id was given");
        }
        Course courseToLeave = courseToLeaveOpt.get();

        log.debug("Deleting user from course students list");
        courseToLeave.getStudentsList().remove(currentUser);

        log.debug("Updating user in session");
        currentUser.getCourses().remove(courseToLeave);
        request.getSession().setAttribute(CURRENT_USER_SESSION_ATTR, currentUser);

        log.debug("Updating course in database");
        CourseService.getService().merge(courseToLeave);

        response.sendRedirect("view/success.jsp");
    }
}
