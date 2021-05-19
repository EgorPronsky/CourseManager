package com.company.manager.servlet.core.course_actions;

import com.company.manager.domain.archive.StudentCourseResult;
import com.company.manager.domain.course.Course;
import com.company.manager.domain.user.User;
import com.company.manager.services.impl.UserServiceImpl;
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
import static com.company.manager.constans.UserAttrAndParamNames.CURRENT_USER_ID_SESSION;

@Slf4j
public class JoinNewCoursesServlet extends HttpServlet {

    public static final String COURSES_ID_PARAM = "courses_id";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Receiving checkbox states");
        Set<Long> coursesIdSet = Arrays.stream(request.getParameterValues(COURSES_ID_PARAM))
                // Removing unchecked checkboxes
                .filter(Objects::nonNull)
                .map(Long::valueOf)
                .collect(Collectors.toSet());

        log.debug("Getting current student from DB");
        Long studentId = (Long)request.getSession(false)
                .getAttribute(CURRENT_USER_ID_SESSION);
        User currentStudent = UserServiceImpl.getService()
                .getUserById(studentId);

        log.debug("Getting courses to join by id from DB");
        List<Course> coursesToJoin = CourseServiceImpl.getService()
                .getCoursesById(coursesIdSet);

        log.debug("Mapping courses to SCR entities");
        Set<StudentCourseResult> scrList = coursesToJoin.stream()
                .map(course -> StudentCourseResult.builder()
                        .course(course).student(currentStudent).build())
                .collect(Collectors.toSet());

        log.debug("Updating student");
        currentStudent.getCourseResults().addAll(scrList);
        UserServiceImpl.getService().updateUser(currentStudent);

        response.sendRedirect(String.format("/%s/main-menu", APP_DOMAIN_NAME));
    }
}
