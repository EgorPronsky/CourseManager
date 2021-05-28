<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="static com.company.manager.string_constans.CourseAttrAndParamNames.*" %>
<%@ page import="static com.company.manager.servlet.core.course_actions.SaveOrUpdateCourseServlet.COURSE_DATE_PATTERN" %>
<%@ page import="com.company.manager.domain.course.Course" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="static com.company.manager.string_constans.ApplicationConstants.FROM_URI" %>
<%@ page import="static com.company.manager.string_constans.UserAttrAndParamNames.WEB_PAGE_CURRENT_USER_ID" %>
<%@ page import="static com.company.manager.string_constans.UserAttrAndParamNames.SESSION_CURRENT_USER_ID" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%-- Prepare variables --%>
<c:set var="course_title" value="<%=COURSE_TITLE%>" />
<c:set var="course_description" value="<%=COURSE_DESCRIPTION%>" />
<c:set var="course_start_date" value="<%=COURSE_START_DATE%>" />
<c:set var="course_end_date" value="<%=COURSE_END_DATE%>" />
<c:set var="course_uri" value="<%=COURSE_URI%>" />
<c:set var="course_id" value="<%=COURSE_ID%>" />
<c:set var="course_date_pattern" value="<%=COURSE_DATE_PATTERN%>" />

<c:set var="course" value="<%=request.getAttribute(COURSE)%>" />

<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script type = "text/javascript">
        function validateCourseInput() {
            const datePattern = '${course_date_pattern}'.toUpperCase();

            const courseStartDateStr = document.getElementById("start_date").value;
            const courseEndDateStr = document.getElementById("end_date").value;

            // Date validation
            if (!isDateValid(courseStartDateStr, datePattern)) {
                alert( "Please provide existing start date" );
                event.preventDefault();
                return false;
            }
            if (!isDateValid(courseEndDateStr, datePattern)) {
                alert( "Please provide existing end date" );
                event.preventDefault();
                return false;
            }

            const startDate = moment(courseStartDateStr, datePattern);
            const endDate = moment(courseEndDateStr, datePattern);

            if (endDate <= startDate) {
                alert( "End date should be greater than start date" );
                event.preventDefault();
                return false;
            }
            return true;
        }

        function isDateValid(courseStartDateStr, datePattern) {
            return moment(courseStartDateStr, datePattern, true).isValid();
        }
    </script>
    <title>Create or update course</title>
</head>

<c:if test="${not empty course}">
    <%-- Formatting dates --%>
    <% DateTimeFormatter formatter = DateTimeFormatter.ofPattern(COURSE_DATE_PATTERN);
        Course course = (Course) pageContext.getAttribute("course");
    %>
    <c:set var="course_start_date_formatted" value="<%=
        course.getCourseInfo().getStartDate().format(formatter) %>" />
    <c:set var="course_end_date_formatted" value="<%=
        course.getCourseInfo().getEndDate().format(formatter) %>" />
</c:if>

