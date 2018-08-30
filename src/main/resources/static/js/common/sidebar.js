let boardSummary;
document.addEventListener("DOMContentLoaded", function (evt) {
    boardSummary = new BoardSummary($_(".board-summary"));
    boardSummary.requestBoardSummary();
    init();
});

function init() {
    addPageShowEvent();
    drawinitTeams();
    initClickEvent();
    createTeam();

    document.addEventListener("click", (evt) => {
        hideMakeTeamBox();
    });
    $_(".sidebar-makeTeam-container").addEventListener("click", (evt) => {
        evt.stopPropagation();
    });

    const inputfield = [$_(".sidebar-makeTeam-name-box"), $_(".sidebar-makeTeam-description-box")];
    const button = $_(".sidebar-makeTeam-submit-button");

    limitInputSize($_(".sidebar-makeTeam-name-box"), 20);
    limitInputSize($_(".sidebar-makeTeam-description-box"), 255);
    for (let input of inputfield) {
        checkValidInput(input, button);
    }
}

function hideMakeTeamBox() {
    $_(".sidebar-makeTeam-container .sidebar-makeTeam-box").style.display = 'none';
    $_(".sidebar-makeTeam-name-box").placeholder = "";
    $_(".sidebar-makeTeam-description-box").placeholder = "";
}

function addPageShowEvent() {
    window.addEventListener("pageshow", function (event) {
        const historyTraversal = event.persisted ||
            (typeof window.performance != "undefined" &&
                window.performance.navigation.type === 2);
        if (historyTraversal) {
            // Handle page restore.
            window.location.reload();
        }
    });
}

function initClickEvent() {
    $_(".sidebar-makeTeam-button").addEventListener("click", (evt) => {
        evt.preventDefault();
        if ($_(".sidebar-makeTeam-box").style.display === 'none') {
            $_(".sidebar-makeTeam-box").style.display = 'block';
            $_(".sidebar-makeTeam-name-box").focus();
        } else {
            hideMakeTeamBox();
        }
    })

    $_(".sidebar-makeTeam-title>span").addEventListener("click", (evt) => {
        $_(".sidebar-makeTeam-box").style.display = 'none';
    })
}

function drawinitTeams() {
    fetchManager({
        url: "/api/teams",
        method: "GET",
        headers: {"content-type": "application/json"},
        callback: drawTeams
    });
}

function drawTeams(status, result) {
    const template = Handlebars.templates["precompile/sidebar_template"];
    for (const team of result) {
        $_(".sidebar-team-list").appendChild(createElementFromHTML(template(team)));
    }
    selectTeam();
}

function createTeam() {
    $_(".sidebar-makeTeam-submit-button").addEventListener("click", (evt) => {
        evt.preventDefault();
        const postObject = {
            "name": $_value(".sidebar-makeTeam-name-box"),
            "description": $_value(".sidebar-makeTeam-description-box")
        };

        fetchManager({
            url: "/api/teams",
            method: "POST",
            headers: {"content-type": "application/json"},
            body: JSON.stringify(postObject),
            callback: displayTeam
        });
    })

}

function displayTeam(status, result) {
    if (status === 201) {
        $_(".sidebar-makeTeam-container .sidebar-makeTeam-box").style.display = 'none';
        $_(".sidebar-makeTeam-name-box").value = "";
        $_(".sidebar-makeTeam-description-box").value = "";

        const template = Handlebars.templates["precompile/sidebar_template"];
        $_(".sidebar-team-list").innerHTML += template(result.team);
        boardSummary.drawTeamBoards(result);
        selectTeam();

    } else {
        result.forEach(function (result) {
            document.getElementsByName(result.errorType)[0].placeholder = result.message;
        });
    }

}

function selectTeam() {
    lists = $_all(".sidebar-team-list > li");
    for (let i = 0; i < lists.length; i++) {
        lists[i].addEventListener("click", (evt) => {
            window.location.href = `/team/${evt.target.getAttribute("data-id")}`;
        })
    }

}



