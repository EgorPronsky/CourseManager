<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.company.manager.domain.archive.CourseResult" %>
<%@ page import="static com.company.manager.constans.CourseAttrAndParamNames.COURSE_ID" %>
<%@ page import="static com.company.manager.constans.CourseAttrAndParamNames.COURSE" %>
<%@ page import="static com.company.manager.servlet.core.students_actions.GetCourseStudentsServlet.GET_STUDENTS_TO_SEE" %>
<%@ page import="static com.company.manager.servlet.core.students_actions.GetCourseStudentsServlet.GET_STUDENTS_TO_GRADE" %>
<%@ page import="static com.company.manager.constans.CourseAttrAndParamNames.*" %>
<%@ page import="com.company.manager.domain.course.Course" %>
<%@ page import="com.company.manager.domain.archive.StudentCourseResult" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="com.company.manager.domain.user.User" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Collections" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <title>My courses</title>
</head>

<%-- Prepare variables --%>
<c:set var="course" value="<%=request.getAttribute(COURSE)%>" />
<c:set var="course_students_view_target" value="<%=request.getParameter(COURSE_STUDENTS_VIEW_TARGET)%>" />

<c:set var="course_id" value="<%=COURSE_ID%>" />
<c:set var="target_get_students_to_see" value="<%=GET_STUDENTS_TO_SEE%>" />
<c:set var="target_get_students_to_grade" value="<%=GET_STUDENTS_TO_GRADE%>" />

<%-- Sorting students --%>
<%
    List<StudentCourseResult> sortedStudentsResults = new ArrayList<>(((Course)pageContext.getAttribute("course")).getStudentResults());
    Collections.sort(sortedStudentsResults, new Comparator<StudentCourseResult>() {
        @Override
        public int compare(StudentCourseResult  o1, StudentCourseResult o2) {
            return o1.getStudent().getUserInfo().getLastName()
                    .compareTo(o2.getStudent().getUserInfo().getLastName());
        }
    });
    List<User> sortedStudents = new ArrayList<>();
    for (StudentCourseResult scr : sortedStudentsResults) {
        sortedStudents.add(scr.getStudent());
    }
%>
<c:set var="sorted_students" value="<%=sortedStudents%>" />

<body>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-lg-8">
            <div class="card">

                <%-- Header --%>
                <div class="card-header">
                    <h3 class="panel-title">${course.courseInfo.name} students</h3>
                </div>

                <%-- Body --%>
                <c:choose>
                    <%--If courses list is empty--%>
                    <c:when test="${course.studentResults.size() == 0}">
                        <hr/>
                        <center><h3 class="panel-title">No students yet</h3></center>
                        <hr/>
                    </c:when>

                    <c:otherwise>

                        <div class="card-body">
                            <form
                                <c:if test="${course_students_view_target == target_get_students_to_see}">
                                    action="${pageContext.request.contextPath}/main-menu/select-courses/leave-course"
                                </c:if>
                                <c:if test="${course_students_view_target == target_get_students_to_grade}">
                                    action="${pageContext.request.contextPath}/main-menu/select-courses/my-not-graded-courses/students/grade-students"
                                </c:if>
                                method="post">

                                <table class="table">
                                    <thead class="thead-dark">
                                    <tr>
                                        <th scope="col">First name</th>
                                        <th scope="col">Last name</th>
                                        <c:if test="${course_students_view_target == target_get_students_to_see}">
                                            <th scope="col">Kick student</th>
                                        </c:if>
                                        <c:if test="${course_students_view_target == target_get_students_to_grade}">
                                            <th scope="col">Result</th>
                                        </c:if>
                                    </tr>
                                    </thead>

                                    <tbody>
                                        <c:forEach var="student" items="${sorted_students}">
                                            <tr>
                                                <td>${student.userInfo.firstName}</td>
                                                <td>${student.userInfo.lastName}</td>

                                                <c:if test="${course_students_view_target == target_get_students_to_see}">
                                                    <%-- Hidden course id --%>
                                                    <c:if test="${not empty course}">
                                                        <input type="hidden" name="<%=COURSE_ID%>" value="${course.id}"/>
                                                    </c:if>
                                                    <td>
                                                        <button class="btn btn-lg btn-primary btn-danger" type="submit"  name="<%=COURSE_STUDENT_ID%>" value="${student.id}">Kick out</button>
                                                    </td>
                                                </c:if>

                                                <c:if test="${course_students_view_target == target_get_students_to_grade}">
                                                    <td>
                                                        <div class="form-check form-check-inline">
                                                            <input class="form-check-input" type="radio" name="${student.id}" id="result_bad${student.id}" value="${CourseResult.BAD}">
                                                            <label class="form-check-label" for="result_bad${student.id}">${CourseResult.BAD.toString()}</label>
                                                        </div>
                                                        <div class="form-check form-check-inline">
                                                            <input class="form-check-input" type="radio" name="${student.id}" id="result_ok${student.id}" value="${CourseResult.OK}" checked>
                                                            <label class="form-check-label" for="result_ok${student.id}">${CourseResult.OK.toString()}</label>
                                                        </div>
                                                        <div class="form-check form-check-inline">
                                                            <input class="form-check-input" type="radio" name="${student.id}" id="result_perfect${student.id}" value="${CourseResult.EXCELLENT}" >
                                                            <label class="form-check-label" for="result_perfect${student.id}">${CourseResult.EXCELLENT.toString()}</label>
                                                        </div>
                                                    </td>
                                                </c:if>
                                            </tr>
                                        </c:forEach>

                                    </tbody>
                                </table>

                                <hr/>

                                <c:if test="${course_students_view_target == target_get_students_to_grade}">
                                    <button class="btn btn-lg btn-primary btn-block" type="submit"  name="${course_id}" value="${course.id}">Submit</button>
                                    <hr/>
                                </c:if>
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
