package web.servlet.core.course_actions;

import domain.course.Course;
import domain.user.User;
import lombok.extern.slf4j.Slf4j;
import service.CourseService;
import view_handlers.JspViewHandler;
import view_handlers.ViewHandler;
import web.servlet.access.SignInServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static web.servlet.access.SignInServlet.CURRENT_USER_SESSION_ATTR;

@Slf4j
public class GetAvailableCoursesServlet extends HttpServlet {

    public static final String AVAILABLE_COURSES_ATTR = "available_courses";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User currentUser = (User)request.getSession()
                .getAttribute(CURRENT_USER_SESSION_ATTR);

        log.debug("Preparing available courses for the user");
        List<Course> availableCourses = CourseService.getService()
                .getAllFutureCourses().stream()
                // Removing courses that student already has
                .filter(course -> !currentUser.getCourses().contains(course))
                .collect(Collectors.toList());

        log.debug("Preparing attributes for response");
        Map<String, Object> respAttrs = new HashMap<>();
        respAttrs.put(AVAILABLE_COURSES_ATTR, availableCourses);

        log.debug("Invoking view handler");
        ViewHandler viewHandler = new JspViewHandler();
        viewHandler.renderView("/view/core/join/join-new-courses.jsp", respAttrs, request, response);
    }
}
