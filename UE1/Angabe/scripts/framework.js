/**
 * Checks if native form validation is available.
 * Source: http://diveintohtml5.info/everything.html
 */
function hasFormValidation() {
    return 'noValidate' in document.createElement('form');
}

/**
 * Checks if native date input is available.
 * Source: http://diveintohtml5.info/everything.html
 */
function hasNativeDateInput() {
    var i = document.createElement('input');
    i.setAttribute('type', 'date');
    return i.type !== 'text';
}

/**
 * Returns the string representation of a date input field in the format dd.mm.yyyy.
 * If the value of the input field can't be interpreted as a date, the original value is returned.
 */
function getNormalizedDateString(selector) {
    var dateDelimiters = ['/','\\','-'];
    value = $(selector).val();

    // normalize delimiter to .
    for(var i = 0; i < dateDelimiters.length; i++)
        value = value.split(dateDelimiters[i]).join(".");

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