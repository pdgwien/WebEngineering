<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="error" scope="request" type="at.ac.tuwien.big.we16.ue3.error.FormError"/>
<fmt:setBundle basename="messages" />
<fmt:message key="registration.registerHeadline" var="title"/>
<jsp:include page='partials/header.jsp'>
    <jsp:param name="title" value="${title}" />
    <jsp:param name="showLogin" value="true" />
</jsp:include>

<main role="main" aria-labelledby="formheadline">
    <form class="form" method="post">
        <h2 id="formheadline" class="registration-headline"><fmt:message key="registration.registerHeadline" /></h2>

        <fieldset>
            <legend><fmt:message key="registration.personalData" /></legend>
            <div class="form-row">
                <label class="form-label" for="salutation-input">
                    <fmt:message key="registration.salutation" /> *
                </label>
                <select name="salutation" id="salutation-input" class="form-input">
                    <option value="ms"><fmt:message key="registration.salutation.ms" /></option>
                    <option value="mr"><fmt:message key="registration.salutation.mr" /></option>
                </select>
            </div>
            <c:if test="${error.titleError}">
                <span id="salutation-error" class="error-text"><fmt:message key="registration.error.mandatory" /></span>
            </c:if>
            <div class="form-row">
                <label class="form-label" for="firstname-input">
                    <fmt:message key="registration.firstname" /> *
                </label>
                <input type="text" name="firstname" id="firstname-input" class="form-input">
            </div>
            <c:if test="${error.firstNameError}">
                <span id="firstname-error" class="error-text"><fmt:message key="registration.error.mandatory" /></span>
            </c:if>
            <div class="form-row">
                <label class="form-label" for="lastname-input">
                    <fmt:message key="registration.lastname" /> *
                </label>
                <input type="text" name="lastname" id="lastname-input" class="form-input">
            </div>
            <c:if test="${error.lastNameError}">
                <span id="lastname-error" class="error-text"><fmt:message key="registration.error.mandatory" /></span>
            </c:if>
            <div class="form-row">
                <label class="form-label" for="dateofbirth-input">
                    <fmt:message key="registration.dateofbirth" /> *
                </label>
                <input type="text" name="dateofbirth" id="dateofbirth-input" class="form-input">
            </div>
            <c:choose>
                <c:when test="${error.dateFormatError}">
                    <span id="date-format-error" class="error-text"><fmt:message key="registration.error.dateFomat" /></span>
                </c:when>
                <c:when test="${error.dateError}">
                    <span id="date-error" class="error-text"><fmt:message key="registration.error.date" /></span>
                </c:when>
            </c:choose>
            <div class="form-row">
                <label class="form-label" for="email-input">
                    <fmt:message key="registration.email" /> *
                </label>
                <input type="text" name="email" id="email-input" class="form-input">
            </div>
            <c:choose>
                <c:when test="${error.mailFormatError}">
                    <span id="email-format-error" class="error-text"><fmt:message key="registration.error.emailFormat" /></span>
                </c:when>
                <c:when test="${error.mailError}">
                    <span id="email-error" class="error-text"><fmt:message key="registration.error.email" /></span>
                </c:when>
            </c:choose>
            <div class="form-row">
                <label class="form-label" for="password-input">
                    <fmt:message key="registration.password" /> *
                </label>
                <input type="password" name="password" id="password-input" class="form-input">
            </div>
            <c:if test="${error.passwordError}">
                <span id="password-error" class="error-text"><fmt:message key="registration.error.password" /></span>
            </c:if>
        </fieldset>

        <fieldset>
            <legend><fmt:message key="registration.shippingaddress" /></legend>
            <div class="form-row">
                <label class="form-label" for="streetAndNumber-input">
                    <fmt:message key="registration.streetAndNumber" />
                </label>
                <input type="text" name="streetAndNumber" id="streetAndNumber-input" class="form-input">
            </div>
            <div class="form-row">
                <label class="form-label" for="postcodeAndCity-input">
                    <fmt:message key="registration.postcodeAndCity" />
                </label>
                <input type="text" name="postcodeAndCity" id="postcodeAndCity-input" class="form-input">
            </div>
            <div class="form-row">
                <label class="form-label" for="country-input">
                    <fmt:message key="registration.country" />
                </label>
                <select name="country" id="country-input" class="form-input">
                    <option value="at"><fmt:message key="registration.country.at" /></option>
                    <option value="de"><fmt:message key="registration.country.de" /></option>
                    <option value="ch"><fmt:message key="registration.country.ch" /></option>
                </select>
            </div>
        </fieldset>

        <div class="form-row form-row-center">
            <p>
                <label for="terms">
                    <input type="checkbox" name="terms" id="terms" required onchange="isFormValid()">
                    <fmt:message key="registration.acceptTerms" /> *
                </label>
            </p>
        </div>
        <div class="form-row form-row-center">
            <button class="button button-submit register-button">
                <fmt:message key="registration.register" />
            </button>
        </div>
        <div class="form-row form-row-center">
            <p>
                <fmt:message key="registration.compulsoryFields" />
            </p>
        </div>
    </form>
</main>

<jsp:include page='partials/footer.jsp'/>