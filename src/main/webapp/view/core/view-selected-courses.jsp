<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="static com.company.manager.servlet.core.get_courses.TeacherNotGradedCoursesServlet.*" %>
<%@ page import="com.company.manager.domain.user.UserRole" %>
<%@ page import="static com.company.manager.string_constans.CourseAttrAndParamNames.*" %>
<%@ page import="static com.company.manager.string_constans.UserAttrAndParamNames.SESSION_CURRENT_USER_INFO" %>
<%@ page import="com.company.manager.domain.course.Course" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="static com.company.manager.servlet.core.course_actions.SaveOrUpdateCourseServlet.COURSE_DATE_PATTERN" %>
<%@ page import="static com.company.manager.string_constans.ApplicationConstants.FROM_URI" %>
<%@ page import="static com.company.manager.servlet.core.students_actions.GetCourseStudentsServlet.GET_STUDENTS_TO_GRADE" %>
<%@ page import="static com.company.manager.servlet.core.students_actions.GetCourseStudentsServlet.GET_STUDENTS_TO_KICK" %>
<%@ page import="static com.company.manager.string_constans.UserAttrAndParamNames.WEB_PAGE_CURRENT_USER_ID" %>
<%@ page import="static com.company.manager.string_constans.UserAttrAndParamNames.*" %>

<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <title>My courses</title>
</head>

<%-- Prepare variables --%>
<c:set var="current_user_info" value="<%=session.getAttribute(SESSION_CURRENT_USER_INFO)%>" />
<c:set var="courses_state" value="<%=request.getAttribute(COURSES_STATE)%>" />
<c:set var="selected_courses" value="<%=request.getAttribute(COURSES)%>" />
<c:set var="not_graded_course_state" value="<%=NOT_GRADED_COURSES_STATE_ATTR_VALUE%>" />

