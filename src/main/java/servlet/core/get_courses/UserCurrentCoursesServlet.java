package servlet.core.get_courses;

import domain.course.Course;
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

@Slf4j
public class UserCurrentCoursesServlet extends HttpServlet {

    public static final String COURSES_ATTR = "courses";
    public static final String COURSES_STATE_ATTR = "courses_state";
    public static final String CURRENT_COURSES_STATE_ATTR_VALUE = "current";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving current user id from session");
        long currentUserId = UserServiceImpl.getService()
                .getCurrentUserIdFromSession_OrThrowEx(request);

        log.debug("Getting user current courses");
        List<Course> currentCourses = CourseServiceImpl.getService()
                .getUserCurrentCourses(currentUserId);

        log.debug("Preparing attributes for response");
        Map<String, Object> respAttrs = new HashMap<>();
        respAttrs.put(COURSES_STATE_ATTR, CURRENT_COURSES_STATE_ATTR_VALUE);
        respAttrs.put(COURSES_ATTR, currentCourses);

        log.debug("Invoking view handler");
        ViewHandler viewHandler = new JspViewHandler();
        viewHandler.renderView("/view/core/view-selected-courses.jsp", respAttrs, request, response);

    }
}
