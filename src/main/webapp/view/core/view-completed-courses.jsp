<%@ page import="static web.servlet.core.get_courses.StudentCompletedCoursesServlet.COURSES_RESULTS_ATTR" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <title>My courses</title>
</head>

<%-- Prepare variables --%>
<c:set var="courses_results" value="<%=request.getAttribute(COURSES_RESULTS_ATTR)%>" />

<body>
<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-lg-12">
            <div class="card">

                <%-- Header --%>
                <div class="card-header">
                    <h3 class="panel-title">My completed courses</h3>
                </div>

                <%-- Body --%>
                <div class="card-body">
                    <c:choose>

                        <%--If courses list is empty--%>
                        <c:when test="${courses_results.size() == 0}">
                            <hr/>
                            <center><h1 class="panel-title">No completed courses yet</h1></center>
                            <hr/>
                        </c:when>

                        <c:otherwise>
                            <table class="table">

                                <thead class="thead-dark">
                                <tr>
                                    <th scope="col">Title</th>
                                    <th scope="col">Start date</th>
                                    <th scope="col">End date</th>
                                    <th scope="col">Description</th>
                                    <th scope="col">Teacher</th>
                                    <th scope="col">Result</th>
                                </tr>
                                </thead>

                                <tbody>
                                <c:forEach var="course_with_result" items="${courses_results}">
                                    <tr>
                                        <th>${course_with_result.course.courseInfo.name}</th>
                                        <td>${course_with_result.course.courseInfo.startDate}</td>
                                        <td>${course_with_result.course.courseInfo.endDate}</td>
                                        <td>
                                            <div class="dropdown">
                                                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                                                    Description
                                                </button>
                                                <div class="dropdown-menu">
                                                    <span class="dropdown-item-text">${course_with_result.course.courseInfo.description}</span>
                                                </div>
                                            </div>
                                        </td>
                                        <td>${course_with_result.course.teacher.userInfo.firstName} ${course_with_result.course.teacher.userInfo.lastName}</td>
                                        <td>${course_with_result.result.toString()}<td>
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
