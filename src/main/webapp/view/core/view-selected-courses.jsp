<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="static com.company.manager.servlet.core.get_courses.TeacherNotGradedCoursesServlet.*" %>
<%@ page import="com.company.manager.domain.user.UserRole" %>
<%@ page import="static com.company.manager.constans.CourseAttrAndParamNames.*" %>
<%@ page import="static com.company.manager.constans.UserAttrAndParamNames.CURRENT_USER_INFO_SESSION" %>
<html>

<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <title>My courses</title>
</head>

<%-- Prepare variables --%>
<c:set var="current_user_info" value="<%=session.getAttribute(CURRENT_USER_INFO_SESSION)%>" />
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
                    <h3 class="panel-title">My ${courses_state} courses</h3>
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
                        <table class="table">

                            <thead class="thead-dark">
                                <tr>
                                    <th scope="col">Title</th>
                                    <th scope="col">Start date</th>
                                    <th scope="col">End date</th>
                                    <th scope="col">Timetable</th>
                                    <th scope="col">URI</th>
                                    <th scope="col">Description</th>

                                    <%-- Special columns for students --%>
                                    <c:if test="${current_user_info.userRole == UserRole.STUDENT}">
                                        <th scope="col">Teacher</th>
                                        <th scope="col">Leave course</th>
                                    </c:if>

                                    <%-- Speacial columns for teachers --%>
                                    <c:if test="${current_user_info.userRole == UserRole.TEACHER && courses_state == not_graded_course_state}">
                                        <th scope="col">Grade students</th>
                                    </c:if>
                                    <c:if test="${current_user_info.userRole == UserRole.TEACHER && courses_state != not_graded_course_state}">
                                        <th scope="col">Edit course</th>
                                        <th scope="col">Delete course</th>
                                    </c:if>
                                </tr>
                            </thead>

                            <tbody>
                                <c:forEach var="course" items="${selected_courses}">
                                    <tr>
                                        <th>${course.courseInfo.name}</th>
                                        <td>${course.courseInfo.startDate}</td>
                                        <td>${course.courseInfo.endDate}</td>
                                        <td>
                                            <div class="dropdown">
                                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                                                    Timetable
                                                </button>
                                                <div class="dropdown-menu">
                                                    <span class="dropdown-item-text">${course.courseInfo.timeTable}</span>
                                                </div>
                                            </div>
                                        </td>
                                        <td>
                                            <div class="dropdown">
                                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                                                    URI
                                                </button>
                                                <div class="dropdown-menu">
                                                    <span class="dropdown-item-text">${course.courseInfo.uri}</span>
                                                </div>
                                            </div>
                                        </td>
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

                                        <c:if test="${current_user_info.userRole == UserRole.STUDENT}">
                                            <td>${course.teacher.userInfo.firstName} ${course.teacher.userInfo.lastName}</td>
                                        </c:if>

                                        <%-- Special actions for students --%>
                                        <c:if test="${current_user_info.userRole == UserRole.STUDENT}">
                                            <td>
                                                <form action="${pageContext.request.contextPath}/main-menu/select-courses/leave-course" method="post">
                                                    <button class="btn btn-primary btn-block" type="submit" name="<%=COURSE_ID%>" value="${course.id}">Leave</button>
                                                </form>
                                            </td>
                                        </c:if>

                                        <%-- Special actions for teachers --%>
                                        <c:if test="${courses_state == not_graded_course_state}">
                                            <td>
                                                <form action="${pageContext.request.contextPath}/main-menu/select-courses/my-not-graded-courses/students" method="get">
                                                    <button class="btn btn-primary btn-block" type="submit" name="<%=COURSE_ID%>" value="${course.id}">Grade</button>
                                                </form>
                                            </td>
                                        </c:if>
                                        <c:if test="${current_user_info.userRole == UserRole.TEACHER && courses_state != not_graded_course_state}">
                                            <td>
                                                <form action="${pageContext.request.contextPath}/main-menu/select-courses/edit-course" method="get">
                                                    <button class="btn btn-primary btn-block" type="submit" name="<%=COURSE_ID%>" value="${course.id}">Edit</button>
                                                </form>
                                            </td>
                                            <td>
                                                <form action="${pageContext.request.contextPath}/main-menu/select-courses/delete-course" method="post">
                                                    <button class="btn btn-primary btn-block" type="submit" name="<%=COURSE_ID%>" value="${course.id}">Delete</button>
                                                </form>
                                            </td>
                                        </c:if>

                                    </tr>
                                </c:forEach>
                            </tbody>

                        </table>
                    </c:otherwise>
                </c:choose>
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
