package servlet.core.get_courses;

import domain.course.Course;
import domain.user.User;
import lombok.extern.slf4j.Slf4j;
import services.impl.CourseServiceImpl;
import handlers.view_handlers.impl.JspViewHandler;
import handlers.view_handlers.ViewHandler;
import services.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static servlet.core.get_courses.UserCurrentCoursesServlet.COURSES_STATE_ATTR;
import static servlet.core.get_courses.UserCurrentCoursesServlet.SELECTED_COURSES_ATTR;

@Slf4j
public class TeacherNotGradedCoursesServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving current user id from session");
        long currentUserId = UserServiceImpl.getService()
                .getCurrentUserIdFromSession_OrThrowEx(request);

        log.debug("Getting teacher not graded courses");
        List<Course> notGradedCourses = CourseServiceImpl.getService()
                .getUserFinishedUngradedCourses(currentUserId);

        log.debug("Preparing attributes for response");
        Map<String, Object> respAttrs = new HashMap<>();
        respAttrs.put(COURSES_STATE_ATTR, "not graded");
        respAttrs.put(SELECTED_COURSES_ATTR, notGradedCourses);

        log.debug("Invoking view handler");
        ViewHandler viewHandler = new JspViewHandler();
        viewHandler.renderView("/view/core/view-selected-courses.jsp", respAttrs, request, response);
    }
}
