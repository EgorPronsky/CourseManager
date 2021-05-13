package servlet.core.course_actions;

import domain.course.Course;
import domain.user.User;
import lombok.extern.slf4j.Slf4j;
import services.impl.CourseServiceImpl;
import services.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Optional;

@Slf4j
public class LeaveCourseServlet extends HttpServlet {

    public static final String COURSE_TO_LEAVE_ID_PARAM = "course_to_leave_id";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving course to leave id");
        long courseIdToLeave =
                Long.parseLong(request.getParameter(COURSE_TO_LEAVE_ID_PARAM));

        log.debug("Getting course from DB by received id");
        Optional<Course> courseToLeaveOpt = CourseServiceImpl.getService().getCourseById(courseIdToLeave);
        if (!courseToLeaveOpt.isPresent())
            throw new InvalidParameterException("Course wasn't received");
        Course courseToLeave = courseToLeaveOpt.get();

        log.debug("Getting current user from DB");
        User currentUser = UserServiceImpl.getService()
                .getCurrentUserFromDB_OrThrowEx(request);

        log.debug("Updating user in DB");
        currentUser.getCourses().remove(courseToLeave);
        UserServiceImpl.getService().updateUser(currentUser);

        response.sendRedirect("main-menu");
    }
}
