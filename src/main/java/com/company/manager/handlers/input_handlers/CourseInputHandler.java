package com.company.manager.handlers.input_handlers;

import com.company.manager.domain.archive.CourseResult;
import com.company.manager.domain.archive.StudentCourseResult;
import com.company.manager.domain.course.Course;
import com.company.manager.domain.user.User;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

import static com.company.manager.constans.CourseAttrAndParamNames.*;

public class CourseInputHandler {

    public static Map<User, CourseResult> getStudentResultsFromRequest(Course course, HttpServletRequest req) {
        Map<User, CourseResult> studentResults = new HashMap<>();
        for(StudentCourseResult scr : course.getStudentResults()) {
            User student = scr.getStudent();
            String resultStr = req.getParameter(String.valueOf(student.getId()));
            CourseResult result = CourseResult.valueOf(resultStr);
            studentResults.put(student, result);
        }
        return studentResults;
    }

    public static String getTimeTableFromRequest(HttpServletRequest req) {
        String timeTable = "";
        if (req.getParameter(MONDAY) != null) {
            timeTable += "Monday " +
                    getTimeTableRow(req, MONDAY_START_TIME, MONDAY_END_TIME) + "\n";
        }
        if (req.getParameter(TUESDAY) != null) {
            timeTable += "Tuesday " +
                    getTimeTableRow(req, TUESDAY_START_TIME, TUESDAY_END_TIME) + "\n";
        }
        if (req.getParameter(WEDNESDAY) != null) {
            timeTable += "Wednesday " +
                    getTimeTableRow(req, WEDNESDAY_START_TIME, WEDNESDAY_END_TIME) + "\n";
        }
        if (req.getParameter(THURSDAY) != null) {
            timeTable += "Thursday " +
                    getTimeTableRow(req, THURSDAY_START_DATE, THURSDAY_END_DATE) + "\n";
        }
        if (req.getParameter(FRIDAY) != null) {
            timeTable += "Friday " +
                    getTimeTableRow(req, FRIDAY_START_DATE, FRIDAY_END_DATE) + "\n";
        }
        if (req.getParameter(SATURDAY) != null) {
            timeTable += "Saturday " +
                    getTimeTableRow(req, SATURDAY_START_DATE, SATURDAY_END_DATE) + "\n";
        }
        if (req.getParameter(SUNDAY) != null) {
            timeTable += "Sunday " +
                    getTimeTableRow(req, SUNDAY_START_DATE, SUNDAY_END_DATE) + "\n";
        }
        return timeTable;
    }

    private static String getTimeTableRow(HttpServletRequest req, String startTimeParamName, String endTimeParamName) {
        return req.getParameter(startTimeParamName) + "-" + req.getParameter(endTimeParamName);
    }

}
