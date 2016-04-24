<div class="user-info-container">
    <h2 class="accessibility" id="userinfoheadline">Benutzerdaten</h2>
    <dl class="user-data properties">
        <dt class="accessibility">Name:</dt>
        <dd class="user-name"><%=user.getFirstName()%> <%=user.getLastName()%></dd>
        <dt>Kontostand:</dt>
        <dd>
            <span class="balance money"><%=user.displayBalance()%></span>
        </dd>
        <dt>Laufend:</dt>
        <dd>
            <span class="running-auctions-count"><%=user.getCurrentAuctions()%></span>
            <span class="auction-label" data-plural="Auktionen" data-singular="Auktion">Auktionen</span>
        </dd>
        <dt>Gewonnen:</dt>
        <dd>
            <span class="won-auctions-count"><%=user.getWonAuctions()%></span>
            <span class="auction-label" data-plural="Auktionen" data-singular="Auktion">Auktionen</span>
        </dd>
        <dt>Verloren:</dt>
        <dd>
            <span class="lost-auctions-count"><%=user.getLostAuctions()%></span>
            <span class="auction-label" data-plural="Auktionen" data-singular="Auktion">Auktionen</span>
        </dd>
    </dl>
</div>
<div class="recently-viewed-container">
    <h3 class="recently-viewed-headline">Zuletzt angesehen</h3>
    <!-- TODO: Implement Local Storage-->
    <ul class="recently-viewed-list"></ul>
</div>