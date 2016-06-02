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

if (supportsLocalStorage()) {
    var current = JSON.parse(localStorage.getItem("recentlyViewed"));
    if (current === null) {
        current = [];
    }
    var newEntry = {url: window.location.pathname, title: document.title};
    var newEntryIsInCurrent = false;
    $.each(current, function (index, val) {
        $(".recently-viewed-list").append('<li class="recently-viewed-link"><a href="' + val.url + '">' + val.title + '</a></li>')
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

/**
 *
 * Registration form validation
 *
 */


/*$(".register-button").prop( "disabled", true );

function isFormValid() {
    var validForm = true;
    if(!checkBirthday())
        validForm = false;
    if(!checkEmail())
        validForm=false;
    if(!checkPassword())
        validForm=false;
    if ( validForm && $("#lastname-input").val() != "" && $("#firstname-input").val() != "" && $("#terms").is(':checked'))
        $(".register-button").prop( "disabled", false );
    else
        $(".register-button").prop( "disabled", true );
}

function checkBirthday() {
    if ($("#dateofbirth-input").val() == "") {
        $("#date-error").hide();
        return false;
    } else if (!isBirthdayValid()) {
        $("#date-error").show();
        return false;
    } else {
        $("#date-error").hide();
        return true;
    }
}

function checkEmail() {
    if ($("#email-input").val().length == 0) {
        $("#email-error").hide();
        return false;
    } else if (!isEmailValid("#email-input")) {
        $("#email-error").show();
        return false;
    } else {
        $("#email-error").hide();
        return true;
    }
}

function checkPassword() {
    if ($("#password-input").val().length == 0) {
        $("#password-error").hide();
        return false;
    } else if(!isStringLengthValid("#password-input")) {
        $("#password-error").show();
        return false;
    } else {
        $("#password-error").hide();
        return true;
    }
}

function isBirthdayValid() {
    var date = getNormalizedDateString("#dateofbirth-input");
    if (!hasFormValidation() || !hasNativeDateInput())
        return isDateformatValid() &&  isDayValid();
    else
        return isDayValid();
}

function isDayValid(){
    var refDate = new Date();
    refDate.setFullYear(refDate.getFullYear() - 18);
    var dateParts = getNormalizedDateString("#dateofbirth-input").split('.').reverse();
    // Create two dates: One assuming that the first part is the month (american format),
    // and one where the first part is the day.
    var mdDate = new Date(dateParts[0], dateParts[1] - 1, dateParts[2]);
    var mdDateIsValid = mdDate.getFullYear() == dateParts[0] && mdDate.getMonth() == dateParts[1] - 1 && mdDate.getDate() == dateParts[2];
    var dmDate = new Date(dateParts[0], dateParts[2] - 1, dateParts[1]);
    var dmDateIsValid = dmDate.getFullYear() == dateParts[0] && dmDate.getMonth() == dateParts[2] - 1 && dmDate.getDate() == dateParts[1];
    return (dmDateIsValid && dmDate <= refDate) || (mdDateIsValid && mdDate <= refDate);
}

function isDateformatValid(){
    var date = $("#dateofbirth-input").val();
    var format = /^(\d{1,2})\.(\d{1,2})\.(\d{4})$/;
    if(value.match(format))
    {
        return true;
    }
    if(date == getNormalizedDateString("#dateofbirth-input") )
    {
        return false;
    }
}

function isEmailValid(selector) {
    var re = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    return re.test($(selector).val());
}

function isStringLengthValid(selector) {
    return $(selector).val().length >= 4 &&  $(selector).val().length <= 12;
}

String.prototype.replaceAll = function(search, replace)
{
    //if replace is null, return original string otherwise it will
    //replace search string with 'undefined'.
    if(!replace)
        return this;

    return this.replace(new RegExp('[' + search + ']', 'g'), replace);
};*/