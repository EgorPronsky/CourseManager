package com.company.manager.servlet.core.course_actions;

import com.company.manager.domain.course.Course;
import lombok.extern.slf4j.Slf4j;
import com.company.manager.services.impl.CourseServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.company.manager.filter.SessionFilter.APP_DOMAIN_NAME;
import static com.company.manager.servlet.core.get_courses.GetCourseToEditServlet.COURSE_ID_PARAM;

@Slf4j
public class DeleteCourseServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving course id");
        long courseId = Long.parseLong(request.getParameter(COURSE_ID_PARAM));

        log.debug("Getting course by id from DB");
        Course course = CourseServiceImpl.getService()
                .getCourseById(courseId);

        log.debug("Deleting course from DB");
        CourseServiceImpl.getService().deleteCourse(course);
        
        response.sendRedirect(String.format("/%s/main-menu/select-courses", APP_DOMAIN_NAME));
    }

}
