<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setBundle basename="messages" />
<!doctype html>
<html lang="de">
<head>
    <meta charset="utf-8">
    <title>BIG Bid - ${param.title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="styles/style.css">
</head>
<body data-decimal-separator="${decimalSeparator}" data-grouping-separator="${groupingSeparator}">

<a href="#productsheadline" class="accessibility">Zum Inhalt springen</a>

<header role="banner" aria-labelledby="bannerheadline">
    <img class="title-image" src="images/big-logo-small.png" alt="BIG Bid logo">
    <h1 class="header-title" id="bannerheadline">
        BIG Bid
    </h1>
        <nav role="navigation" aria-labelledby="navigationheadline">
            <h2 class="accessibility" id="navigationheadline">Navigation</h2>
            <ul class="navigation-list">
                <li>
                    <c:choose>
                        <c:when test="${not empty param.showLogout}">
                            <a href="./logout" class="button" accesskey="l">Abmelden</a>
                        </c:when>
                        <c:when test="${not empty param.showLogin}">
                            <a href="./login" class="button" accesskey="l">Anmelden</a>
                        </c:when>
                        <c:when test="${not empty param.showRegistration}">
                            <a href="./registration" class="button" accesskey="r">Registrieren</a>
                        </c:when>
                    </c:choose>
                </li>
            </ul>
        </nav>
</header>
<section class="main-container">