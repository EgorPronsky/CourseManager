<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="static com.company.manager.string_constans.UserAttrAndParamNames.*" %>
<%@ page import="static com.company.manager.filter.RegistrationFilter.EMAIL_EXISTS_MESSAGE_ATTR_NAME" %>
<%@ page session="false" %>
<html>

<head>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
  <script>
    function validateRegistrationInput() {
      // Validating password matching
      const password = document.getElementById("password").value;
      const passwordConfirm = document.getElementById("password_confirm").value;

      if (password !== passwordConfirm) {
        alert( "Password mismatch!" );
        event.preventDefault();
        return false;
      }
      return true;
    }
  </script>

  <title>Registration</title>

</head>

<%-- Prepare variables --%>
<c:set var="first_name" value="<%=FIRST_NAME%>" />
<c:set var="last_name" value="<%=LAST_NAME%>" />
<c:set var="is_teacher" value="<%=IS_TEACHER%>" />
<c:set var="email" value="<%=EMAIL%>" />
<c:set var="password" value="<%=PASSWORD%>" />

<c:set var="first_name_value" value="<%=request.getParameter(FIRST_NAME)%>" />
<c:set var="last_name_value" value="<%=request.getParameter(LAST_NAME)%>" />
<c:set var="is_teacher_value" value="<%=request.getParameter(IS_TEACHER)%>" />
<c:set var="email_value" value="<%=request.getParameter(EMAIL)%>" />

<c:set var="email_exists_message" value="<%=request.getAttribute(EMAIL_EXISTS_MESSAGE_ATTR_NAME)%>" />

<body>

<div class="container">
    <div class="row justify-content-md-center">
      <div class="col-md-5">
        <div class="card">

        <%-- Header --%>
        <div class="card-header">
          <ol class="breadcrumb">
            <li class="breadcrumb-item">
              <a href="${pageContext.request.contextPath}/login-page" style="font-size: medium">Back to login page</a>
            </li>
            <li class="breadcrumb-item active" aria-current="page" style="font-size: medium">Registration</li>
          </ol>
        </div>

        <%-- Body --%>
        <div class="card-body">
          <form accept-charset="UTF-8" action="sign-up" onsubmit="validateRegistrationInput()" method="post">
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
                      pattern="[a-z0-9._-]+@[a-z0-9.-]+\.[a-z]{2,}$"
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
                    required>
                </div>

                <%--Password (confirm)--%>
                <div class="form-group">
                  <label for="password_confirm">Password (confirm)</label>
                  <input
                    class="form-control"
                    placeholder="Password"
                    id="password_confirm"
                    type="password"
                    required>
                </div>

                <%-- "I'm a teacher" checkbox --%>
                <div class="form-check">
                  <input class="form-check-input"
                         name="${is_teacher}"
                         id="is_teacher"
                         type="checkbox"
                         value="Is teacher"
                         <c:if test="${not empty is_teacher_value}">checked</c:if>>
                  <label class="form-check-label" for="is_teacher">I'm a teacher</label>
                </div>
            </fieldset>

            <%-- Submit button--%>
            <hr/>
            <input class="btn btn-lg btn-success btn-block" type="submit" value="Sign up">

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