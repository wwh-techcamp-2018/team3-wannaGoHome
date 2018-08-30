document.addEventListener("DOMContentLoaded", function (evt) {
    new SignForm(document.querySelector("form"), "/api/users/signin", "/");
    window.addEventListener('popstate', (evt) => {window.location.href="/"});
});