let teamIndex;
let currentUser;
let changeRightsFunction = () => {};
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

    $_(".profile-avatar-holder-icon").addEventListener("click", function (evt) {
        $_("body").click();
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
        $_(".team-profile-delete-button").style.display = "none";
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

    $_(".team-profile-delete-button").addEventListener("click", function(evt) {
        evt.preventDefault();
        evt.stopPropagation();
        onClickDeleteTeamButton();
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
                        return;
                    }
                    const errorNode = $_(".profile-upload-error")
                    errorNode.innerText = response[0].message;
                    errorNode.style.display = "block";
                }
            })
        }
    };

    $_(".user-rights-content").addEventListener("click", function(evt) {
        evt.stopPropagation();
        changeRightsFunction(evt);
    });

}

function onClickDeleteTeamButton() {
    showDialog("Delete Team", "Really Delete it? Careful", onClickOkDelete, ()=>{});
}

function onClickOkDelete() {
    fetchManager({
        url: "/api/teams/" + teamIndex,
        method: "DELETE",
        callback: handleDeleteTeam
    });
}

function handleDeleteTeam(status) {
    if (status === 200) {
        window.location.href = "/";
        return;
    }

    showDialog("Fail to delete team", "You cannot delete it haha !!!!");
}

function drawMembers(status, result) {
    const teamMemberTemplate = Handlebars.templates["precompile/team/team_page_member"];
    for(const member of result) {
        const memberElem = createElementFromHTML(teamMemberTemplate(member));
        $_(".team-users-holder").appendChild(memberElem);
        if(currentUser.userPermission == "Admin" && currentUser.id != member.id) {
            memberElem.querySelector(".rights-button").addEventListener("click", function(evt) {
                evt.stopPropagation();

                $_(".user-rights-box").style.display = "block";
                $_(".user-rights-box").style.left = evt.pageX + "px";
                $_(".user-rights-box").style.top = $_("body").scrollTop + evt.pageY + "px";

                changeRightsFunction = function(evt) {

                    fetchManager({
                        url: `/api/teams/${teamIndex}/permission`,
                        method: "PUT",
                        headers: {"content-type" : "application/json"},
                        body: JSON.stringify({"teamId": teamIndex,
                                "userId": this.id,
                                "permission": evt.target.id.trim()}),
                        callback: (status, response) => {
                            location.reload();
                        }
                    })
                }.bind(this);
            }.bind(member));

            memberElem.querySelector(".remove-button").addEventListener("click", function(evt) {
                fetchManager({
                    url: `/api/teams/${teamIndex}/users`,
                    method: "DELETE",
                    body: JSON.stringify({"teamId": teamIndex,
                        "userId": this.id}),
                    callback: (status, response) => {
                        if(status == 200) {
                            location.reload();
                        }
                    }
                })
            }.bind(member));

            document.addEventListener("click", function(evt) {
                $_(".user-rights-box").style.display = "none";
                $_(".profile-upload-error").style.display = "none";
            });

        }

        if(currentUser.id == member.id) {
            memberElem.querySelector(".remove-button").style.visibility = "hidden";
        }

        if(currentUser.userPermission == "Manager") {
            memberElem.querySelector(".rights-button").style.display = "none";
            memberElem.querySelector(".remove-button").style.display = "none";

        }
        if(currentUser.userPermission == "Member") {
            memberElem.querySelector(".rights-button").style.display = "none";
            memberElem.querySelector(".remove-button").style.display = "none";
            $_(".invite-team-button-holder").style.display = "none";
        }
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
                    callback: (status, result) => {
                        if(status === 200) {
                            showNormalDialog("팀 초대", "팀 초대 요청을 보냈습니다.", $_("body").click());
                            return;
                        }
                        showDialog("팀 초대", result[0].message, $_("body").click());
                    }
                });
                document.querySelector("body").click();
            }.bind(user));
        }
    }
}
