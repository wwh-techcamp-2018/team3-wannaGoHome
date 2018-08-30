document.addEventListener("DOMContentLoaded", function (evt) {
    new SignForm(document.querySelector("form"), "/api/users/signin", "/");
    history.pushState(null, document.title, location.href);
    window.addEventListener('popstate', (evt) => {
        window.location.href = "/"
    });
});