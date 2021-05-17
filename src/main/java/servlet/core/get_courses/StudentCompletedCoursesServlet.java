package servlet.core.get_courses;

import domain.archive.CourseResult;
import domain.archive.StudentCourseResult;
import domain.user.User;
import lombok.extern.slf4j.Slf4j;
import services.UserService;
import services.impl.CourseServiceImpl;
import services.impl.StudentCourseResultServiceImpl;
import services.impl.UserServiceImpl;
import servlet.access.SignInServlet;
import util.creators.StudentCourseResultCreator;
import handlers.view_handlers.impl.JspViewHandler;
import handlers.view_handlers.ViewHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class StudentCompletedCoursesServlet extends HttpServlet {

    public static final String COURSES_RESULTS_ATTR = "courses_results";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving current user id from session");
        long currentUserId = UserServiceImpl.getService()
                .getCurrentUserIdFromSession_OrThrowEx(request);

        log.debug("Getting student finished graded courses");
        List<StudentCourseResult> gradedCourses =
                StudentCourseResultServiceImpl.getService()
                        .getCoursesResultsByStudentId(currentUserId);

        User user = UserServiceImpl.getService()
                .findUserById_OrThrowEx(currentUserId);

        log.debug("Getting student finished ungraded courses");
        List<StudentCourseResult> ungradedCourses = CourseServiceImpl.getService()
                .getStudentFinishedUngradedCourses(currentUserId).stream()
                .map(course -> StudentCourseResultCreator
                                .createStudentCourseResult(user, course, CourseResult.NOT_GRADED))
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
