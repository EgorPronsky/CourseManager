package com.company.manager.servlet.core.course_actions;

import com.company.manager.domain.archive.StudentCourseResult;
import com.company.manager.domain.course.Course;
import com.company.manager.domain.user.User;
import com.company.manager.services.impl.UserServiceImpl;
import com.company.manager.util.StudentCourseResultConverter;
import lombok.extern.slf4j.Slf4j;
import com.company.manager.services.impl.CourseServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.company.manager.constans.ApplicationConstants.APP_DOMAIN_NAME;
import static com.company.manager.constans.CourseAttrAndParamNames.COURSES_ID;
import static com.company.manager.constans.UserAttrAndParamNames.CURRENT_USER_ID_SESSION;

@Slf4j
public class JoinNewCoursesServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving courses id");
        String[] coursesStrIdToJoin = request.getParameterValues(COURSES_ID);

        if (coursesStrIdToJoin != null) {
            Set<Long> coursesIdSet = Arrays.stream(coursesStrIdToJoin)
                    // Removing not selected courses (checkboxes)
                    .filter(Objects::nonNull)
                    .map(Long::valueOf)
                    .collect(Collectors.toSet());

            log.debug("Getting courses to join by id from DB");
            List<Course> coursesToJoin = CourseServiceImpl.getService()
                    .getCoursesById(coursesIdSet);

            log.debug("Getting current student from DB");
            Long studentId = (Long) request.getSession(false)
                    .getAttribute(CURRENT_USER_ID_SESSION);
            User currentStudent = UserServiceImpl.getService()
                    .getUserById(studentId);

            log.debug("Mapping courses to SCR entities");
            Set<StudentCourseResult> scrSet = StudentCourseResultConverter
                    .convertStudentCoursesToSCR(currentStudent, coursesToJoin);

            log.debug("Updating student");
            currentStudent.getCourseResults().addAll(scrSet);
            UserServiceImpl.getService().updateUser(currentStudent);
        }

        response.sendRedirect(String.format("/%s/main-menu/select-courses", APP_DOMAIN_NAME));
    }
}
