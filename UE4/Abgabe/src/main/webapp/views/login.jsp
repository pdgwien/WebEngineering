<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setBundle basename="messages" />
<jsp:useBean id="error" scope="request" type="java.lang.Boolean" />
<fmt:message key="login.headline" var="title"/>
<jsp:include page='partials/header.jsp'>
    <jsp:param name="title" value="${title}" />
    <jsp:param name="showRegistration" value="true" />
</jsp:include>

<main role="main" aria-labelledby="formheadline">
    <form class="form" method="post">
        <h2 id="formheadline" class="registration-headline"><fmt:message key="login.headline" /></h2>
        <c:if test="${error}">
            <p class="error-text">
                <fmt:message key="login.invalidLoginData" />
            </p>
        </c:if>
        <div class="form-row">
            <label class="form-label" for="email-input">
                <fmt:message key="login.email" />
            </label>
            <input type="email" name="email" id="email-input" required class="form-input">
            <span id="email-error" class="error-text"></span>
        </div>
        <div class="form-row">
            <label class="form-label" for="password-input">
                <fmt:message key="login.password" />
            </label>
            <input type="password" name="password" id="password-input" required class="form-input" minlength="4" maxlength="12">
            <span id="password-error" class="error-text"></span>
        </div>
        <div class="form-row form-row-center">
            <button class="button button-submit">
                <fmt:message key="login.login" />
            </button>
        </div>
    </form>
</main>
<jsp:include page='partials/footer.jsp'/>