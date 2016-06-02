<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setBundle basename="messages" />
<jsp:useBean id="product" scope="request" type="at.ac.tuwien.big.we16.ue3.model.Product"/>
<c:set var="localeCode" value="${pageContext.response.locale}" />
<c:choose>
    <c:when test="${localeCode == 'de' }">
        <c:set var="productName" value="${product.name_de}" />
    </c:when>
    <c:otherwise>
        <c:set var="productName" value="${product.name_en}" />
    </c:otherwise>
</c:choose>
<jsp:include page='partials/header.jsp'>
    <jsp:param name="title" value="${productName}" />
    <jsp:param name="showLogout" value="true" />
</jsp:include>
<jsp:include page='partials/sidebar.jsp'/>


<main aria-labelledby="productheadline" class="details-container">
        <div class="details-image-container">
            <img class="details-image" src="/images/${product.image}" alt="${product.imageAlt}">
        </div>
        <div data-product-id="${product.id}" class="details-data">
            <h2 class="main-headline" id="productheadline">${productName}</h2>
            <div class="auction-expired-text" <c:if test="${!product.hasAuctionEnded()}">style="display:none"</c:if>>
                <p>
                    <fmt:message key="details.auctionEnded" />
                    <fmt:message key="details.sold" >
                        <fmt:param>
                            <span class="highest-bid">
                                <c:if test="${product.hasBids()}">
                                    <fmt:formatNumber value="${product.highestBid.convertedAmount}" maxFractionDigits="2" minFractionDigits="2"/> €
                                </c:if>
                            </span>
                        </fmt:param>
                        <fmt:param>
                            <span class="highest-bidder">
                                <c:if test="${product.hasBids()}">
                                    ${product.highestBid.user.fullName}
                                </c:if>
                            </span>
                        </fmt:param>
                    </fmt:message>
                </p>
            </div>
            <c:if test="${!product.hasAuctionEnded()}">
                <p class="detail-time"><fmt:message key="details.remainingTime" />: <span data-end-time="<fmt:formatDate value="${product.auctionEnd}" pattern="yyyy,MM,dd,HH,mm,ss,SSS"/>" class="detail-rest-time js-time-left"></span></p>
                <form class="bid-form" method="post" action="/product/${product.id}/bid">
                    <label class="bid-form-field" id="highest-price">
                        <c:choose>
                            <c:when test="${product.hasBids()}">
                                <span class="highest-bid"><fmt:formatNumber value="${product.highestBid.convertedAmount}" maxFractionDigits="2" minFractionDigits="2"/> €</span>
                                <span class="highest-bidder">${product.highestBid.user.fullName}</span>
                            </c:when>
                            <c:otherwise>
                                <span class="highest-bid"><fmt:message key="details.noBids" /></span>
                                <span class="highest-bidder"></span>
                            </c:otherwise>
                        </c:choose>
                    </label>
                    <label class="accessibility" for="new-price"></label>
                    <input type="number" step="0.01" min="0" id="new-price" class="bid-form-field form-input" name="new-price" required>
                    <p class="bid-error"><fmt:message key="details.bidError" /></p>
                    <input type="submit" id="submit-price" class="bid-form-field button" name="submit-price" value="<fmt:message key="details.submitBid" />">
                </form>
            </c:if>
            <h2 class="sub-main-headline" id="relatedproductsheadline"><fmt:message key="details.relatedProducts" /></h2>
            <ul class="related-products-list">
                <c:forEach items="${product.relatedProducts}" var="relatedProduct">
                    <c:choose>
                        <c:when test="${localeCode == 'de' }">
                            <li>${relatedProduct.name_de}</li>
                        </c:when>
                        <c:otherwise>
                            <li>${relatedProduct.name_en}</li>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </ul>
        </div>
</main>
<jsp:include page='partials/footer.jsp'/>