<%@ page import="static com.company.manager.string_constans.CourseAttrAndParamNames.COURSES_RESULTS" %>
<%@ page import="com.company.manager.domain.archive.CourseResult" %>
<%@ page import="com.company.manager.domain.archive.StudentCourseResult" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="static com.company.manager.servlet.core.course_actions.SaveOrUpdateCourseServlet.COURSE_DATE_PATTERN" %>
<%@ page import="static com.company.manager.string_constans.UserAttrAndParamNames.WEB_PAGE_CURRENT_USER_ID" %>
<%@ page import="static com.company.manager.string_constans.UserAttrAndParamNames.SESSION_CURRENT_USER_ID" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8"%>

<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <title>My courses</title>
</head>

<%-- Prepare variables --%>
<c:set var="courses_results" value="<%=request.getAttribute(COURSES_RESULTS)%>" />

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
                               style="font-size: large">Home</a>
                        </li>
                        <li class="breadcrumb-item">
                            <a href="${pageContext.request.contextPath}/main-menu/select-courses?<%=WEB_PAGE_CURRENT_USER_ID%>=<%=session.getAttribute(SESSION_CURRENT_USER_ID)%>" style="font-size: large">Select my courses</a>
                        </li>
                        <li class="breadcrumb-item active" aria-current="page" style="font-size: large">My completed courses</li>
                    </ol>
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
                            <table class="table table-striped">

                                <thead class="thead-dark">
                                    <tr>
                                        <th><center>Title</center></th>
                                        <th><center>Start date</center></th>
                                        <th><center>End date</center></th>
                                        <th><center>Description</center></th>
                                        <th><center>Teacher</center></th>
                                        <th><center>Result</center></th>
                                    </tr>
                                </thead>

                                <tbody>
                                    <%-- Prepare to format dates --%>
                                    <% DateTimeFormatter formatter = DateTimeFormatter.ofPattern(COURSE_DATE_PATTERN); %>
                                    <c:forEach var="course_with_result" items="${courses_results}">
                                        <tr>
                                            <th>${course_with_result.course.courseInfo.name}</th>

                                            <%-- Formatting dates --%>
                                            <%StudentCourseResult scr = (StudentCourseResult) pageContext.getAttribute("course_with_result");%>
                                            <td><%=scr.getCourse().getCourseInfo().getStartDate().format(formatter)%></td>
                                            <td><%=scr.getCourse().getCourseInfo().getEndDate().format(formatter)%></td>

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
                                            <c:if test="${not empty course_with_result.result}">
                                                <c:if test="${course_with_result.result == CourseResult.EXCELLENT}">
                                                    <td>
                                                        <button type="button" class="btn btn-success btn-block">${CourseResult.EXCELLENT.toString()}</button>
                                                    </td>
                                                </c:if>
                                                <c:if test="${course_with_result.result == CourseResult.OK}">
                                                    <td>
                                                        <button type="button" class="btn btn-info btn-block">${CourseResult.OK.toString()}</button>
                                                    </td>
                                                </c:if>
                                                <c:if test="${course_with_result.result == CourseResult.BAD}">
                                                    <td>
                                                        <button type="button" class="btn btn-danger btn-block">${CourseResult.BAD.toString()}</button>
                                                    </td>
                                                </c:if>
                                            </c:if>
                                            <c:if test="${empty course_with_result.result}">
                                                <td>
                                                    <button type="button" class="btn btn-secondary btn-block">Not graded yet</button>
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
