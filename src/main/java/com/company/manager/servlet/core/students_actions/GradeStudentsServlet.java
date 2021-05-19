package com.company.manager.servlet.core.students_actions;

import com.company.manager.domain.archive.CourseResult;
import com.company.manager.domain.course.Course;
import com.company.manager.domain.user.Student;
import com.company.manager.servlet.core.get_courses.GetCourseToEditServlet;
import lombok.extern.slf4j.Slf4j;
import com.company.manager.services.impl.CourseServiceImpl;
import com.company.manager.services.impl.StudentCourseResultServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.company.manager.filter.SessionFilter.APP_DOMAIN_NAME;
import static com.company.manager.servlet.core.get_courses.GetCourseToEditServlet.COURSE_ID_PARAM;

@Slf4j
public class GradeStudentsServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving course id from request");
        long courseId = Long.parseLong(request.getParameter(COURSE_ID_PARAM));

        log.debug("Getting course by received id");
        Course course = CourseServiceImpl.getService().getCourseById(courseId);

        log.debug("Receiving student results from request and saving in map");
        Map<Student, CourseResult> studentResults = new HashMap<>();
        for(Student st : course.getStudents()) {
            String resultStr = request.getParameter(String.valueOf(st.getId()));
            CourseResult res = CourseResult.valueOf(resultStr);
            studentResults.put(st, res);
        }

        log.debug("Saving student results in database");
        StudentCourseResultServiceImpl.getService()
                .saveStudentCourseResults(course, studentResults);

        response.sendRedirect(String.format("/%s/main-menu", APP_DOMAIN_NAME));
    }
}
