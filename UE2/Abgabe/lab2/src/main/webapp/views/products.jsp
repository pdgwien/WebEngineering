<jsp:useBean id="auctionStorage" scope="session" class="at.ac.tuwien.big.we16.ue2.model.AuctionStorage"/>
<%@ page import="java.util.List" %>
<%@ page import="at.ac.tuwien.big.we16.ue2.model.Auction" %>

<%List<Auction> auctions = auctionStorage.getAuctions();
for(Auction a : auctions){

    String productClasses = "";
    if(a.isExpired()){
        productClasses += "expired";
    }

    if(a.getHighestBidder()== user){
        productClasses += "highlight";
    }
    String bidderName = "";
    if(a.getHighestBidder()!=null){
           bidderName = a.getHighestBidder().getFullName();
       }
%>
<div class="product-outer">
    <a href="/details?name=<%=a.getName()%>" class="product <%=productClasses%> "
       title="Mehr Informationen zu <%=a.getName()%>">
        <img class="product-image" src="../images/<%=a.getImage()%>" alt="">
        <dl class="product-properties properties">
            <dt>Bezeichnung</dt>
            <dd class="product-name"><%=a.getName()%></dd>
            <dt>Preis</dt>
            <dd class="product-price">
                <%=a.displayHighestBid()%>
            </dd>
            <dt>Verbleibende Zeit</dt>
            <dd data-end-time="<%=a.getExpirationDate()%>" data-end-text="abgelaufen"
                class="product-time js-time-left"></dd>
            <dt>Höchstbietende/r</dt>
            <dd class="product-highest"><%=bidderName%></dd>
        </dl>
    </a>
</div>
<% }%>