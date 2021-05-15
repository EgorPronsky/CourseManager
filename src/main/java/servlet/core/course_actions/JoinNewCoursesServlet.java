package servlet.core.course_actions;

import domain.course.Course;
import domain.user.User;
import lombok.extern.slf4j.Slf4j;
import services.impl.CourseServiceImpl;
import services.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static filter.SessionFilter.APP_DOMAIN_NAME;

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

        log.debug("Getting courses to join from DB by id");
        List<Course> coursesToJoin = CourseServiceImpl.getService()
                .getAllCoursesById(coursesIdList);

        log.debug("Getting current user from DB");
        User currentUser = UserServiceImpl.getService()
                .getCurrentUserFromDB_OrThrowEx(request);

        log.debug("Updating user in DB");
        currentUser.getCourses().addAll(coursesToJoin);
        UserServiceImpl.getService().updateUser(currentUser);

        response.sendRedirect(String.format("/%s/main-menu", APP_DOMAIN_NAME));
    }
}
