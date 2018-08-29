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

function fileFetchManager({url, body, callback}) {
    fetch(url, {method : "POST", body, credentials: "same-origin"})
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
    return (event.key == "Enter" && event.shiftKey);
}

function detectEnter(event) {
    return (event.key == "Enter");
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

function getFormattedDate(date) {
    return date.getFullYear() + "/" + checkTime(date.getMonth() + 1) + "/" +
            checkTime(date.getDate());
}

function addEscapedText(elem, text) {
    elem.innerHTML = "";
    elem.appendChild(document.createTextNode(text));
}

String.prototype.replaceAll = function(search, replacement) {
    var target = this;
    return target.replace(new RegExp(search, 'g'), replacement);
};

// if true landscape non jquery
function imageDimensions(img) {
    if(img.height > img.width) {
        return false;
    } else {
        return true;
    }
}

function setOverlayClickFunctions(elem, stopPropagateElem, clickCallBack, backgroundCallBack) {
    stopPropagateElem.addEventListener("click", function(evt) {
        evt.stopPropagation();
    });
    elem.addEventListener("click", function(evt) {
        evt.stopPropagation();
        document.querySelector("body").click();
        clickCallBack(evt);
    });

    document.addEventListener("click", function(evt) {
       backgroundCallBack(evt);
    });
}

function limitInputSize(inputElem, size) {
    inputElem.addEventListener("input", (evt) => {
        const currentValue = evt.currentTarget.value.trim();
        evt.currentTarget.value = currentValue.substring(0, Math.min(currentValue.length, size));
    });
}

function showDialog(title, description, okCallback, cancelCallback) {
    const popupElement = createElementFromHTML(`
        <div class="popup-wrapper">
            <div class="popup-background"></div>
            <div class="popup">
                <div class="popup-title">${title}</div>
                <p class="popup-description">${description}</p>
                <div class="popup-button-wrapper">
                    <button class="popup-ok-button">OK</button>
                    <button class="popup-cancel-button">Cancel</button>
                </div>
            </div>
        </div>
    `);
    document.body.appendChild(popupElement);

    popupElement.querySelector(".popup-ok-button").addEventListener("click", (evt) => {
        evt.stopPropagation();
        evt.preventDefault();
        popupElement.remove();
        if (okCallback)
            okCallback();
    });

    const cancelButton = popupElement.querySelector(".popup-cancel-button");
    if (cancelCallback) {
        cancelButton.addEventListener("click", (evt) => {
            popupElement.remove();
            cancelCallback();
        });
    }
    else {
        cancelButton.classList.add("popup-button-hide");
    }
}

class PageObject {
    constructor() {
        this.init();
        this.connectSocket();
    }

    init() {

    }

    // TODO: make a common singleton pattern socket
    connectSocket() {
        const socket = new SockJS('/websocket');
        this.stompClient = Stomp.over(socket);
        this.stompClient.debug = null;
        this.stompClient.connect({}, function (frame) {

        }.bind(this))
    }
}