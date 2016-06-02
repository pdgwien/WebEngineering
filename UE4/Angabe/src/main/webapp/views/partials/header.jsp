<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="decimalSeparator" scope="request" type="java.lang.Character"/>
<jsp:useBean id="groupingSeparator" scope="request" type="java.lang.Character"/>
<fmt:setBundle basename="messages" />
<!doctype html>
<html lang="de">
<head>
    <meta charset="utf-8">
    <title>BIG Bid - ${param.title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="/styles/style.css">
</head>
<body data-decimal-separator="${decimalSeparator}" data-grouping-separator="${groupingSeparator}">

<a href="#productsheadline" class="accessibility"><fmt:message key="header.jumpToContent" /></a>

<header role="banner" aria-labelledby="bannerheadline">
    <img class="title-image" src="/images/big-logo-small.png" alt="BIG Bid logo">
    <h1 class="header-title" id="bannerheadline">
        BIG Bid
    </h1>
        <nav role="navigation" aria-labelledby="navigationheadline">
            <h2 class="accessibility" id="navigationheadline"><fmt:message key="header.navigation" /></h2>
            <ul class="navigation-list">
                <li>
                    <c:choose>
                        <c:when test="${not empty param.showLogout}">
                            <a href="/logout" class="button" accesskey="l"><fmt:message key="header.logout" /></a>
                        </c:when>
                        <c:when test="${not empty param.showLogin}">
                            <a href="/login" class="button" accesskey="l"><fmt:message key="header.login" /></a>
                        </c:when>
                        <c:when test="${not empty param.showRegistration}">
                            <a href="/registration" class="button" accesskey="l"><fmt:message key="header.register" /></a>
                        </c:when>
                    </c:choose>
                </li>
            </ul>
        </nav>
</header>
<section class="main-container">