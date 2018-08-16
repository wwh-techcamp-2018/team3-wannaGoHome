function $(selector) {
    return document.querySelector(selector);
}

function $_all(selector) {
    return document.querySelectorAll(selector);
}

function $_value(selector) {
    return $(selector).value;
}

function getManager({url, method, headers, callback}) {
    fetch(url, {method, headers, credentials: "same-origin"})
        .then((response) => {
            const value = response;
            return value.json();
        }).then((result) => {
        callback(result);
    });
}

function fetchManager({url, method, body, callback}) {
    fetch(url, {method, body, headers: {"Content-type": "application/json"}, credentials: "same-origin"})
        .then((response) => {
            res = response;
            return response.json();
        }).then((result) => {
        callback(res.status, result);
    });
}
