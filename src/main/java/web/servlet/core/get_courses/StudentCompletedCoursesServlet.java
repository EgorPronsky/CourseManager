package web.servlet.core.get_courses;

import domain.archive.CourseResult;
import domain.archive.StudentCourseResult;
import domain.course.Course;
import domain.course.CourseState;
import domain.user.User;
import lombok.extern.slf4j.Slf4j;
import service.CourseService;
import service.StudentCourseResultService;
import util.CourseUtil;
import util.StudentCourseResultCreator;
import view_handlers.JspViewHandler;
import view_handlers.ViewHandler;
import web.servlet.access.SignInServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class StudentCompletedCoursesServlet extends HttpServlet {

    public static final String COURSES_RESULTS_ATTR = "courses_results";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User student = (User) request.getSession()
                .getAttribute(SignInServlet.CURRENT_USER_SESSION_ATTR);

        log.debug("Getting student graded courses with results");
        List<StudentCourseResult> gradedCourses =
                StudentCourseResultService.getService()
                        .getCoursesResultsByStudentId(student.getId());


        log.debug("Getting student ungraded finished courses");
        List<StudentCourseResult> ungradedCourses = CourseService.getService()
                .getUserFinishedUngradedCourses(student.getId())
                .stream()
                .map(course -> StudentCourseResultCreator
                                .createStudentCourseResult(student, course, CourseResult.NOT_GRADED))
                .collect(Collectors.toList());


        gradedCourses.addAll(ungradedCourses);
        List<StudentCourseResult> allStudentFinishedCourses = gradedCourses;

        log.debug("Preparing attributes for response");
        Map<String, Object> respAttrs = new HashMap<>();
        respAttrs.put(COURSES_RESULTS_ATTR, allStudentFinishedCourses);

        log.debug("Invoking view handler");
        ViewHandler viewHandler = new JspViewHandler();
        viewHandler.renderView("/view/core/view-completed-courses.jsp", respAttrs, request, response);
    }
}
