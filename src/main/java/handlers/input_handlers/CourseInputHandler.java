package handlers.input_handlers;

import domain.archive.CourseResult;
import domain.course.Course;
import domain.user.User;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class CourseInputHandler {

    public static final String MONDAY_PARAM = "monday";
    public static final String MONDAY_START_TIME_PARAM = "monday_start_date";
    public static final String MONDAY_END_TIME_PARAM = "monday_end_date";

    public static final String TUESDAY_PARAM = "tuesday";
    public static final String TUESDAY_START_TIME_PARAM = "tuesday_start_date";
    public static final String TUESDAY_END_TIME_PARAM = "tuesday_end_date";

    public static final String WEDNESDAY_PARAM = "wednesday";
    public static final String WEDNESDAY_START_TIME_PARAM = "wednesday_start_date";
    public static final String WEDNESDAY_END_TIME_PARAM = "wednesday_end_date";

    public static final String THURSDAY_PARAM = "thursday";
    public static final String THURSDAY_START_DATE_PARAM = "thursday_start_date";
    public static final String THURSDAY_END_DATE_PARAM = "thursday_end_date";

    public static final String FRIDAY_PARAM = "friday";
    public static final String FRIDAY_START_DATE_PARAM = "friday_start_date";
    public static final String FRIDAY_END_DATE_PARAM = "friday_end_date";

    public static final String SATURDAY_PARAM = "saturday";
    public static final String SATURDAY_START_DATE_PARAM = "saturday_start_date";
    public static final String SATURDAY_END_DATE_PARAM = "saturday_end_date";

    public static final String SUNDAY_PARAM = "sunday";
    public static final String SUNDAY_START_DATE_PARAM = "sunday_start_date";
    public static final String SUNDAY_END_DATE_PARAM = "sunday_end_date";

    public static String getTimeTableFromRequest(HttpServletRequest req) {
        String timeTable = "";

        // Verify checkboxes states (days of week)
        if (req.getParameter(MONDAY_PARAM) != null) {
            timeTable += "Monday " +
                    getTimeTableRow(req, MONDAY_START_TIME_PARAM, MONDAY_END_TIME_PARAM) + "\n";
        }
        if (req.getParameter(TUESDAY_PARAM) != null) {
            timeTable += "Tuesday " +
                    getTimeTableRow(req, TUESDAY_START_TIME_PARAM, TUESDAY_END_TIME_PARAM) + "\n";
        }
        if (req.getParameter(WEDNESDAY_PARAM) != null) {
            timeTable += "Wednesday " +
                    getTimeTableRow(req, WEDNESDAY_START_TIME_PARAM, WEDNESDAY_END_TIME_PARAM) + "\n";
        }
        if (req.getParameter(THURSDAY_PARAM) != null) {
            timeTable += "Thursday " +
                    getTimeTableRow(req, THURSDAY_START_DATE_PARAM, THURSDAY_END_DATE_PARAM) + "\n";
        }
        if (req.getParameter(FRIDAY_PARAM) != null) {
            timeTable += "Friday " +
                    getTimeTableRow(req, FRIDAY_START_DATE_PARAM, FRIDAY_END_DATE_PARAM) + "\n";
        }
        if (req.getParameter(SATURDAY_PARAM) != null) {
            timeTable += "Saturday " +
                    getTimeTableRow(req, SATURDAY_START_DATE_PARAM, SATURDAY_END_DATE_PARAM) + "\n";
        }
        if (req.getParameter(SUNDAY_PARAM) != null) {
            timeTable += "Sunday " +
                    getTimeTableRow(req, SUNDAY_START_DATE_PARAM, SUNDAY_END_DATE_PARAM) + "\n";
        }

        return timeTable;
    }

    private static String getTimeTableRow(HttpServletRequest req, String startTimeParamName, String endTimeParamName) {
        return req.getParameter(startTimeParamName) + "-" + req.getParameter(endTimeParamName);
    }

    public static Map<User, CourseResult> getStudentResultsFromRequest(Course course, HttpServletRequest req) {
        Map<User, CourseResult> studentResults = new HashMap<>();
        for(User student : course.getStudentsList()) {
            String resultStr = req.getParameter(String.valueOf(student.getId()));
            CourseResult result = CourseResult.valueOf(resultStr);
            studentResults.put(student, result);
        }
        return studentResults;
    }

}
