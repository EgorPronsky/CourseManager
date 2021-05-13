package servlet.core.students_actions;

import domain.course.Course;
import lombok.extern.slf4j.Slf4j;
import services.impl.CourseServiceImpl;
import handlers.view_handlers.impl.JspViewHandler;
import handlers.view_handlers.ViewHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class GetCourseStudentsServlet extends HttpServlet {

    public static final String COURSE_ID_TO_GET_STUDENTS_PARAM = "course_id_to_get_students";
    public static final String COURSE_ATTR = "course";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving course id from request");
        long courseId = Long.parseLong(request.getParameter(COURSE_ID_TO_GET_STUDENTS_PARAM));

        log.debug("Finding course by received id");
        Optional<Course> courseOpt = CourseServiceImpl.getService().getCourseById(courseId);

        if (!courseOpt.isPresent()) {
            throw new InvalidParameterException("Course wasn't found by given id");
        }
        Course course = courseOpt.get();

        log.debug("Preparing attributes for response");
        Map<String, Object> respAttrs = new HashMap<>();
        respAttrs.put(COURSE_ATTR, course);

        log.debug("Invoking view handler");
        ViewHandler viewHandler = new JspViewHandler();
        viewHandler.renderView("/view/core/course-students.jsp", respAttrs, request, response);
    }
}
