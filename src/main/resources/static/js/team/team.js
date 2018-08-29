let teamIndex;
let currentUser;
document.addEventListener("DOMContentLoaded", function(evt) {
    teamIndex = window.location.href.trim().split("/").pop();

    fetchManager({
        url: `/api/teams/${teamIndex}/member`,
        method: "GET",
        headers: {"content-type": "application/json"},
        callback: (status, result) => {
            currentUser = result;
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
        }
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

    $_(".user-search-bar-holder input").addEventListener("keyup", function(evt) {
        $_(".user-search-bar-results").innerHTML = "";

        const queryText = evt.currentTarget.value.trim();
        if(queryText.length > 1) {
            fetchManager({
                url: `/api/teams/${teamIndex}/search/${queryText}`,
                method: "GET",
                headers: {"content-type" : "application/json"},
                callback: drawSearchResults
            });
        }
    });

    document.addEventListener("click", function(evt) {
        $_(".user-search-box").style.display = "none";
    });

});

function drawTeam(status, result) {
    const teamHeaderTemplate = Handlebars.templates["precompile/team/team_page_header"];
    $_(".team-page-header").insertBefore(createElementFromHTML(teamHeaderTemplate(result)), $_(".team-options-holder"));

    if(currentUser.userPermission != "Admin") {
        $_(".team-profile-edit-button").style.display = "none";
        return;
    }
    $_(".team-profile-image").addEventListener("click", function(evt) {
        evt.preventDefault();
        evt.stopPropagation();
        $_(".profile-avatar-holder").style.display = "block";
        $_(".profile-avatar-holder").style.left = evt.pageX + "px";
        $_(".profile-avatar-holder").style.top = $_("body").scrollTop + evt.pageY + 5 + "px";
    });

    $_(".team-profile-edit-button").addEventListener("click", function(evt) {
        evt.preventDefault();
        evt.stopPropagation();
        $_(".profile-avatar-holder").style.display = "block";
        $_(".profile-avatar-holder").style.left = evt.pageX + "px";
        $_(".profile-avatar-holder").style.top = $_("body").scrollTop + evt.pageY + 5 + "px";
    });

    $_("body").addEventListener("click", function(evt) {
        $_(".profile-avatar-holder").style.display = "none";
    });

    $_(".profile-avatar-holder").addEventListener("click", function(evt) {
        evt.stopPropagation();
    });

    $_(".profile-upload").oninput = function(evt) {
        const uploadFiles = $_(".profile-upload-button").files;
        if(uploadFiles.length !== 0) {
            fileFetchManager({
                url: `/api/teams/profile/${teamIndex}`,
                body: getFileFormData(uploadFiles),
                callback: (status, response) => {
                    if(status === 200) {
                        $_(".profile-upload").reset();
                        $_(".team-profile-image-section").src = response.profile;
                        $_("body").click();
                    }
                }
            })
        }
    }

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
        $_(".user-search-bar-results").style.display = "block";
        for(const user of result) {
            const userElem = createElementFromHTML(searchResultTemplate(user))
            $_(".user-search-bar-results").appendChild(userElem);
            user.teamId = teamIndex;
            userElem.addEventListener("click", function(evt) {
                fetchManager({
                    url: `/api/teams/${this.teamId}/invite/${this.id}`,
                    method: "POST",
                    body: {},
                    headers: {"content-type": "application/json"},
                    callback: (status, result) => {console.log(result)}
                });
                document.querySelector("body").click();
            }.bind(user));
        }
    }
}