<body>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-lg-12">
            <div class="card">

                <%-- Header --%>
                <div class="card-header">
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item">
                            <a href="${pageContext.request.contextPath}/main-menu?<%=WEB_PAGE_CURRENT_USER_ID%>=<%=session.getAttribute(SESSION_CURRENT_USER_ID)%>"
                               style="font-size: large">
                                Home
                            </a>
                        </li>
                        <li class="breadcrumb-item">
                            <a href="${pageContext.request.contextPath}/main-menu/select-courses?<%=WEB_PAGE_CURRENT_USER_ID%>=<%=session.getAttribute(SESSION_CURRENT_USER_ID)%>"
                               style="font-size: large">
                                Select my courses
                            </a>
                        </li>
                        <li class="breadcrumb-item active"
                            aria-current="page"
                            style="font-size: large">
                            My ${courses_state} courses
                        </li>
                    </ol>
                </div>

                <%-- Body --%>
                <div class="card-body">
                <c:choose>

                    <%--If courses list is empty--%>
                    <c:when test="${selected_courses.size() == 0}">
                        <hr/>
                        <center><h1 class="panel-title">No ${courses_state} courses yet</h1></center>
                        <hr/>
                    </c:when>


                    <c:otherwise>
                    <table class="table table-striped">

                        <%-- Table header --%>
                        <thead class="thead-dark">
                        <tr>
                            <th><center>Title</center></th>
                            <th><center>Start date</center></th>
                            <th><center>End date</center></th>

                            <c:if test="${courses_state != not_graded_course_state}">
                                <th><center>Timetable</center></th>
                                <th><center>URI</center></th>
                            </c:if>

                            <th><center>Description</center></th>

                            <%-- Special columns for students --%>
                            <c:if test="${current_user_info.userRole == UserRole.STUDENT}">
                                <th><center>Teacher</center></th>
                                <th><center>Leave course</center></th>
                            </c:if>

                            <%-- Speacial columns for teachers --%>
                            <c:if test="${current_user_info.userRole == UserRole.TEACHER && courses_state == not_graded_course_state}">
                                <th><center>Grade students</center></th>
                            </c:if>
                            <c:if test="${current_user_info.userRole == UserRole.TEACHER && courses_state != not_graded_course_state}">
                                <th><center>Subscribed students</center></th>
                                <th><center>Edit course</center></th>
                                <th><center>Delete course</center></th>
                            </c:if>
                        </tr>
                        </thead>

                        <%-- Table body --%>
                        <tbody>
                            <%-- Prepare to format dates --%>
                            <% DateTimeFormatter formatter = DateTimeFormatter.ofPattern(COURSE_DATE_PATTERN); %>
                            <c:forEach var="course" items="${selected_courses}">
                                <tr>
                                    <th>${course.courseInfo.name}</th>

                                    <%-- Formatting dates --%>
                                    <%Course course = (Course) pageContext.getAttribute("course");%>
                                    <td><%=course.getCourseInfo().getStartDate().format(formatter)%></td>
                                    <td><%=course.getCourseInfo().getEndDate().format(formatter)%></td>

                                    <c:if test="${courses_state != not_graded_course_state}">
                                    <td>
                                        <div class="dropdown">
                                            <button type="button"
                                                    class="btn btn-default dropdown-toggle"
                                                    data-toggle="dropdown">
                                                Timetable
                                            </button>
                                            <div class="dropdown-menu">
                                                <span class="dropdown-item-text"><c:if test="${empty course.courseInfo.timeTable}">No timetable yet</c:if>${course.courseInfo.timeTable}</span>
                                            </div>
                                        </div>
                                    </td>
                                    <td>
                                        <div class="dropdown">
                                            <button type="button"
                                                    class="btn btn-default dropdown-toggle"
                                                    data-toggle="dropdown">
                                                URI
                                            </button>
                                            <div class="dropdown-menu">
                                                <span class="dropdown-item-text">
                                                    <c:if test="${empty course.courseInfo.uri}">
                                                        <span class="dropdown-item-text">No uri yet</span>
                                                    </c:if>
                                                    <c:if test="${not empty course.courseInfo.uri}">
                                                        <a href="${course.courseInfo.uri}" target="_blank">${course.courseInfo.uri}</a>
                                                    </c:if>
                                                </span>
                                            </div>
                                        </div>
                                    </td>
                                    </c:if>
                                    <td>
                                        <div class="dropdown">
                                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                                                Description
                                            </button>
                                            <div class="dropdown-menu">
                                                <span class="dropdown-item-text">${course.courseInfo.description}</span>
                                            </div>
                                        </div>
                                    </td>

                                    <%-- Special for students --%>
                                    <c:if test="${current_user_info.userRole == UserRole.STUDENT}">
                                        <td>${course.teacher.userInfo.firstName} ${course.teacher.userInfo.lastName}</td>
                                        <td>
                                            <form action="${pageContext.request.contextPath}/main-menu/select-courses/leave-course" method="post">
                                                <%-- Hidden current user id --%>
                                                <input type="hidden"
                                                       name="<%=WEB_PAGE_CURRENT_USER_ID%>"
                                                       value="<%=session.getAttribute(SESSION_CURRENT_USER_ID) %>"/>
                                                <%-- Hidden current URI --%>
                                                <input type="hidden" name="<%=FROM_URI%>"
                                                       value="<%=(String)request.getAttribute(FROM_URI)%>"/>
                                                <button class="btn btn-warning btn-block"
                                                        type="submit"
                                                        name="<%=COURSE_ID%>"
                                                        value="${course.id}">
                                                    Leave
                                                </button>
                                            </form>
                                        </td>
                                    </c:if>

                                    <%-- Special actions for teachers --%>
                                    <c:if test="${courses_state == not_graded_course_state}">
                                        <td>
                                            <form action="${pageContext.request.contextPath}/main-menu/select-courses/students" method="get">
                                                <%-- Hidden current user id --%>
                                                <input type="hidden"
                                                       name="<%=WEB_PAGE_CURRENT_USER_ID%>"
                                                       value="<%=session.getAttribute(SESSION_CURRENT_USER_ID) %>"/>
                                                <%-- Hidden current URI --%>
                                                <input type="hidden"
                                                       name="<%=FROM_URI%>"
                                                       value="<%=(String)request.getAttribute(FROM_URI)%>"/>
                                                <%-- Hidden course students view target --%>
                                                <input type="hidden"
                                                       name="<%=COURSE_STUDENTS_VIEW_TARGET%>"
                                                       value="<%=GET_STUDENTS_TO_GRADE%>"/>
                                                <button class="btn btn-primary btn-block"
                                                        type="submit"
                                                        name="<%=COURSE_ID%>"
                                                        value="${course.id}">
                                                    Grade
                                                </button>
                                            </form>
                                        </td>
                                    </c:if>
                                    <c:if test="${current_user_info.userRole == UserRole.TEACHER && courses_state != not_graded_course_state}">
                                        <td>
                                            <form action="${pageContext.request.contextPath}/main-menu/select-courses/students" method="get">
                                                <%-- Hidden current user id --%>
                                                <input type="hidden"
                                                       name="<%=WEB_PAGE_CURRENT_USER_ID%>"
                                                       value="<%=session.getAttribute(SESSION_CURRENT_USER_ID) %>"/>
                                                <%-- Hidden current URI --%>
                                                <input type="hidden"
                                                       name="<%=FROM_URI%>"
                                                       value="<%=(String)request.getAttribute(FROM_URI)%>"/>
                                                <%-- Hidden course students view target --%>
                                                <input type="hidden"
                                                       name="<%=COURSE_STUDENTS_VIEW_TARGET%>"
                                                       value="<%=GET_STUDENTS_TO_KICK%>"/>
                                                <button class="btn btn-primary btn-block"
                                                        type="submit"
                                                        name="<%=COURSE_ID%>"
                                                        value="${course.id}">
                                                    Students
                                                </button>
                                            </form>
                                        </td>
                                        <td>
                                            <form action="${pageContext.request.contextPath}/main-menu/select-courses/edit-course" method="get">
                                                <%-- Hidden current user id --%>
                                                <input type="hidden"
                                                       name="<%=WEB_PAGE_CURRENT_USER_ID%>"
                                                       value="<%=session.getAttribute(SESSION_CURRENT_USER_ID) %>"/>
                                                <%-- Hidden current URI --%>
                                                <input type="hidden"
                                                       name="<%=FROM_URI%>"
                                                       value="<%=(String)request.getAttribute(FROM_URI)%>"/>
                                                <button class="btn btn-success btn-block"
                                                        type="submit"
                                                        name="<%=COURSE_ID%>"
                                                        value="${course.id}">
                                                    Edit
                                                </button>
                                            </form>
                                        </td>
                                        <td>
                                            <form action="${pageContext.request.contextPath}/main-menu/select-courses/delete-course" method="post">
                                                <%-- Hidden current user id --%>
                                                <input type="hidden"
                                                       name="<%=WEB_PAGE_CURRENT_USER_ID%>"
                                                       value="<%=session.getAttribute(SESSION_CURRENT_USER_ID) %>"/>
                                                <%-- Hidden current URI --%>
                                                <input type="hidden"
                                                       name="<%=FROM_URI%>"
                                                       value="<%=(String)request.getAttribute(FROM_URI)%>"/>
                                                <button class="btn btn-danger btn-block"
                                                        type="submit"
                                                        name="<%=COURSE_ID%>"
                                                        value="${course.id}">
                                                    Delete
                                                </button>
                                            </form>
                                        </td>
                                    </c:if>

                                </tr>
                            </c:forEach>
                        </tbody>

                    </table>
                    </c:otherwise>
                </c:choose>

                <hr/>
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
