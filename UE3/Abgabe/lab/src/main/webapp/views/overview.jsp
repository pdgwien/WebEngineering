<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="products" scope="request" type="java.util.Collection<at.ac.tuwien.big.we16.ue3.model.Product>" />
<jsp:useBean id="user" scope="request" type="at.ac.tuwien.big.we16.ue3.model.User" />
<fmt:setBundle basename="messages" />
<jsp:include page='partials/header.jsp'>
    <jsp:param name="title" value="Produkte" />
    <jsp:param name="showLogout" value="true" />
</jsp:include>
<jsp:include page='partials/sidebar.jsp' />
<c:set var="localeCode" value="${pageContext.response.locale}" />

<main role="main" aria-labelledby="productsheadline">
    <h2 class="main-headline" id="productsheadline">Produkte</h2>
    <ul class="products">
        <c:forEach items="${products}" var="product">
            <li class="product-outer" data-product-id="${product.id}">
                <a href="product/${product.id}" class="product <c:if test="${product.hasAuctionEnded()}">expired</c:if> <c:if test="${not empty product.highestBid && product.highestBid.isBy(user)}">highlight</c:if>" title="Mehr Informationen zu ${product.name}">
                    <img class="product-image" src="images/${product.image}" alt="${product.imageAlt}">
                    <dl class="product-properties properties">
                        <dt>Bezeichnung</dt>
                        <dd class="product-name">${product.name}</dd>
                        <dt>Preis</dt><dd class="product-price">
                            <c:choose>
                                <c:when test="${product.hasBids()}">
                                    <fmt:formatNumber value="${product.highestBid.convertedAmount}"  maxFractionDigits="2" minFractionDigits="2"/> €
                                </c:when>
                                <c:otherwise>
                                    <span class="no-bids">Noch keine Gebote</span>
                                </c:otherwise>
                            </c:choose>
                        </dd>
                        <dt>Verbleibende Zeit</dt><dd data-end-time="<fmt:formatDate value="${product.auctionEnd}" pattern="yyyy,MM,dd,HH,mm,ss,SSS"/>" data-end-text="abgelaufen" class="product-time js-time-left"></dd>
                        <dt>H&ouml;chstbietende/r</dt><dd class="product-highest">${product.highestBid.user.fullName}</dd>
                    </dl>
                </a>
            </li>
        </c:forEach>
    </ul>
</main>
<jsp:include page='partials/footer.jsp' />