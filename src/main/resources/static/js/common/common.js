function $(selector) {
    return document.querySelector(selector);
}

function $_all(selector) {
    return document.querySelectorAll(selector);
}

function $_value(selector) {
    return $(selector).value;
}

function createElementFromHTML(htmlString) {
    const div = document.createElement('div');
    div.innerHTML = htmlString.trim();

    // Change this to div.childNodes to support multiple top-level nodes
    return div.firstChild;
}