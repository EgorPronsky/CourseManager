package web.servlet.core.students_actions;

import domain.archive.CourseResult;
import domain.course.Course;
import domain.user.User;
import lombok.extern.slf4j.Slf4j;
import service.CourseService;
import service.StudentCourseResultService;
import util.CourseInputHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class GradeStudentsServlet extends HttpServlet {

    public static final String COURSE_ID_TO_GRADE_STUDENTS_PARAM = "course_id_to_grade_students";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving course id");
        long courseId = Long.parseLong(request.getParameter(COURSE_ID_TO_GRADE_STUDENTS_PARAM));

        log.debug("Finding course by received id");
        Optional<Course> courseOpt = CourseService.getService().getCourseById(courseId);

        if (!courseOpt.isPresent()) {
            throw new InvalidParameterException("Course wasn't found by given id");
        }
        Course course = courseOpt.get();

        log.debug("Receiving student results from request");
        Map<User, CourseResult> studentResults =
                CourseInputHandler.getStudentResultsFromRequest(course, request);

        log.debug("Saving student results in database");
        StudentCourseResultService.getService()
                .saveStudentCourseResults(course, studentResults);

        response.sendRedirect("view/success.jsp");
    }
}
