<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="products" scope="request" type="java.util.Collection<at.ac.tuwien.big.we16.ue3.model.Product>" />
<jsp:useBean id="user" scope="request" type="at.ac.tuwien.big.we16.ue3.model.User" />
<fmt:setBundle basename="messages" />
<fmt:message key="overview.headline" var="title"/>
<jsp:include page='partials/header.jsp'>
    <jsp:param name="title" value="${title}" />
    <jsp:param name="showLogout" value="true" />
</jsp:include>
<jsp:include page='partials/sidebar.jsp' />
<c:set var="localeCode" value="${pageContext.response.locale}" />

<main role="main" aria-labelledby="productsheadline">
    <h2 class="main-headline" id="productsheadline"><fmt:message key="overview.headline" /></h2>
    <div class="products">
        <c:forEach items="${products}" var="product">
            <div class="product-outer" data-product-id="${product.id}">
                <a href="/product/${product.id}" class="product <c:if test="${product.hasAuctionEnded()}">expired</c:if> <c:if test="${product.highestBid.isBy(user)}">highlight</c:if>" title="<fmt:message key="overview.moreInformation" /> ${product.name_en}">
                    <img class="product-image" src="/images/${product.image}" alt="${product.imageAlt}">
                    <dl class="product-properties properties">
                        <dt><fmt:message key="overview.name" /></dt>
                        <c:choose>
                            <c:when test="${localeCode=='de'}">
                                <dd class="product-name">${product.name_de}</dd>
                            </c:when>
                            <c:otherwise>
                                <dd class="product-name">${product.name_en}</dd>
                            </c:otherwise>
                        </c:choose>
                        <dt><fmt:message key="overview.price" /></dt><dd class="product-price">
                            <c:choose>
                                <c:when test="${product.hasBids()}">
                                    <fmt:formatNumber value="${product.highestBid.convertedAmount}"  maxFractionDigits="2" minFractionDigits="2"/> €
                                </c:when>
                                <c:otherwise>
                                    <span class="no-bids"><fmt:message key="overview.noBids" /></span>
                                </c:otherwise>
                            </c:choose>
                        </dd>
                        <dt><fmt:message key="overview.remainingTime" /></dt><dd data-end-time="<fmt:formatDate value="${product.auctionEnd}" pattern="yyyy,MM,dd,HH,mm,ss,SSS"/>" data-end-text="<fmt:message key="overview.expired" />" class="product-time js-time-left"></dd>
                        <dt><fmt:message key="overview.highestBidder" /></dt><dd class="product-highest">${product.highestBid.user.fullName}</dd>
                    </dl>
                </a>
            </div>
        </c:forEach>
    </div>
</main>
<jsp:include page='partials/footer.jsp' />