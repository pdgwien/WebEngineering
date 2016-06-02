<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setBundle basename="messages" />
<fmt:message key="sidebar.auction" var="auction" />
<fmt:message key="sidebar.auctions" var="auctions" />
<jsp:useBean id="user" scope="request" type="at.ac.tuwien.big.we16.ue3.model.User" />
<aside class="sidebar" aria-labelledby="userinfoheadline">
  <div class="user-info-container">
    <h2 class="accessibility" id="userinfoheadline"><fmt:message key="sidebar.userData" /></h2>
    <dl class="user-data properties">
      <dt class="accessibility"><fmt:message key="sidebar.userName" />:</dt><dd class="user-name">${user.fullName}</dd>
      <dt><fmt:message key="sidebar.balance" />:</dt>
      <dd>
        <span class="balance"><fmt:formatNumber value="${user.convertedBalance}" maxFractionDigits="2" minFractionDigits="2"/> €</span>
      </dd>
      <dt><fmt:message key="sidebar.running" />:</dt>
      <dd>
        <span class="running-auctions-count">${user.runningAuctionsCount}</span>
        <span class="auction-label" data-plural="${auctions}" data-singular="${auction}">${user.runningAuctionsCount == 1 ? auction : auctions}</span>
      </dd>
      <dt><fmt:message key="sidebar.won" />:</dt>
      <dd>
        <span class="won-auctions-count">${user.wonAuctionsCount}</span>
        <span class="auction-label" data-plural="${auctions}" data-singular="${auction}">${user.wonAuctionsCount == 1 ? auction : auctions}</span>
      </dd>
      <dt><fmt:message key="sidebar.lost" />:</dt>
      <dd>
        <span class="lost-auctions-count">${user.lostAuctionsCount}</span>
        <span class="auction-label" data-plural="${auctions}" data-singular="${auction}">${user.lostAuctionsCount == 1 ? auction : auctions}</span>
      </dd>
    </dl>
  </div>
  <div class="recently-viewed-container">
    <h3 class="recently-viewed-headline"><fmt:message key="sidebar.recentlyViewed" /></h3>
    <ul class="recently-viewed-list"></ul>
  </div>
</aside>