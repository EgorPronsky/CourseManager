package web.servlet.core.course_actions;

import domain.course.Course;
import domain.user.User;
import lombok.extern.slf4j.Slf4j;
import service.CourseService;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static web.servlet.access.SignInServlet.CURRENT_USER_SESSION_ATTR;

@Slf4j
public class JoinNewCoursesServlet extends HttpServlet {

    public static final String COURSES_TO_JOIN_ID_PARAM = "courses_to_join_id";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving checkbox states");
        List<Long> coursesIdList = Arrays.stream(request.getParameterValues(COURSES_TO_JOIN_ID_PARAM))
                // Removing unchecked checkboxes
                .filter(Objects::nonNull)
                .map(Long::valueOf)
                .collect(Collectors.toList());

        log.debug("Getting courses to join by id");
        List<Course> coursesToJoin = CourseService.getService()
                .getAllCoursesById(coursesIdList);

        log.debug("Updating user in session");
        User currentUser = (User)request.getSession()
                .getAttribute(CURRENT_USER_SESSION_ATTR);
        currentUser.getCourses().addAll(coursesToJoin);
        request.getSession().setAttribute(CURRENT_USER_SESSION_ATTR, currentUser);

        log.debug("Updating user in database");
        UserService.getService().merge(currentUser);

        response.sendRedirect("view/success.jsp");
    }
}
