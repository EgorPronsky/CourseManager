<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.company.manager.domain.archive.CourseResult" %>
<%@ page import="static com.company.manager.string_constans.CourseAttrAndParamNames.COURSE_ID" %>
<%@ page import="static com.company.manager.string_constans.CourseAttrAndParamNames.COURSE" %>
<%@ page import="static com.company.manager.string_constans.CourseAttrAndParamNames.*" %>
<%@ page import="com.company.manager.domain.course.Course" %>
<%@ page import="com.company.manager.domain.archive.StudentCourseResult" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Collections" %>
<%@ page import="static com.company.manager.string_constans.ApplicationConstants.FROM_URI" %>
<%@ page import="static com.company.manager.servlet.core.students_actions.GetCourseStudentsServlet.GET_STUDENTS_TO_KICK" %>
<%@ page import="static com.company.manager.servlet.core.students_actions.GetCourseStudentsServlet.GET_STUDENTS_TO_GRADE" %>
<%@ page import="static com.company.manager.string_constans.UserAttrAndParamNames.WEB_PAGE_CURRENT_USER_ID" %>
<%@ page import="static com.company.manager.string_constans.UserAttrAndParamNames.SESSION_CURRENT_USER_ID" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <title>Course students</title>
</head>

<%-- Prepare variables --%>
<c:set var="course" value="<%=request.getAttribute(COURSE)%>" />
<%
    String target = request.getParameter(COURSE_STUDENTS_VIEW_TARGET);
    if (target == null) target = (String)request.getAttribute(COURSE_STUDENTS_VIEW_TARGET);
%>
<c:set var="course_students_view_target" value="<%=target%>" />

<c:set var="course_id" value="<%=COURSE_ID%>" />
<c:set var="target_get_students_to_see" value="<%=GET_STUDENTS_TO_KICK%>" />
<c:set var="target_get_students_to_grade" value="<%=GET_STUDENTS_TO_GRADE%>" />

<%-- Sorting students --%>
<%
    List<StudentCourseResult> sortedStudentsInSCR = new ArrayList<>(((Course)pageContext.getAttribute("course")).getStudentResults());
    Collections.sort(sortedStudentsInSCR, new Comparator<StudentCourseResult>() {
        @Override
        public int compare(StudentCourseResult  o1, StudentCourseResult o2) {
            return o1.getStudent().getUserInfo().getLastName()
                    .compareTo(o2.getStudent().getUserInfo().getLastName());
        }
    });
%>
<c:set var="sorted_students_in_scr" value="<%=sortedStudentsInSCR%>" />

