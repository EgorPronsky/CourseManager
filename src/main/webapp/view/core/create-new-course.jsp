<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="static web.servlet.core.course_actions.SubmitNewCourseServlet.*" %>
<%@ page import="static util.CourseInputHandler.MONDAY_PARAM" %>
<%@ page import="static util.CourseInputHandler.MONDAY_START_TIME_PARAM" %>
<%@ page import="static util.CourseInputHandler.*" %>
<html>

<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <title>Create a new course</title>
</head>

<%-- Prepare variables --%>
<c:set var="course_title" value="<%=COURSE_TITLE_PARAM%>" />
<c:set var="course_description" value="<%=COURSE_DESCRIPTION_PARAM%>" />
<c:set var="course_start_date" value="<%=COURSE_START_DATE_PARAM%>" />
<c:set var="course_end_date" value="<%=COURSE_END_DATE_PARAM%>" />
<c:set var="course_uri" value="<%=COURSE_URI_PARAM%>" />

<body>
<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-lg-10">
            <div class="card">

                <div class="card-header">
                    <h3 class="panel-title">Create a new course</h3>
                </div>

                <div class="card-body">
                    <form accept-charset="UTF-8" role="form" action="${pageContext.request.contextPath}/submit-new-course" method="post">
                        <fieldset>

                            <div class="row">

                                <!--Left column-->
                                <div class="col">
                                    <!--Title-->
                                    <div class="form-group">
                                        <label for="title">Title</label>
                                        <input class="form-control" id="title" name="${course_title}" type="text" required>
                                    </div>

                                    <div class="form-row">
                                        <!--Start date-->
                                        <div class="form-group col">
                                            <label for="start_date">Start date</label>
                                            <input class="form-control" placeholder="dd-MM-yyyy" id="start_date" name="${course_start_date}" type="text" required>
                                        </div>

                                        <!--End date-->
                                        <div class="form-group col">
                                            <label for="end_date">End date</label>
                                            <input class="form-control" placeholder="dd-MM-yyyy" id="end_date" name="${course_end_date}" type="text" required>
                                        </div>
                                    </div>

                                    <!--URI for connection-->
                                    <div class="form-group">
                                        <label for="uri">URI for connection</label>
                                        <input class="form-control" id="uri" name="${course_uri}" type="text" required>
                                    </div>

                                    <!--Description-->
                                    <div class="form-group">
                                        <label for="description">Description</label>
                                        <textarea class="form-control" id="description" name="${course_description}" type="text" rows="6" required></textarea>
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
                                        <c:set var="monday" value="<%=MONDAY_PARAM%>" />
                                        <c:set var="monday_start_time" value="<%=MONDAY_START_TIME_PARAM%>" />
                                        <c:set var="monday_end_time" value="<%=MONDAY_END_TIME_PARAM%>" />
                                        <tr>
                                            <td>
                                                <input name="${monday}" type="checkbox" value="monday">
                                            </td>
                                            <td>Monday</td>
                                            <td>
                                                <div class="checkbox">
                                                    <input class="form-control form-control-sm" placeholder="12:00" name="${monday_start_time}" type="text">
                                                </div>
                                            </td>
                                            <td>
                                                <div class="checkbox">
                                                    <input class="form-control form-control-sm" placeholder="14:00" name="${monday_end_time}" type="text">
                                                </div>
                                            </td>
                                        </tr>

                                        <!--Tuesday-->
                                        <c:set var="tuesday" value="<%=TUESDAY_PARAM%>" />
                                        <c:set var="tuesday_start_time" value="<%=TUESDAY_START_TIME_PARAM%>" />
                                        <c:set var="tuesday_end_time" value="<%=TUESDAY_END_TIME_PARAM%>" />
                                        <tr>
                                            <td>
                                                <input name="${tuesday}" type="checkbox" value="tuesday">
                                            </td>
                                            <td>Tuesday</td>
                                            <td>
                                                <div class="checkbox">
                                                    <input class="form-control form-control-sm" placeholder="12:00" name="${tuesday_start_time}" type="text">
                                                </div>
                                            </td>
                                            <td>
                                                <div class="checkbox">
                                                    <input class="form-control form-control-sm" placeholder="14:00" name="${tuesday_end_time}" type="text">
                                                </div>
                                            </td>
                                        </tr>

                                        <!--Wednesday-->
                                        <c:set var="wednesday" value="<%=WEDNESDAY_PARAM%>" />
                                        <c:set var="wednesday_start_time" value="<%=WEDNESDAY_START_TIME_PARAM%>" />
                                        <c:set var="wednesday_end_time" value="<%=WEDNESDAY_END_TIME_PARAM%>" />
                                        <tr>
                                            <td>
                                                <input name="${wednesday}" type="checkbox" value="wednesday">
                                            </td>
                                            <td>Wednesday</td>
                                            <td>
                                                <div class="checkbox">
                                                    <input class="form-control form-control-sm" placeholder="12:00" name="${wednesday_start_time}" type="text">
                                                </div>
                                            </td>
                                            <td>
                                                <div class="checkbox">
                                                    <input class="form-control form-control-sm" placeholder="14:00" name="${wednesday_end_time}" type="text">
                                                </div>
                                            </td>
                                        </tr>

                                        <!--Thursday-->
                                        <c:set var="thursday" value="<%=THURSDAY_PARAM%>" />
                                        <c:set var="thursday_start_time" value="<%=THURSDAY_START_DATE_PARAM%>" />
                                        <c:set var="thursday_end_time" value="<%=THURSDAY_END_DATE_PARAM%>" />
                                        <tr>
                                            <td>
                                                <input name="${thursday}" type="checkbox" value="thursday">
                                            </td>
                                            <td>Thursday</td>
                                            <td>
                                                <div class="checkbox">
                                                    <input class="form-control form-control-sm" placeholder="12:00" name="${thursday_start_time}" type="text">
                                                </div>
                                            </td>
                                            <td>
                                                <div class="checkbox">
                                                    <input class="form-control form-control-sm" placeholder="14:00" name="${thursday_end_time}" type="text">
                                                </div>
                                            </td>
                                        </tr>

                                        <!--Friday-->
                                        <c:set var="friday" value="<%=FRIDAY_PARAM%>" />
                                        <c:set var="friday_start_time" value="<%=FRIDAY_START_DATE_PARAM%>" />
                                        <c:set var="friday_end_time" value="<%=FRIDAY_END_DATE_PARAM%>" />
                                        <tr>
                                            <td>
                                                <input name="${friday}" type="checkbox" value="friday">
                                            </td>
                                            <td>Friday</td>
                                            <td>
                                                <div class="checkbox">
                                                    <input class="form-control form-control-sm" placeholder="12:00" name="${friday_start_time}" type="text">
                                                </div>
                                            </td>
                                            <td>
                                                <div class="checkbox">
                                                    <input class="form-control form-control-sm" placeholder="14:00" name="${friday_end_time}" type="text">
                                                </div>
                                            </td>
                                        </tr>

                                        <!--Saturday-->
                                        <c:set var="saturday" value="<%=SATURDAY_PARAM%>" />
                                        <c:set var="saturday_start_time" value="<%=SATURDAY_START_DATE_PARAM%>" />
                                        <c:set var="saturday_end_time" value="<%=SATURDAY_END_DATE_PARAM%>" />
                                        <tr>
                                            <td>
                                                <input name="${saturday}" type="checkbox" value="saturday">
                                            </td>
                                            <td>Saturday</td>
                                            <td>
                                                <div class="checkbox">
                                                    <input class="form-control form-control-sm" placeholder="12:00" name="${saturday_start_time}" type="text">
                                                </div>
                                            </td>
                                            <td>
                                                <div class="checkbox">
                                                    <input class="form-control form-control-sm" placeholder="14:00" name="${saturday_end_time}" type="text">
                                                </div>
                                            </td>
                                        </tr>

                                        <!--Sunday-->
                                        <c:set var="sunday" value="<%=SUNDAY_PARAM%>" />
                                        <c:set var="sunday_start_time" value="<%=SUNDAY_START_DATE_PARAM%>" />
                                        <c:set var="sunday_end_time" value="<%=SUNDAY_END_DATE_PARAM%>" />
                                        <tr>
                                            <td>
                                                <input name="${sunday}" type="checkbox" value="saturday">
                                            </td>
                                            <td>Sunday</td>
                                            <td>
                                                <div class="checkbox">
                                                    <input class="form-control form-control-sm" placeholder="12:00" name="${sunday_start_time}" type="text">
                                                </div>
                                            </td>
                                            <td>
                                                <div class="checkbox">
                                                    <input class="form-control form-control-sm" placeholder="14:00" name="${sunday_end_time}" type="text">
                                                </div>
                                            </td>
                                        </tr>
                                        <tbody>
                                    </table>

                                </div>
                            </div>

                            <%-- Submit button --%>
                            <hr/>
                            <input class="btn btn-lg btn-success btn-block" type="submit" value="Submit">

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

</body>
</html>