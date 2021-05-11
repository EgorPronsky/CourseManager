package util;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Slf4j
public class CourseInputValidator {

    public static boolean isStartDateBeforeEndDate(Date startDate, Date endDate) {
        return startDate.before(endDate);
    }

    public static Optional<Date> tryParseStringToDate(String dateStr, String dateFormat) {
        Date date = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
            date = formatter.parse(dateStr);
        } catch (ParseException e) {
            log.debug("Failed to parse string to date");
        }
        return Optional.ofNullable(date);
    }


}