<body>
<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-lg-10">
            <div class="card">

                <%-- Header --%>
                <div class="card-header">
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item">
                            <a href="${pageContext.request.contextPath}/main-menu?<%=WEB_PAGE_CURRENT_USER_ID%>=<%=session.getAttribute(SESSION_CURRENT_USER_ID)%>" style="font-size: large">Home</a>
                        </li>
                        <c:if test="${empty course}">
                            <li class="breadcrumb-item active" aria-current="page" style="font-size: large">Create a new course</li>
                        </c:if>
                        <c:if test="${not empty course}">
                            <li class="breadcrumb-item">
                                <a href="${pageContext.request.contextPath}/main-menu/select-courses?<%=WEB_PAGE_CURRENT_USER_ID%>=<%=session.getAttribute(SESSION_CURRENT_USER_ID)%>" style="font-size: large">Select my courses</a>
                            </li>
                            <li class="breadcrumb-item">
                                <a href="<%=request.getParameter(FROM_URI)%>?<%=WEB_PAGE_CURRENT_USER_ID%>=<%=session.getAttribute(SESSION_CURRENT_USER_ID)%>" style="font-size: large">Back to courses</a>
                            </li>
                            <li class="breadcrumb-item active" aria-current="page" style="font-size: large">Edit course</li>
                        </c:if>
                    </ol>
                </div>

                <%-- Body --%>
                <div class="card-body">
                    <form accept-charset="UTF-8" name="courseForm" onsubmit="return(validateCourseInput());" action="${pageContext.request.contextPath}/main-menu/create-or-update-course" method="post">
                        <fieldset>

                            <div class="row">

                                <!--Left column-->
                                <div class="col">
                                    <!--Title-->
                                    <div class="form-group">
                                        <label for="title">Title</label>
                                        <input
                                                class="form-control"
                                                id="title"
                                                name="${course_title}"
                                                type="text"
                                                <c:if test="${not empty course}">
                                                        value="${course.courseInfo.name}"
                                                </c:if>
                                                <c:if test="${empty course}">
                                                        value="Course#"
                                                </c:if>
                                                required>
                                    </div>

                                    <c:if test="${not empty course}">
                                        <%-- Formatting dates --%>
                                        <% DateTimeFormatter formatter = DateTimeFormatter.ofPattern(COURSE_DATE_PATTERN);
                                            Course course = (Course) pageContext.getAttribute("course");
                                        %>
                                        <c:set var="course_start_date_formatted" value="<%=
                                            course.getCourseInfo().getStartDate().format(formatter) %>" />
                                        <c:set var="course_end_date_formatted" value="<%=
                                            course.getCourseInfo().getEndDate().format(formatter) %>" />
                                    </c:if>

                                    <div class="form-row">
                                        <!--Start date-->
                                        <div class="form-group col">
                                            <label for="start_date">Start date</label>
                                            <input
                                                    class="form-control"
                                                    placeholder="${course_date_pattern}"
                                                    id="start_date"
                                                    name="${course_start_date}"
                                                    type="text"
                                                    <c:if test="${not empty course}">
                                                        value="${course_start_date_formatted}"
                                                    </c:if>
                                                    <c:if test="${empty course}">
                                                        value="01-01-2022"
                                                    </c:if>
                                                    pattern="[0-9]{2}-[0-9]{2}-[0-9]{4}$"
                                                    required>
                                        </div>

                                        <!--End date-->
                                        <div class="form-group col">
                                            <label for="end_date">End date</label>
                                            <input
                                                    class="form-control"
                                                    placeholder="${course_date_pattern}"
                                                    id="end_date"
                                                    name="${course_end_date}"
                                                    type="text"
                                                    <c:if test="${not empty course}">
                                                            value="${course_end_date_formatted}"
                                                    </c:if>
                                                    <c:if test="${empty course}">
                                                            value="31-12-2023"
                                                    </c:if>
                                                    pattern="[0-9]{2}-[0-9]{2}-[0-9]{4}$"
                                                    required>
                                        </div>
                                    </div>

                                    <!--URI for connection-->
                                    <div class="form-group">
                                        <label for="uri">URI for connection</label>
                                        <input
                                                class="form-control"
                                                id="uri"
                                                name="${course_uri}"
                                                type="text"
                                                <c:if test="${not empty course}">
                                                    value="${course.courseInfo.uri}"
                                                </c:if>
                                                <c:if test="${empty course}">
                                                    value="https://zoom.us/meetings"
                                                </c:if>
                                                >
                                    </div>

                                    <!--Description-->
                                    <div class="form-group">
                                        <label for="description">Description</label>
                                        <textarea
                                                class="form-control"
                                                id="description"
                                                name="${course_description}"
                                                type="text"
                                                rows="6"
                                                required><c:if test="${not empty course}">${course.courseInfo.description}</c:if><c:if test="${empty course}">Some description</c:if>
                                        </textarea>
                                    </div>
                                </div>

                                <!--Right column-->
                                <div class="col">

                                    <!--Time table-->
                                    <table class="table table-striped">
                                        <thead>
                                        <tr>
                                            <th colspan="2">Day of week</th>
                                            <th scope="col">Start time</th>
                                            <th scope="col">End time</th>
                                        </tr>
                                        </thead>
                                        <tbody>

                                        <!--Monday-->
                                        <c:set var="monday" value="<%=MONDAY%>" />
                                        <c:set var="monday_start_time" value="<%=MONDAY_START_TIME%>" />
                                        <c:set var="monday_end_time" value="<%=MONDAY_END_TIME%>" />
                                        <tr>
                                            <td>
                                                <input name="${monday}" type="checkbox" value="monday">
                                            </td>
                                            <td>Monday</td>
                                            <td>
                                                <div class="checkbox">
                                                    <input class="form-control form-control-sm" placeholder="12:00" name="${monday_start_time}" value="12:00" pattern="^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$" type="text">
                                                </div>
                                            </td>
                                            <td>
                                                <div class="checkbox">
                                                    <input class="form-control form-control-sm" placeholder="14:00" id="monday_end" name="${monday_end_time}" pattern="^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$" value="14:00" type="text">
                                                </div>
                                            </td>
                                        </tr>

                                        <!--Tuesday-->
                                        <c:set var="tuesday" value="<%=TUESDAY%>" />
                                        <c:set var="tuesday_start_time" value="<%=TUESDAY_START_TIME%>" />
                                        <c:set var="tuesday_end_time" value="<%=TUESDAY_END_TIME%>" />
                                        <tr>
                                            <td>
                                                <input name="${tuesday}" type="checkbox" value="tuesday">
                                            </td>
                                            <td>Tuesday</td>
                                            <td>
                                                <div class="checkbox">
                                                    <input class="form-control form-control-sm" placeholder="12:00" pattern="^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$" name="${tuesday_start_time}" value="12:00" type="text">
                                                </div>
                                            </td>
                                            <td>
                                                <div class="checkbox">
                                                    <input class="form-control form-control-sm" placeholder="14:00" pattern="^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$" name="${tuesday_end_time}" value="14:00" type="text">
                                                </div>
                                            </td>
                                        </tr>

                                        <!--Wednesday-->
                                        <c:set var="wednesday" value="<%=WEDNESDAY%>" />
                                        <c:set var="wednesday_start_time" value="<%=WEDNESDAY_START_TIME%>" />
                                        <c:set var="wednesday_end_time" value="<%=WEDNESDAY_END_TIME%>" />
                                        <tr>
                                            <td>
                                                <input name="${wednesday}" id="wednesday" type="checkbox" value="wednesday">
                                            </td>
                                            <td>Wednesday</td>
                                            <td>
                                                <div class="checkbox">
                                                    <input class="form-control form-control-sm" placeholder="12:00" pattern="^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$" name="${wednesday_start_time}" value="12:00" type="text">
                                                </div>
                                            </td>
                                            <td>
                                                <div class="checkbox">
                                                    <input class="form-control form-control-sm" placeholder="14:00" pattern="^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$" name="${wednesday_end_time}" value="14:00" type="text">
                                                </div>
                                            </td>
                                        </tr>

                                        <!--Thursday-->
                                        <c:set var="thursday" value="<%=THURSDAY%>" />
                                        <c:set var="thursday_start_time" value="<%=THURSDAY_START_DATE%>" />
                                        <c:set var="thursday_end_time" value="<%=THURSDAY_END_DATE%>" />
                                        <tr>
                                            <td>
                                                <input name="${thursday}" type="checkbox" value="thursday">
                                            </td>
                                            <td>Thursday</td>
                                            <td>
                                                <div class="checkbox">
                                                    <input class="form-control form-control-sm" placeholder="12:00" pattern="^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$" name="${thursday_start_time}" value="12:00" type="text">
                                                </div>
                                            </td>
                                            <td>
                                                <div class="checkbox">
                                                    <input class="form-control form-control-sm" placeholder="14:00" pattern="^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$" name="${thursday_end_time}" value="14:00" type="text">
                                                </div>
                                            </td>
                                        </tr>

                                        <!--Friday-->
                                        <c:set var="friday" value="<%=FRIDAY%>" />
                                        <c:set var="friday_start_time" value="<%=FRIDAY_START_DATE%>" />
                                        <c:set var="friday_end_time" value="<%=FRIDAY_END_DATE%>" />
                                        <tr>
                                            <td>
                                                <input name="${friday}" type="checkbox" value="friday">
                                            </td>
                                            <td>Friday</td>
                                            <td>
                                                <div class="checkbox">
                                                    <input class="form-control form-control-sm" placeholder="12:00" pattern="^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$" name="${friday_start_time}" value="12:00" type="text">
                                                </div>
                                            </td>
                                            <td>
                                                <div class="checkbox">
                                                    <input class="form-control form-control-sm" placeholder="14:00" pattern="^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$" name="${friday_end_time}" value="14:00" type="text">
                                                </div>
                                            </td>
                                        </tr>

                                        <!--Saturday-->
                                        <c:set var="saturday" value="<%=SATURDAY%>" />
                                        <c:set var="saturday_start_time" value="<%=SATURDAY_START_DATE%>" />
                                        <c:set var="saturday_end_time" value="<%=SATURDAY_END_DATE%>" />
                                        <tr>
                                            <td>
                                                <input name="${saturday}" type="checkbox" value="saturday">
                                            </td>
                                            <td>Saturday</td>
                                            <td>
                                                <div class="checkbox">
                                                    <input class="form-control form-control-sm" placeholder="12:00" pattern="^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$" name="${saturday_start_time}" value="12:00" type="text">
                                                </div>
                                            </td>
                                            <td>
                                                <div class="checkbox">
                                                    <input class="form-control form-control-sm" placeholder="14:00" pattern="^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$" name="${saturday_end_time}" value="14:00" type="text">
                                                </div>
                                            </td>
                                        </tr>

                                        <!--Sunday-->
                                        <c:set var="sunday" value="<%=SUNDAY%>" />
                                        <c:set var="sunday_start_time" value="<%=SUNDAY_START_DATE%>" />
                                        <c:set var="sunday_end_time" value="<%=SUNDAY_END_DATE%>" />
                                        <tr>
                                            <td>
                                                <input name="${sunday}" type="checkbox" value="saturday">
                                            </td>
                                            <td>Sunday</td>
                                            <td>
                                                <div class="checkbox">
                                                    <input class="form-control form-control-sm" placeholder="12:00" pattern="^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$" name="${sunday_start_time}" value="12:00" type="text">
                                                </div>
                                            </td>
                                            <td>
                                                <div class="checkbox">
                                                    <input class="form-control form-control-sm" placeholder="14:00" pattern="^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$" name="${sunday_end_time}" value="14:00" type="text">
                                                </div>
                                            </td>
                                        </tr>
                                        <tbody>
                                    </table>

                                </div>
                            </div>

                            <%-- Hidden course id --%>
                            <c:if test="${not empty course}">
                                <input type="hidden" name="${course_id}" value="${course.id}">
                            </c:if>

                            <%-- Hidden uri to redirect --%>
                            <c:if test="${not empty course}">
                                <input type="hidden" name="<%=FROM_URI%>" value="<%=request.getParameter(FROM_URI)%>">
                            </c:if>

                            <%-- Hidden current user id --%>
                            <input type="hidden"
                                   name="<%=WEB_PAGE_CURRENT_USER_ID%>"
                                   value="<%=session.getAttribute(SESSION_CURRENT_USER_ID) %>">

                            <%-- Submit button --%>
                            <hr/>
                            <input
                                class="btn btn-lg btn-success btn-block"
                                type="submit"
                                <c:if test="${empty course}">
                                    value="Submit"
                                </c:if>
                                <c:if test="${not empty course}">
                                    value="Save changes"
                                </c:if>>

                        </fieldset>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
<script src="https://momentjs.com/downloads/moment.js"></script>

</body>
</html>