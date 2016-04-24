/*
    Helper functions for the first exercise of the Web Engineering course
*/

/* 
    checks if native form validation is available.
    Source/Further informations: http://diveintohtml5.info/everything.html
*/
function hasFormValidation() {
    return 'noValidate' in document.createElement('form');
}

/* 
    checks if native date input is available.
    Source/Further informations: http://diveintohtml5.info/everything.html
*/
function hasNativeDateInput() {
    var i = document.createElement('input');
    i.setAttribute('type', 'date');
    return i.type !== 'text';
}

var DATE_DELIMITERS = ['/','\\','-'];

/*
    returns the string representation of a date input field in the format dd.mm.yyyy.
    If the value of the input field can't be interpreted as a date, the original value is returned.
*/
function getNormalizedDateString(selector) {
    value = $(selector).val();
    
    // normalize delimiter to .
    for(var i = 0; i < DATE_DELIMITERS.length; i++) 
        value = value.split(DATE_DELIMITERS[i]).join(".");
    
    // check if date might be reverse, i.e., yyyy.mm.dd
    rehtml5 = /^(\d{4})\.(\d{1,2})\.(\d{1,2})$/;
    if(regs = value.match(rehtml5))
        value = regs[3] + "." + regs[2] + "." + regs[1];

    // check if valid date string dd.mm.yyyy
    date = /^(\d{1,2})\.(\d{1,2})\.(\d{4})$/;
    if(value.match(date))
      return value;
    return $(selector).val();
}

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

function writeNewText(el, secs) {
    if (secs > 0) {
        secs--;
        el.html(secToMMSS(secs));
    }
    else {
        el.html(el.data("end-text"));
        el.parents(".product").addClass("expired");
    }
}
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

// Depending on the setup of your server, servlet, and socket, you may have to
// change the URL.

//Ajax-Code


$(".bid-form").submit(function(event) {
    event.preventDefault();
    var x = $(".bid-form").serialize() + "&auctionName=" + document.getElementById("productheadline").innerHTML;
    $.post("/bid",x,function (data) {
        if(data.bidSuccess == true) {
            $(".bid-error").css({"display":"none"});
            $(".balance")[0].innerHTML=formatCurrency(data.newBalance/100);
            $(".running-auctions-count")[0].innerHTML=data.auctionCount;
            $(".highest-bidder")[0].innerHTML=data.highestBidder;
            $(".highest-bid")[0].innerHTML=formatCurrency(data.newBid/100);
        }else{
            $(".bid-error").css({"display":"block"});
        }
    })
});



var socket = new WebSocket("ws://localhost:8080/socket");
socket.onmessage = function (event) {
    var message = JSON.parse(event.data);
    /***  write your code here ***/

    switch (message.type) {
        case "balance":
            handleBalance(message);
            break;
        case "bid":
            handleBid(message);
            break;
        case "auctionExpired":
            handleAuctionExpired(message);
            break;
    }
};

function handleBalance(message) {

    $(".balance")[0].innerHTML = formatCurrency(message.balance/100);
    if (window.location.pathname == "/details") {
        $.get('overview', function (data) {});
    }

}

function handleAuctionExpired(message) {
    if(window.location.pathname == "/login")
        return;

    $(".balance")[0].innerHTML = formatCurrency(message.balance/100);
    $(".running-auctions-count")[0].innerHTML = message.currentAuctions;
    $(".won-auctions-count")[0].innerHTML = message.wonAuctions;
    $(".lost-auctions-count")[0].innerHTML = message.lostAuctions;

    if (window.location.pathname == "/details") {
        var name = $(".main-headline")[0];
        $.get('overview', function (data) {});

        if(name.innerHTML == message.auction) {
            $(".auction-active").hide();
            $(".auction-expired-text").show();
        }
    }
}

function handleBid(message) {
    if (window.location.pathname == "/overview") {

        var products = document.getElementsByClassName("product-name");

        for (var i = 0; i < products.length; i++) {

            if(products[i].innerHTML == message.auction){

                var currentUser = $(".user-name")[0].innerHTML;
                var currentHighest = $(products[i]).siblings(".product-highest")[0].innerHTML;

                if(message.userFullName != currentUser && currentUser==currentHighest){
                    var highlighted = $(products[i]).closest(".highlight")[0];
                    $(highlighted).removeClass("highlight");
                }
                
                $(products[i]).siblings(".product-price")[0].innerHTML=formatCurrency(message.newBid/100);
                $(products[i]).siblings(".product-highest")[0].innerHTML=message.userFullName;

            }
        }

    } else if (window.location.pathname == "/details"){
        var name = $(".main-headline")[0];
        if(name.innerHTML == message.auction){
            $(".auction-active .highest-bid")[0].innerHTML=formatCurrency(message.newBid/100);
            $(".auction-active .highest-bidder")[0].innerHTML=message.userFullName;

            $.get('overview', function(data) {});
        }
    }
}

$( document ).ready(function() {

    $( ".money" ).each(function() {
        $(this).html(formatCurrency(parseFloat($(this).text())));
    });

    if(supportsLocalStorage() && localStorage.getItem("history") !== null ){

        $(".recently-viewed-list").show();
        $(".recently-viewed-headline").show();

        var recent = JSON.parse(localStorage.getItem("history"));

        var ul = $(".recently-viewed-list")[0];

        for (var i = 0; i<recent.length; ++i) {

            var li = document.createElement("li");
            li.setAttribute("class", "recently-viewed-link");

            var a = document.createElement("a");
            a.setAttribute("href" , "http://localhost:8080/details?name=" + recent[i]);
            a.innerHTML = recent[i];

            li.appendChild(a);
            ul.appendChild(li);
        }

    } else {
        $(".recently-viewed-list").hide();
        $(".recently-viewed-headline").hide();
    }

});
