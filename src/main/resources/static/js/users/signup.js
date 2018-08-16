document.addEventListener("DOMContentLoaded", function(evt) {
    new SignForm(document.querySelector("form"), "/api/users");
});