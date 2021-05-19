<%@ page import="static com.company.manager.constans.UserAttrAndParamNames.*" %>
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Prevent caching --%>
<%
    response.addHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.addHeader("Cache-Control", "pre-check=0, post-check=0");
    response.setDateHeader("Expires", 0);
%>

<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <title>Login</title>
</head>

<%-- Prepare variables --%>
<c:set var="email" value="<%=EMAIL%>" />
<c:set var="password" value="<%=PASSWORD%>" />
<c:set var="remember_user" value="<%=REMEMBER_USER%>" />
<c:set var="email_value" value="<%=request.getAttribute(EMAIL)%>" />
<c:set var="invalid_email_or_password_message" value="<%=request.getAttribute(INVALID_EMAIL_OR_PASSWORD_MESSAGE)%>" />

<body>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-md-5">
            <div class="card">

                <%-- Header --%>
                <div class="card-header">
                    <h3 class="panel-title">Login</h3>
                </div>

                <%-- Body --%>
                <div class="card-body">
                    <form accept-charset="UTF-8" action="try-sign-in" method="post">
                        <fieldset>

                            <%--Email--%>
                            <div class="form-group">
                                <input
                                    <c:if test="${empty invalid_email_or_password_message}">
                                        class="form-control"
                                    </c:if>
                                    <%--If invalid email or password --%>
                                    <c:if test="${not empty invalid_email_or_password_message}">
                                        class="form-control is-invalid"
                                    </c:if>
                                    placeholder="yourmail@example.com"
                                    name="${email}"
                                    type="text"
                                    <c:if test="${not empty invalid_email_or_password_message}">
                                        value="${email_value}"
                                    </c:if>
                                    required>
                            </div>

                            <%--Password--%>
                            <div class="form-group">
                                <input
                                    <c:if test="${empty invalid_email_or_password_message}">
                                            class="form-control"
                                    </c:if>
                                    <%--If invalid email or password --%>
                                    <c:if test="${not empty invalid_email_or_password_message}">
                                            class="form-control is-invalid"
                                    </c:if>
                                    class="form-control"
                                    placeholder="Password"
                                    name="${password}"
                                    type="password"
                                    required>
                            </div>

                            <%--Invalid email or passsword message--%>
                            <c:if test="${not empty invalid_email_or_password_message}">
                                <div class="alert alert-danger" role="alert">
                                    ${invalid_email_or_password_message}
                                </div>
                            </c:if>

                            <%--"Remember me" checkbox--%>
                            <div class="form-check mb-3">
                                <input class="form-check-input" name="${remember_user}" id="remember_me" type="checkbox" value="Remember me">
                                <label class="form-check-label" for="remember_me">Remember me</label>
                            </div>

                            <%--Log In button--%>
                            <input class="btn btn-lg btn-success btn-block" type="submit" value="Log In">

                        </fieldset>
                    </form>

                    <hr/>
                    <center><h5>OR</h5></center>

                    <%--Sign up button--%>
                    <form action="registration-page" method="get">
                        <input class="btn btn-lg btn-primary btn-block" type="submit" value="Sign Up">
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