<body>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-lg-8">
            <div class="card">

                <%-- Header --%>
                <div class="card-header">
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item">
                            <a href="${pageContext.request.contextPath}/main-menu?<%=WEB_PAGE_CURRENT_USER_ID%>=<%=session.getAttribute(SESSION_CURRENT_USER_ID)%>" style="font-size: large">Home</a>
                        </li>
                        <li class="breadcrumb-item">
                            <a href="${pageContext.request.contextPath}/main-menu/select-courses?<%=WEB_PAGE_CURRENT_USER_ID%>=<%=session.getAttribute(SESSION_CURRENT_USER_ID)%>" style="font-size: large">Select courses</a>
                        </li>
                        <li class="breadcrumb-item">
                            <a href="<%=request.getParameter(FROM_URI)%>?<%=WEB_PAGE_CURRENT_USER_ID%>=<%=session.getAttribute(SESSION_CURRENT_USER_ID)%>" style="font-size: large">Back to courses</a>
                        </li>
                        <li class="breadcrumb-item active" aria-current="page" style="font-size: large">${course.courseInfo.name} students</li>
                    </ol>
                </div>

                <%-- Body --%>
                <div class="card-body">

                    <c:choose>
                    <%--If courses list is empty--%>
                    <c:when test="${course.studentResults.size() == 0}">
                        <hr/>
                        <center><h3 class="panel-title">No students yet</h3></center>
                        <hr/>
                    </c:when>

                    <c:otherwise>
                            <form
                                <c:if test="${course_students_view_target == target_get_students_to_see}">
                                    action="${pageContext.request.contextPath}/main-menu/select-courses/leave-course"
                                </c:if>
                                <c:if test="${course_students_view_target == target_get_students_to_grade}">
                                    action="${pageContext.request.contextPath}/main-menu/select-courses/my-not-graded-courses/students/grade-students"
                                </c:if>
                                method="post">

                                <%-- Hidden course id --%>
                                <input type="hidden" name="${course_id}" value="${course.id}"/>

                                <%-- Hidden URI to redirect --%>
                                <input type="hidden" name="<%=FROM_URI%>" value="<%=request.getParameter(FROM_URI)%>"/>

                                <table class="table table-striped">
                                    <thead class="thead-dark">
                                    <tr>
                                        <th scope="col"><center>First name</center></th>
                                        <th scope="col"><center>Last name</center></th>
                                        <c:if test="${course_students_view_target == target_get_students_to_see}">
                                            <th scope="col"><center>Kick student</center></th>
                                        </c:if>
                                        <c:if test="${course_students_view_target == target_get_students_to_grade}">
                                            <th scope="col"><center>Result</center></th>
                                        </c:if>
                                    </tr>
                                    </thead>

                                    <tbody>
                                        <c:forEach var="scr" items="${sorted_students_in_scr}">
                                            <tr>
                                                <td><center>${scr.student.userInfo.firstName}</center></td>
                                                <td><center>${scr.student.userInfo.lastName}</center></td>

                                                <c:if test="${course_students_view_target == target_get_students_to_see}">
                                                    <td><center>
                                                        <button class="btn btn-primary btn-danger"
                                                                type="submit"
                                                                name="<%=COURSE_STUDENT_ID%>"
                                                                value="${scr.student.id}">Kick out</button>
                                                        </center>
                                                    </td>
                                                </c:if>

                                                <c:if test="${course_students_view_target == target_get_students_to_grade}">
                                                    <td>
                                                        <div class="form-check form-check-inline">
                                                            <input class="form-check-input"
                                                                   type="radio"
                                                                   name="${scr.student.id}"
                                                                   id="result_bad${scr.student.id}"
                                                                   value="${CourseResult.BAD}">
                                                            <label class="form-check-label"
                                                                   for="result_bad${scr.student.id}">${CourseResult.BAD.toString()}</label>
                                                        </div>
                                                        <div class="form-check form-check-inline">
                                                            <input class="form-check-input"
                                                                   type="radio"
                                                                   name="${scr.student.id}"
                                                                   id="result_ok${scr.student.id}"
                                                                   value="${CourseResult.OK}" checked>
                                                            <label class="form-check-label"
                                                                   for="result_ok${scr.student.id}">${CourseResult.OK.toString()}</label>
                                                        </div>
                                                        <div class="form-check form-check-inline">
                                                            <input class="form-check-input"
                                                                   type="radio"
                                                                   name="${scr.student.id}"
                                                                   id="result_perfect${scr.student.id}"
                                                                   value="${CourseResult.EXCELLENT}" >
                                                            <label class="form-check-label"
                                                                   for="result_perfect${scr.student.id}">${CourseResult.EXCELLENT.toString()}</label>
                                                        </div>
                                                    </td>
                                                </c:if>
                                            </tr>
                                        </c:forEach>

                                    </tbody>
                                </table>

                                <hr/>
                                <c:if test="${course_students_view_target == target_get_students_to_grade}">
                                    <button class="btn btn-lg btn-primary btn-block" type="submit">Submit</button>
                                    <hr/>
                                </c:if>

                                <%-- Hidden current user id --%>
                                <input type="hidden"
                                       name="<%=WEB_PAGE_CURRENT_USER_ID%>"
                                       value="<%=session.getAttribute(SESSION_CURRENT_USER_ID) %>"/>
                            </form>

                        </div>
                    </c:otherwise>
                </c:choose>

            </div>
        </div>
    </div>
</div>
<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
</body>

</html>
