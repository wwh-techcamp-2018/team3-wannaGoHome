document.addEventListener("DOMContentLoaded", function(evt) {
    const teamIndex = window.location.href.trim().split("/").pop();

    fetchManager({
        url: `/api/teams/${teamIndex}`,
        method: "GET",
        headers: {"content-type": "application/json"},
        callback: drawTeam
    });

    fetchManager({
        url: `/api/teams/${teamIndex}/members`,
        method: "GET",
        headers: {"content-type": "application/json"},
        callback: drawMembers
    });

    $_(".invite-team-button").addEventListener("click", function(evt) {
        evt.stopPropagation();
        const left = evt.pageX - 150;
        const top = evt.pageY + 5;
        $_(".user-search-box").style.display = "block";
        $_(".user-search-box").style.left = left + "px";
        $_(".user-search-box").style.top = top + "px";
        $_(".user-search-box input").focus();
    });

    $_(".user-search-box").addEventListener("click", function(evt) {
        evt.stopPropagation();
    });

    $_(".user-search-bar-holder input").addEventListener("keypress", function(evt) {
        if(detectEnter(evt)) {
            console.log();
            const queryText = evt.currentTarget.value.trim();
            if(queryText.length > 1) {
                fetchManager({
                    url: `/api/teams/${teamIndex}/search/${queryText}`,
                    method: "GET",
                    headers: {"content-type" : "application/json"},
                    callback: drawSearchResults
                });
            }
        }
    });

    document.addEventListener("click", function(evt) {
        $_(".user-search-box").style.display = "none";
    });

});

function drawTeam(status, result) {
    const teamHeaderTemplate = Handlebars.templates["precompile/team/team_page_header"];
    $_(".team-page-header").insertBefore(createElementFromHTML(teamHeaderTemplate(result)), $_(".team-options-holder"));
}

function drawMembers(status, result) {
    const teamMemberTemplate = Handlebars.templates["precompile/team/team_page_member"];
    for(const member of result) {
        $_(".team-users-holder").appendChild(createElementFromHTML(teamMemberTemplate(member)));
    }
}

function drawSearchResults(status, result) {
    const searchResultTemplate = Handlebars.templates["precompile/team/search_result_member"];
    $_(".user-search-bar-results").innerHTML = "";
    if(result.length > 0) {
        console.log(result);
        $_(".user-search-bar-results").style.display = "block";
        for(const user of result) {
            const userElem = createElementFromHTML(searchResultTemplate(user))
            $_(".user-search-bar-results").appendChild(userElem);
            userElem.addEventListener("click", function(evt) {

            });
        }
    }
}