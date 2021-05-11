package web.servlet.core.get_courses;

import domain.course.Course;
import domain.user.User;
import lombok.extern.slf4j.Slf4j;
import service.CourseService;
import view_handlers.JspViewHandler;
import view_handlers.ViewHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static web.servlet.access.SignInServlet.CURRENT_USER_SESSION_ATTR;
import static web.servlet.core.get_courses.UserCurrentCoursesServlet.COURSES_STATE_ATTR;
import static web.servlet.core.get_courses.UserCurrentCoursesServlet.SELECTED_COURSES_ATTR;

@Slf4j
public class TeacherNotGradedCoursesServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User teacher = (User) request.getSession()
                .getAttribute(CURRENT_USER_SESSION_ATTR);

        log.debug("Getting teacher not graded courses");
        List<Course> notGradedCourses = CourseService.getService()
                .getUserFinishedUngradedCourses(teacher.getId());

        log.debug("Preparing attributes for response");
        Map<String, Object> respAttrs = new HashMap<>();
        respAttrs.put(COURSES_STATE_ATTR, "not graded");
        respAttrs.put(SELECTED_COURSES_ATTR, notGradedCourses);

        log.debug("Invoking view handler");
        ViewHandler viewHandler = new JspViewHandler();
        viewHandler.renderView("/view/core/view-selected-courses.jsp", respAttrs, request, response);
    }
}
