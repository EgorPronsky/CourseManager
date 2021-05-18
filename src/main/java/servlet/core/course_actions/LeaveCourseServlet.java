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
import java.util.Optional;

import static filter.SessionFilter.APP_DOMAIN_NAME;
import static servlet.core.get_courses.GetCourseToEditServlet.COURSE_ID_PARAM;

@Slf4j
public class LeaveCourseServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving course id to leave");
        long courseIdToLeave = Long.parseLong(request.getParameter(COURSE_ID_PARAM));

        log.debug("Getting course by received id from DB");
        Optional<Course> courseToLeaveOpt = CourseServiceImpl.getService()
                .getCourseById(courseIdToLeave);

        // Course could be already deleted
        if (courseToLeaveOpt.isPresent()) {
            log.debug("Getting current user from DB");
            User currentUser = UserServiceImpl.getService()
                    .getCurrentUserFromDB_OrThrowEx(request);

            log.debug("Updating user in DB");
            currentUser.getCourses().remove(courseToLeaveOpt.get());
            UserServiceImpl.getService().updateUser(currentUser);
        }

        response.sendRedirect(String.format("/%s/main-menu/select-courses", APP_DOMAIN_NAME));
    }
}
