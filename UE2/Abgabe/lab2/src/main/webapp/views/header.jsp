<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<img class="title-image" src="../images/big-logo-small.png" alt="BIG Bid logo">

<h1 class="header-title" id="bannerheadline">
    BIG Bid
</h1>
<nav aria-labelledby="navigationheadline">
    <h2 class="accessibility" id="navigationheadline">Navigation</h2>
    <ul class="navigation-list">
        <li>
            <c:if test="${not empty user}">
                <a href="/logout" class="button" accesskey="l">Abmelden</a>
            </c:if>
            <c:if test="${empty user}">
                <a href="" class="button" accesskey="l">Registrieren</a>
            </c:if>
        </li>
    </ul>
</nav>
