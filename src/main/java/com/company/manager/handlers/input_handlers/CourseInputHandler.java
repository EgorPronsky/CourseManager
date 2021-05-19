package com.company.manager.handlers.input_handlers;

import javax.servlet.http.HttpServletRequest;

import static com.company.manager.constans.CourseAttrAndParamNames.*;

public class CourseInputHandler {

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
