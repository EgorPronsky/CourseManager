package com.company.manager.servlet.core.students_actions;

import com.company.manager.domain.archive.CourseResult;
import com.company.manager.domain.archive.StudentCourseResult;
import com.company.manager.domain.course.Course;
import com.company.manager.domain.user.User;
import com.company.manager.util.StudentCourseResultConverter;
import lombok.extern.slf4j.Slf4j;
import com.company.manager.services.impl.CourseServiceImpl;
import com.company.manager.services.impl.StudentCourseResultServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.company.manager.constans.ApplicationConstants.FROM_URI;
import static com.company.manager.constans.CourseAttrAndParamNames.COURSE_ID;

@Slf4j
public class GradeStudentsServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving course id from request");
        long courseId = Long.parseLong(request.getParameter(COURSE_ID));

        log.debug("Getting course by received id");
        Course course = CourseServiceImpl.getService().getCourseById(courseId);

        log.debug("Receiving student results from request and saving in map");
        Map<User, CourseResult> studentResults = new HashMap<>();
        for(StudentCourseResult scr : course.getStudentResults()) {
            User student = scr.getStudent();
            String resultStr = request.getParameter(String.valueOf(student.getId()));
            CourseResult result = CourseResult.valueOf(resultStr);
            studentResults.put(student, result);
        }

        log.debug("Converting student results to SCR entities");
        Set<StudentCourseResult> scrSet = StudentCourseResultConverter
                .convertCourseStudentsResultsToSCR(course, studentResults);

        log.debug("Updating student results in database");
        StudentCourseResultServiceImpl.getService()
                .updateStudentCourseResults(scrSet);

        response.sendRedirect(request.getParameter(FROM_URI));
    }
}
