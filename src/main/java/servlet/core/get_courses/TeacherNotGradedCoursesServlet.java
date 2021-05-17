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

import static servlet.core.get_courses.UserCurrentCoursesServlet.*;

@Slf4j
public class TeacherNotGradedCoursesServlet extends HttpServlet {

    public static final String NOT_GRADED_COURSES_STATE_ATTR_VALUE = "not graded";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving current user id from session");
        long currentUserId = UserServiceImpl.getService()
                .getCurrentUserIdFromSession_OrThrowEx(request);

        log.debug("Getting teacher not graded courses");
        List<Course> notGradedCourses = CourseServiceImpl.getService()
                .getTeacherFinishedNotGradedCourses(currentUserId);

        log.debug("Preparing attributes for response");
        Map<String, Object> respAttrs = new HashMap<>();
        respAttrs.put(COURSES_STATE_ATTR, NOT_GRADED_COURSES_STATE_ATTR_VALUE);
        respAttrs.put(COURSES_ATTR, notGradedCourses);

        log.debug("Invoking view handler");
        ViewHandler viewHandler = new JspViewHandler();
        viewHandler.renderView("/view/core/view-selected-courses.jsp", respAttrs, request, response);
    }
}
