/*
    returns the string representation of the given value (seconds) in the format mm:ss.
*/
function secToMMSS(value){
    var minutes = Math.floor(value / 60);
    var seconds = (value % 60);
    
    if(seconds < 10) {
        seconds = "0" + seconds;
    }
    if(minutes < 10) {
        minutes = "0" + minutes;
    }
    return minutes + ":" + seconds;
}

/* 
  checks if native form validation is available.
  Source/Further informations: http://diveintohtml5.info/storage.html
*/
function supportsLocalStorage() {
    try {
        return 'localStorage' in window && window['localStorage'] !== null;
    } catch(e) {
        return false;
    }
}

if (supportsLocalStorage()) {
    var current = JSON.parse(localStorage.getItem("recentlyViewed"));
    if (current === null) {
        current = [];
    }
    var newEntry = {url: window.location.pathname, title: document.title};
    var newEntryIsInCurrent = false;
    $.each(current, function (index, val) {
        $(".recently-viewed-list").append('<li class="recently-viewed-link"><a href="' + val.url + '">' + val.title + '</a></li>');
        newEntryIsInCurrent = newEntryIsInCurrent || (val.url === newEntry.url && val.title === newEntry.title);
    });
    if (current.length > 0) {
        $(".recently-viewed-list, .recently-viewed-headline").show();
    }
    if (!newEntryIsInCurrent) {
        current.unshift(newEntry);
    }
    localStorage.setItem("recentlyViewed", JSON.stringify(current.slice(0, 3)));
}

function writeNewText(el, secs) {
    if (secs > 0) {
        secs--;
        el.html(secToMMSS(secs));
    }
    else {
        el.html(el.data("end-text"));
        el.parents(".product").addClass("expired");
    }
};

$(".js-time-left").each(function() {
    var endTime = $(this).data("end-time").split(",");
    endTime = new Date(endTime[0],endTime[1]-1,endTime[2],endTime[3],endTime[4],endTime[5],endTime[6]);
    var today = new Date();
    var diffS = Math.round((endTime - today) / 1000);
    var that = $(this);
    writeNewText(that, diffS);
    setInterval(function () {
        if (diffS > 0) {
            diffS--;
        }
        writeNewText(that, diffS);
    }, 1000);
});

function formatCurrency(x) {
    // regex from http://stackoverflow.com/a/2901298
    return x.toFixed(2).replace(".", $("body").data('decimal-separator')).replace(/\B(?=(\d{3})+(?!\d))/g, $("body").data('grouping-separator')) + "&nbsp;€";
}

$('.bid-form').on('submit', function(e) {
    e.preventDefault();
    $.post(
        $(this).attr("action"),
        {amount: $("[name=new-price]").val()},
        function(data) {
            if (data.success) {
                $(".highest-bid").html(formatCurrency(data.amount));
                $(".highest-bidder").html(data.name);
                $(".bid-error").hide();
                $("[name=new-price]").val("");
                $(".balance").html(formatCurrency(data.balance));
                $(".running-auctions-count").html(data.runningAuctions);
                var auctionLabel = $(".running-auctions-count").next();
                auctionLabel.html(auctionLabel.data(data.runningAuctions === 1 ? "singular" : "plural"));
            }
            else {
                $(".bid-error").show();
            }
        }
    );
});

var socket = new WebSocket("ws://localhost:8080/socket");
socket.onmessage = function (event) {
    var data = JSON.parse(event.data);
    var auctionLabel;

    if (data.type == "newBid") {
        $("[data-product-id=\""+data.id+"\"] .highest-bid").html(formatCurrency(data.amount));
        $("[data-product-id=\""+data.id+"\"] .highest-bidder").html(data.userFullName);
        $("[data-product-id=\""+data.id+"\"] .product-price").html(formatCurrency(data.amount));
        $("[data-product-id=\""+data.id+"\"] .product-highest").html(data.userFullName);
    }
    else if (data.type == "expiredProducts") {
        $(".balance").html(formatCurrency(data.balance));
        $(".running-auctions-count").html(data.running);
        auctionLabel = $(".running-auctions-count").next();
        auctionLabel.html(auctionLabel.data(data.running === 1 ? "singular" : "plural"));
        $(".won-auctions-count").html(data.won);
        auctionLabel = $(".won-auctions-count").next();
        auctionLabel.html(auctionLabel.data(data.won === 1 ? "singular" : "plural"));
        $(".lost-auctions-count").html(data.lost);
        auctionLabel = $(".lost-auctions-count").next();
        auctionLabel.html(auctionLabel.data(data.lost === 1 ? "singular" : "plural"));
        $.each(data.expiredProducts, function(index, id) {
            $("[data-product-id=\""+id+"\"] .bid-form").hide();
            $("[data-product-id=\""+id+"\"] .detail-time").hide();
            $("[data-product-id=\""+id+"\"] .auction-expired-text").show();
            $("[data-product-id=\""+id+"\"] .product").addClass("expired");
        });
    }
    else if (data.type == "reimbursement") {
        $(".balance").html(formatCurrency(data.balance));
    }
};
