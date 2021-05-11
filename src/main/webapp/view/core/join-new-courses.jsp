<%@ page import="static web.servlet.core.course_actions.GetAvailableCoursesServlet.AVAILABLE_COURSES_ATTR" %>
<%@ page import="static web.servlet.core.course_actions.JoinNewCoursesServlet.COURSES_TO_JOIN_ID_PARAM" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <title>Join courses</title>
</head>

<%-- Prepare variables --%>
<c:set var="courses_to_join" value="<%=COURSES_TO_JOIN_ID_PARAM%>" />
<c:set var="available_courses" value="<%=request.getAttribute(AVAILABLE_COURSES_ATTR)%>" />

<body>
<div class="container">
    <div class="row">
        <div class="col-lg-12">
            <div class="card">

                <%-- Header --%>
                <div class="card-header">
                    <h3>Choose courses you want to join</h3>
                </div>

                <%-- Body --%>
                <div class="card-body">
                    <c:choose>

                        <%-- If available courses list is empty --%>
                        <c:when test="${available_courses.size() == 0}">
                            <hr/>
                            <center><h1>No courses available right now</h1></center>
                            <hr/>
                        </c:when>

                        <c:otherwise>
                            <form action="${pageContext.request.contextPath}/join-new-courses" method="post">
                                <table class="table">

                                    <thead class="thead-dark">
                                    <tr>
                                        <th scope="col">Add</th>
                                        <th scope="col">Title</th>
                                        <th scope="col">Start date</th>
                                        <th scope="col">End date</th>
                                        <th scope="col">Teacher</th>
                                        <th scope="col">Description</th>
                                    </tr>
                                    </thead>

                                    <tbody>
                                        <c:forEach var="course" items="${available_courses}">
                                            <tr>
                                                <td>
                                                    <div class="checkbox">
                                                        <input name="${courses_to_join}" type="checkbox" value="${course.id}">
                                                    </div>
                                                </td>
                                                <th>${course.courseInfo.name}</th>
                                                <td>${course.courseInfo.startDate}</td>
                                                <td>${course.courseInfo.endDate}</td>
                                                <td>${course.teacher.userInfo.firstName} ${course.teacher.userInfo.lastName}</td>
                                                <td>${course.courseInfo.description}</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>

                                <%-- Submit button --%>
                                <hr/>
                                <input class="btn btn-lg btn-primary btn-block" type="submit" value="Submit">
                                <hr/>

                            </form>
                            <hr/>
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