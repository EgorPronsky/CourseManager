package com.company.manager.servlet.core.course_actions;

import com.company.manager.domain.course.Course;
import com.company.manager.domain.user.Student;
import com.company.manager.servlet.access.SignInServlet;
import com.company.manager.servlet.core.get_courses.GetCourseToEditServlet;
import lombok.extern.slf4j.Slf4j;
import com.company.manager.services.impl.CourseServiceImpl;
import com.company.manager.services.impl.StudentServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.company.manager.filter.SessionFilter.APP_DOMAIN_NAME;

@Slf4j
public class LeaveCourseServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving course id to leave");
        long courseIdToLeave = Long.parseLong(request.getParameter(GetCourseToEditServlet.COURSE_ID_PARAM));

        log.debug("Getting course by received id from DB");
        Optional<Course> courseToLeaveOpt = CourseServiceImpl.getService()
                .findCourseById(courseIdToLeave);

        // Course could be already deleted
        if (courseToLeaveOpt.isPresent()) {
            log.debug("Getting current student from DB");
            Long studentId = (Long)request.getSession(false)
                    .getAttribute(SignInServlet.CURRENT_USER_ID_SESSION_ATTR);
            Student currentStudent = StudentServiceImpl.getService()
                    .getStudentById(studentId);

            log.debug("Updating student in DB");
            currentStudent.getCourses().remove(courseToLeaveOpt.get());
            StudentServiceImpl.getService().updateStudent(currentStudent);
        }

        response.sendRedirect(String.format("/%s/main-menu/select-courses", APP_DOMAIN_NAME));
    }
}
