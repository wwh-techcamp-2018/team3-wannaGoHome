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


function fetchManager({url, method, body, headers, callback}) {
    fetch(url, {method, body, headers, credentials: "same-origin"})
        .then((response) => {
            var value = response;
            if(value.status === 200 || value.status === 201) {
                return value.json();
            } else {
                return;
            }

        }).then((result) => {
        callback(value.status, result);
    });
}