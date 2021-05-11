<%@ page import="static web.servlet.access.SignInServlet.CURRENT_USER_SESSION_ATTR" %>
<%@ page import="domain.user.UserRole" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>

<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">

    <title>Title</title>
</head>

<%-- Prepare variables --%>
<c:set var="current_user" value="<%=session.getAttribute(CURRENT_USER_SESSION_ATTR)%>" />

<body>
<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-lg-8">
            <div class="card">

                <%-- Header --%>
                <div class="card-header">
                    <h3 class="panel-title">Welcome, ${current_user.userInfo.firstName}!</h3>
                </div>

                <%-- Body--%>
                <div class="card-body">
                    <form action="view/core/select-my-courses.jsp" method="get">
                        <input class="btn btn-lg btn-primary btn-block" type="submit" value="View my courses">
                    </form>
                    <hr/>

                    <c:choose>
                        <%--Special button for students--%>
                        <c:when test="${current_user.userInfo.userRole == UserRole.STUDENT}">
                            <form action="${pageContext.request.contextPath}/join-new-courses-page" method="get">
                                <input class="btn btn-lg btn-primary btn-block" type="submit" value="Sign up for a new courses">
                            </form>
                        </c:when>

                        <%--Special button for teachers--%>
                        <c:when test="${current_user.userInfo.userRole == UserRole.TEACHER}">
                            <form action="view/core/create-new-course.jsp" method="get">
                                <input class="btn btn-lg btn-primary btn-block" type="submit" value="Create a new course">
                            </form>
                        </c:when>

                        <c:otherwise>
                            <p>Undefined</p>
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
