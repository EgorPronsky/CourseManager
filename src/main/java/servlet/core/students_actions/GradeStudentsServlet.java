package servlet.core.students_actions;

import domain.archive.CourseResult;
import domain.course.Course;
import domain.user.User;
import lombok.extern.slf4j.Slf4j;
import services.impl.CourseServiceImpl;
import services.impl.StudentCourseResultServiceImpl;
import handlers.input_handlers.CourseInputHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Map;
import java.util.Optional;

import static filter.SessionFilter.APP_DOMAIN_NAME;

@Slf4j
public class GradeStudentsServlet extends HttpServlet {

    public static final String COURSE_ID_TO_GRADE_STUDENTS_PARAM = "course_id_to_grade_students";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving course id from request");
        long courseId = Long.parseLong(request.getParameter(COURSE_ID_TO_GRADE_STUDENTS_PARAM));

        log.debug("Finding course by received id");
        Course course = CourseServiceImpl.getService()
                .getCourseById_OrThrowEx(courseId);

        log.debug("Receiving student results from request and saving in map");
        Map<User, CourseResult> studentResults =
                CourseInputHandler.getStudentResultsFromRequest(course, request);

        log.debug("Saving student results in database");
        StudentCourseResultServiceImpl.getService()
                .saveStudentCourseResults(course, studentResults);

        response.sendRedirect(String.format("/%s/main-menu", APP_DOMAIN_NAME));
    }
}
