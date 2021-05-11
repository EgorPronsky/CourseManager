package web.servlet.core.get_courses;

import domain.course.Course;
import domain.course.CourseState;
import domain.user.User;
import lombok.extern.slf4j.Slf4j;
import util.CourseUtil;
import view_handlers.JspViewHandler;
import view_handlers.ViewHandler;
import web.servlet.access.SignInServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static web.servlet.access.SignInServlet.CURRENT_USER_SESSION_ATTR;

@Slf4j
public class UserCurrentCoursesServlet extends HttpServlet {

    public static final String SELECTED_COURSES_ATTR = "selected_courses";
    public static final String COURSES_STATE_ATTR = "courses_state";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User currentUser = (User) request.getSession()
                .getAttribute(CURRENT_USER_SESSION_ATTR);

        log.debug("Getting user current courses");
        List<Course> currentCourses = CourseUtil
                .getFilteredCourses(currentUser.getCourses(), CourseState.CURRENT);

        log.debug("Preparing attributes for response");
        Map<String, Object> respAttrs = new HashMap<>();
        respAttrs.put(COURSES_STATE_ATTR, "current");
        respAttrs.put(SELECTED_COURSES_ATTR, currentCourses);

        log.debug("Invoking view handler");
        ViewHandler viewHandler = new JspViewHandler();
        viewHandler.renderView("/view/core/view-selected-courses.jsp", respAttrs, request, response);

    }
}
