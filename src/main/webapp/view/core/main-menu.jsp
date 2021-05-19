<%@ page import="com.company.manager.domain.user.UserRole" %>
<%@ page import="static com.company.manager.constans.UserAttrAndParamNames.CURRENT_USER_INFO_SESSION" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>

<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">

    <title>Title</title>
</head>

<%-- Prepare variables --%>
<c:set var="current_user_info" value="<%=session.getAttribute(CURRENT_USER_INFO_SESSION)%>" />

<body>
<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-lg-8">
            <div class="card">

                <%-- Header --%>
                <div class="card-header">
                    <h3 class="panel-title">Welcome, ${current_user_info.firstName}!</h3>
                </div>

                <%-- Body--%>
                <div class="card-body">
                    <form action="${pageContext.request.requestURI}/select-courses" method="get">
                        <input class="btn btn-lg btn-primary btn-block" type="submit" value="View my courses">
                    </form>
                    <hr/>

                    <c:choose>
                        <%--Special button for students--%>
                        <c:when test="${current_user_info.userRole == UserRole.STUDENT}">
                            <form action="${pageContext.request.requestURI}/join-new-courses" method="get">
                                <input class="btn btn-lg btn-primary btn-block" type="submit" value="Join new courses">
                            </form>
                        </c:when>

                        <%--Special button for teachers--%>
                        <c:when test="${current_user_info.userRole == UserRole.TEACHER}">
                            <form action="${pageContext.request.requestURI}/create-new-course" method="get">
                                <input class="btn btn-lg btn-primary btn-block" type="submit" value="Create a new course">
                            </form>
                        </c:when>

                        <c:otherwise>
                            <p>Undefined</p>
                        </c:otherwise>
                    </c:choose>

                    <%-- Log out button --%>
                    <hr/>
                    <form action="${pageContext.request.requestURI}/log-out" method="get">
                        <input class="btn btn-lg btn-success btn-block" type="submit" value="Log out">
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
