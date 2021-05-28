<%@ page import="static com.company.manager.string_constans.CourseAttrAndParamNames.COURSES" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="static com.company.manager.servlet.core.course_actions.SaveOrUpdateCourseServlet.COURSE_DATE_PATTERN" %>
<%@ page import="com.company.manager.domain.course.Course" %>
<%@ page import="static com.company.manager.string_constans.CourseAttrAndParamNames.COURSES_ID" %>
<%@ page import="static com.company.manager.string_constans.UserAttrAndParamNames.WEB_PAGE_CURRENT_USER_ID" %>
<%@ page import="static com.company.manager.string_constans.UserAttrAndParamNames.SESSION_CURRENT_USER_ID" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <title>Join courses</title>
</head>

<%-- Prepare variables --%>
<c:set var="courses_to_join" value="<%=COURSES_ID%>" />
<c:set var="available_courses" value="<%=request.getAttribute(COURSES)%>" />

<body>
<div class="container">
    <div class="row">
        <div class="col-lg-12">
            <div class="card">

                <%-- Header --%>
                <div class="card-header">
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item">
                            <a href="${pageContext.request.contextPath}/main-menu?<%=WEB_PAGE_CURRENT_USER_ID%>=<%=session.getAttribute(SESSION_CURRENT_USER_ID)%>" style="font-size: large">Home</a>
                        </li>
                        <li class="breadcrumb-item active" aria-current="page" style="font-size: large">Join new courses</li>
                    </ol>
                </div>

                <%-- Body --%>
                <div class="card-body">
                    <c:choose>

                        <%-- If available courses list is empty --%>
                        <c:when test="${available_courses.size() == 0}">
                            <hr/>
                            <center><h1>No courses available right now</h1></center>
                            <hr/>
                            <%-- Back to main menu button --%>
                            <hr/>
                            <form action="${pageContext.request.contextPath}/main-menu" method="get">
                                <input class="btn btn-lg btn-outline-success btn-block" type="submit" value="Back to main menu">
                            </form>
                        </c:when>

                        <c:otherwise>
                            <form action="${pageContext.request.contextPath}/main-menu/join-new-courses/join-selected-courses" method="post">
                                <table class="table table-striped">

                                    <thead class="thead-dark">
                                    <tr>
                                        <th><center>Add</center></th>
                                        <th><center>Title</center></th>
                                        <th><center>Start date</center></th>
                                        <th><center>End date</center></th>
                                        <th><center>Timetable</center></th>
                                        <th><center>Teacher</center></th>
                                        <th><center>Description</center></th>
                                    </tr>
                                    </thead>

                                    <tbody>
                                        <%-- Prepare to format dates --%>
                                        <% DateTimeFormatter formatter = DateTimeFormatter.ofPattern(COURSE_DATE_PATTERN); %>
                                        <c:forEach var="course" items="${available_courses}">
                                            <tr>
                                                <td>
                                                    <div class="checkbox">
                                                        <input name="${courses_to_join}" type="checkbox" value="${course.id}">
                                                    </div>
                                                </td>
                                                <th>${course.courseInfo.name}</th>

                                                <%-- Formatting dates --%>
                                                <%Course course = (Course) pageContext.getAttribute("course");%>
                                                <td><%=course.getCourseInfo().getStartDate().format(formatter)%></td>
                                                <td><%=course.getCourseInfo().getEndDate().format(formatter)%></td>

                                                <td>
                                                    <div class="dropdown">
                                                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                                                            Timetable
                                                        </button>
                                                        <div class="dropdown-menu">
                                                            <c:if test="${not empty course.courseInfo.timeTable}">
                                                                <span class="dropdown-item-text">${course.courseInfo.timeTable}</span>
                                                            </c:if>
                                                            <c:if test="${empty course.courseInfo.timeTable}">
                                                                <span class="dropdown-item-text">No timetable yet</span>
                                                            </c:if>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td>${course.teacher.userInfo.firstName} ${course.teacher.userInfo.lastName}</td>
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
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>

                                <%-- Submit button --%>
                                <hr/>
                                <input class="btn btn-lg btn-primary btn-block" type="submit" value="Submit">
                                <hr/>

                                <%-- Hidden current user id --%>
                                <input type="hidden"
                                       name="<%=WEB_PAGE_CURRENT_USER_ID%>"
                                       value="<%=session.getAttribute(SESSION_CURRENT_USER_ID) %>"/>

                            </form>
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
