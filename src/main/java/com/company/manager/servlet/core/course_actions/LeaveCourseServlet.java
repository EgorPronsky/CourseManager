package com.company.manager.servlet.core.course_actions;

import com.company.manager.domain.archive.StudentCourseResult;
import com.company.manager.domain.course.Course;
import com.company.manager.domain.user.User;
import com.company.manager.services.impl.StudentCourseResultServiceImpl;
import com.company.manager.services.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import com.company.manager.services.impl.CourseServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.company.manager.constans.ApplicationConstants.APP_DOMAIN_NAME;
import static com.company.manager.constans.CourseAttrAndParamNames.COURSE_ID;
import static com.company.manager.constans.UserAttrAndParamNames.CURRENT_USER_ID_SESSION;

@Slf4j
public class LeaveCourseServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving course id to leave");
        long courseIdToLeave = Long.parseLong(request.getParameter(COURSE_ID));

        log.debug("Getting course by received id from DB");
        Optional<Course> courseToLeaveOpt = CourseServiceImpl.getService()
                .findCourseById(courseIdToLeave);

        // Course could be already deleted
        if (courseToLeaveOpt.isPresent()) {
            log.debug("Getting current student from DB");
            Long studentId = (Long)request.getSession(false)
                    .getAttribute(CURRENT_USER_ID_SESSION);
            User currentStudent = UserServiceImpl.getService()
                    .getUserById(studentId);

            log.debug("Mapping course to SCR entity");
            StudentCourseResult scrToLeave = StudentCourseResult.builder()
                    .student(currentStudent).course(courseToLeaveOpt.get()).build();

            log.debug("Deleting user from course");
            StudentCourseResultServiceImpl.getService()
                    .deleteStudentCourseResult(scrToLeave);
        }

        response.sendRedirect(String.format("/%s/main-menu/select-courses", APP_DOMAIN_NAME));
    }
}
