function $_(selector) {
    return document.querySelector(selector);
}

function $_all(selector) {
    return document.querySelectorAll(selector);
}

function $_value(selector) {
    return $_(selector).value;
}

function createElementFromHTML(htmlString) {
    const div = document.createElement('div');
    div.innerHTML = htmlString.trim();

    // Change this to div.childNodes to support multiple top-level nodes
    return div.firstChild;
}

function fetchManager({url, method, body, callback}) {
    // TODO: response status 를 자연스럽게 넘기기
    fetch(url, {method, body, headers: {"Content-type": "application/json"}, credentials: "same-origin"})
        .then((response) => {
            res = response;
            return response.json();
        }).then((result) => {
        callback(res.status, result);
    });
}

function rgbToHex(rgb) {
    rgb = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
    return ("#" + hex(rgb[1]) + hex(rgb[2]) + hex(rgb[3])).toUpperCase();
}

function hex(x) {
    const hexDigits = new Array
    ("0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f");
    return isNaN(x) ? "00" : hexDigits[(x - x % 16) / 16] + hexDigits[x % 16];
}

function getBoundingRect(element) {
    const rect = element.getBoundingClientRect();
    return rect;
}

function detectShiftEnter(event) {
    return (event.keyCode == 13 && event.shiftKey);
}

function detectEnter(event) {
    return (event.keyCode == 13);
}

function pasteIntoInput(el, text) {
    el.focus();
    if (typeof el.selectionStart == "number"
        && typeof el.selectionEnd == "number") {
        const val = el.value;
        const selStart = el.selectionStart;

        const value = val.slice(0, selStart) + text + val.slice(el.selectionEnd);

        // need to use setTimeout due to a slight bug in Chrome
        setTimeout(function(value) {
            this.selectionStart = this.selectionEnd = value.length;
            this.value = value;
            this.scrollTop = this.scrollHeight;
        }.bind(el, value), 0)
    } else if (typeof document.selection != "undefined") {
        const textRange = document.selection.createRange();
        textRange.text = text;
        textRange.collapse(false);
        textRange.select();
    }
}

function checkTime(value) {
    return (value < 10) ? "0" + value : value;
}

function getFormattedTime(date) {
    return date.getHours() + ":" + checkTime(date.getMinutes());
}

String.prototype.replaceAll = function(search, replacement) {
    var target = this;
    return target.replace(new RegExp(search, 'g'), replacement);
};


