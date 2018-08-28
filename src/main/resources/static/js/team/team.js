document.addEventListener("DOMContentLoaded", function(evt) {
    const teamIndex = window.location.href.trim().split("/").pop();

    fetchManager({
        url: `/api/teams/${teamIndex}`,
        method: "GET",
        headers: {"content-type": "application/json"},
        callback: drawTeam
    });

    $_(".invite-team-button").addEventListener("click", function(evt) {
        evt.stopPropagation();
        const left = evt.clientX - 150;
        const top = evt.clientY + 5;
        $_(".user-search-box").style.display = "block";
        $_(".user-search-box").style.left = left + "px";
        $_(".user-search-box").style.top = top + "px";
    });

    $_(".user-search-box").addEventListener("click", function(evt) {
        evt.stopPropagation();
    });

    document.addEventListener("click", function(evt) {
        $_(".user-search-box").style.display = "none";
    });

});

function drawTeam(status, result) {
    const teamHeaderTemplate = Handlebars.templates["precompile/team/team_page_header"];
    $_(".team-page-header").insertBefore(createElementFromHTML(teamHeaderTemplate(result)), $_(".team-options-holder"));
}