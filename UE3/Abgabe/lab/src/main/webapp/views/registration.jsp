<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setBundle basename="messages" />
<jsp:include page='partials/header.jsp'>
    <jsp:param name="title" value="Registrieren" />
    <jsp:param name="showLogin" value="true" />
</jsp:include>

<main role="main" aria-labelledby="formheadline">
    <form class="form" method="post">
        <h2 id="formheadline" class="registration-headline">Registrieren</h2>

        <fieldset>
            <legend>Persönliche Daten</legend>
            <div class="form-row">
                <label class="form-label" for="salutation-input">
                    Anrede *
                </label>
                <select name="salutation" id="salutation-input" class="form-input">
                    <option value="ms">Frau</option>
                    <option value="mr">Herr</option>
                </select>
            </div>
            <div class="form-row">
                <label class="form-label" for="firstname-input">
                    Vorname *
                </label>
                <input type="text" name="firstname" id="firstname-input" class="form-input">
            </div>
            <div class="form-row">
                <label class="form-label" for="lastname-input">
                    Nachname *
                </label>
                <input type="text" name="lastname" id="lastname-input" class="form-input">
            </div>
            <div class="form-row">
                <label class="form-label" for="dateofbirth-input">
                    Geburtsdatum *
                </label>
                <input type="text" name="dateofbirth" id="dateofbirth-input" class="form-input">
            </div>
            <div class="form-row">
                <label class="form-label" for="email-input">
                    Email *
                </label>
                <input type="text" name="email" id="email-input" class="form-input">
            </div>
            <div class="form-row">
                <label class="form-label" for="password-input">
                    Passwort *
                </label>
                <input type="password" name="password" id="password-input" class="form-input">
            </div>
        </fieldset>

        <fieldset>
            <legend>Versandadresse</legend>
            <div class="form-row">
                <label class="form-label" for="streetAndNumber-input">
                    Straße/Hausnummer
                </label>
                <input type="text" name="streetAndNumber" id="streetAndNumber-input" class="form-input">
            </div>
            <div class="form-row">
                <label class="form-label" for="postcodeAndCity-input">
                    PLZ/Ort
                </label>
                <input type="text" name="postcodeAndCity" id="postcodeAndCity-input" class="form-input">
            </div>
            <div class="form-row">
                <label class="form-label" for="country-input">
                    Land
                </label>
                <select name="country" id="country-input" class="form-input">
                    <option value="at">Österreich</option>
                    <option value="de">Deutschland</option>
                    <option value="ch">Schweiz</option>
                </select>
            </div>
        </fieldset>

        <div class="form-row form-row-center">
            <p>
                <label for="terms">
                    <input type="checkbox" name="terms" id="terms" required onchange="isFormValid()">
                    Mit meiner Anmeldung akzeptiere ich die AGB. *
                </label>
            </p>
        </div>
        <div class="form-row form-row-center">
            <button class="button button-submit register-button">
                Registrieren
            </button>
        </div>
        <div class="form-row form-row-center">
            <p>
                Mit * gekennzeichnete Felder sind Pflichtfelder.
            </p>
        </div>
    </form>
</main>

<jsp:include page='partials/footer.jsp'/>