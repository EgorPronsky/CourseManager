package com.company.manager.servlet.core.students_actions;

import com.company.manager.domain.archive.CourseResult;
import com.company.manager.domain.archive.StudentCourseResult;
import com.company.manager.domain.course.Course;
import com.company.manager.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import com.company.manager.services.impl.CourseServiceImpl;
import com.company.manager.services.impl.StudentCourseResultServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static com.company.manager.constans.ApplicationConstants.APP_DOMAIN_NAME;
import static com.company.manager.constans.CourseAttrAndParamNames.COURSE_ID;

@Slf4j
public class GradeStudentsServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving course id from request");
        long courseId = Long.parseLong(request.getParameter(COURSE_ID));

        log.debug("Getting course by received id");
        Course course = CourseServiceImpl.getService().getCourseById(courseId);

        log.debug("Receiving student results from request and saving in map");
        Set<StudentCourseResult> scrSet = new HashSet<>();
        for(StudentCourseResult scr : course.getStudentResults()) {
            User student = scr.getStudent();

            String resultStr = request.getParameter(String.valueOf(student.getId()));
            CourseResult result = CourseResult.valueOf(resultStr);

            scrSet.add(StudentCourseResult.builder()
                    .course(course).student(student)
                    .result(result).build());
        }

        log.debug("Saving student results in database");
        StudentCourseResultServiceImpl.getService()
                .saveStudentCourseResults(scrSet);

        response.sendRedirect(String.format("/%s/main-menu", APP_DOMAIN_NAME));
    }
}
