<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setBundle basename="messages" />
<jsp:useBean id="error" scope="request" type="java.lang.Boolean" />
<jsp:include page='partials/header.jsp'>
    <jsp:param name="title" value="Anmelden" />
    <jsp:param name="showRegistration" value="true" />
</jsp:include>

<main role="main" aria-labelledby="formheadline">
    <form class="form" method="post">
        <h2 id="formheadline" class="registration-headline">Anmelden</h2>
        <div class="form-row">
            <label class="form-label" for="email-input">
                Email
            </label>
            <input type="email" name="email" id="email-input" required class="form-input">
            <span id="email-error" class="error-text"></span>
        </div>
        <div class="form-row">
            <label class="form-label" for="password-input">
                Passwort
            </label>
            <input type="password" name="password" id="password-input" required class="form-input" minlength="4" maxlength="12">
            <span id="password-error" class="error-text"></span>
        </div>
        <%if(error){%>
        <p style="color:red; text-align:center">Invalid user/password combination</p>
        <%}%>
        <div class="form-row form-row-center">
            <button class="button button-submit">
                Anmelden
            </button>
        </div>
    </form>
</main>
<jsp:include page='partials/footer.jsp'/>