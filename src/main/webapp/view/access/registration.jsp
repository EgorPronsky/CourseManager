<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="static servlet.access.SignUpServlet.*" %>
<%@ page import="static filter.RegistrationFilter.EMAIL_EXISTS_MESSAGE_ATTR" %>
<%@ page import="static filter.RegistrationFilter.PASSWORD_MISMATCH_MESSAGE_ATTR" %>
<%@ page import="static filter.SignInFilter.EMAIL_PARAM" %>
<%@ page import="static filter.SignInFilter.PASSWORD_PARAM" %>

<html>

<head>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
  <title>Registration</title>
</head>

<%-- Prepare variables --%>
<c:set var="first_name" value="<%=FIRST_NAME_PARAM%>" />
<c:set var="last_name" value="<%=LAST_NAME_PARAM%>" />
<c:set var="is_teacher" value="<%=IS_TEACHER_PARAM%>" />
<c:set var="email" value="<%=EMAIL_PARAM%>" />
<c:set var="password" value="<%=PASSWORD_PARAM%>" />
<c:set var="password_confirm" value="<%=PASSWORD_CONFIRM_PARAM%>" />

<c:set var="first_name_value" value="<%=request.getParameter(FIRST_NAME_PARAM)%>" />
<c:set var="last_name_value" value="<%=request.getParameter(LAST_NAME_PARAM)%>" />
<c:set var="is_teacher_value" value="<%=request.getParameter(IS_TEACHER_PARAM)%>" />
<c:set var="email_value" value="<%=request.getParameter(EMAIL_PARAM)%>" />
<c:set var="password_value" value="<%=request.getParameter(PASSWORD_PARAM)%>" />
<c:set var="password_confirm_value" value="<%=request.getParameter(PASSWORD_CONFIRM_PARAM)%>" />

<c:set var="email_exists_message" value="<%=request.getAttribute(EMAIL_EXISTS_MESSAGE_ATTR)%>" />
<c:set var="password_mismatch_message" value="<%=request.getAttribute(PASSWORD_MISMATCH_MESSAGE_ATTR)%>" />

<body>

<div class="container">
    <div class="row justify-content-md-center">
      <div class="col-md-5">
        <div class="card">

        <%-- Header --%>
        <div class="card-header">
          <h3 class="panel-title">Registration</h3>
        </div>

        <%-- Body --%>
        <div class="card-body">
          <form accept-charset="UTF-8" action="sign-up" method="post">
            <fieldset>

              <%--First name--%>
              <div class="form-group">
                <label for="first_name">First name</label>
                <input
                  class="form-control"
                  id="first_name"
                  name="${first_name}"
                  type="text"
                  <c:if test="${not empty first_name_value}">
                    value="${first_name_value}"
                  </c:if>
                  required>
              </div>

              <%--Last name--%>
              <div class="form-group">
                <label for="last_name">Last name</label>
                <input
                  class="form-control"
                  id="last_name"
                  name="${last_name}"
                  type="text"
                  <c:if test="${not empty last_name_value}">
                    value="${last_name_value}"
                  </c:if>
                  required>
              </div>

              <%--Email--%>
              <div class="form-group">
                <label for="email">Email</label>
                  <input
                    <c:if test="${empty email_exists_message}">
                      class="form-control"
                    </c:if>
                    <%--If input is not valid--%>
                    <c:if test="${not empty email_exists_message}">
                      class="form-control is-invalid"
                    </c:if>
                    placeholder="yourmail@example.com"
                    id="email"
                    name="${email}"
                    type="email"
                    <c:if test="${not empty email_value}">
                      value="${email_value}"
                    </c:if>
                    pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$"
                    required>
                <div class="invalid-feedback">${email_exists_message}</div>
              </div>

              <%--Password--%>
              <div class="form-group">
                <label for="password">Password</label>
                <input
                  class="form-control"
                  placeholder="Password"
                  id="password"
                  name="${password}"
                  type="password"
                  <c:if test="${not empty password_value}">
                    value="${password_value}"
                  </c:if>
                  required>
              </div>

              <%--Password (confirm)--%>
              <div class="form-group">
                <label for="password_confirm">Password (confirm)</label>
                <input
                  <c:if test="${empty password_mismatch_message}">
                    class="form-control"
                  </c:if>
                  <%--If input is not valid--%>
                  <c:if test="${not empty password_mismatch_message}">
                    class="form-control is-invalid"
                  </c:if>
                  placeholder="Password"
                  id="password_confirm"
                  name="${password_confirm}"
                  type="password"
                  <c:if test="${not empty password_confirm_value}">
                    value="${password_confirm_value}"
                  </c:if>
                  required>
                <div class="invalid-feedback">${password_mismatch_message}</div>
              </div>

              <%-- "Is teacher" checkbox --%>
              <div class="form-check">
                <input class="form-check-input" name="${is_teacher}" id="is_teacher" type="checkbox" value="Is teacher">
                <label class="form-check-label" for="is_teacher">I'm a teacher</label>
              </div>

              <%-- Submit button--%>
              <hr/>
              <input class="btn btn-lg btn-success btn-block" type="submit" value="Sign up">

            </fieldset>
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