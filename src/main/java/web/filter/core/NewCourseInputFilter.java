package web.filter.core;

import javax.servlet.*;
import java.io.IOException;

public class NewCourseInputFilter implements Filter {

    public void destroy() { }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        // Receiving course input data to check on validity
        //String startDateStr = req.getParameter("course_start_date");
        //String endDateStr = req.getParameter("course_end_date");

        //Optional<Date> startDate =  CourseInputValidator.tryParseStringToDate(startDateStr, "dd/MM/yyyy");
        //Optional<Date> endDate =  CourseInputValidator.tryParseStringToDate(startDateStr, "dd/MM/yyyy");





        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException { }

}
