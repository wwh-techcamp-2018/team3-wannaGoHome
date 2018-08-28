var boardSummary;
document.addEventListener("DOMContentLoaded", function(evt) {
    boardSummary = new BoardSummary($_(".board-summary"));
    boardSummary.requestBoardSummary();
    init();
});

function init() {
    addPageShowEvent();
    drawinitTeams();
    initClickEvent();
    createTeam();

    document.addEventListener("click", (evt)=>{
        $_(".sidebar-makeTeam-container .sidebar-makeTeam-box").style.display = 'none';
    });
    $_(".sidebar-makeTeam-container").addEventListener("click", (evt)=>{
        evt.stopPropagation();
    });

    const inputfield = [$_(".sidebar-makeTeam-name-box"), $_(".sidebar-makeTeam-description-box")];
    const button = $_(".sidebar-makeTeam-submit-button");
    checkValidInput(inputfield, button);

}

function addPageShowEvent() {
    window.addEventListener( "pageshow", function ( event ) {
        const historyTraversal = event.persisted ||
            ( typeof window.performance != "undefined" &&
                window.performance.navigation.type === 2 );
        if ( historyTraversal ) {
            // Handle page restore.
            window.location.reload();
        }
    });
}

function initClickEvent() {

    $_(".sidebar-makeTeam-button").addEventListener("click", (evt) => {
        evt.preventDefault();
        if($_(".sidebar-makeTeam-box").style.display === 'none') {
            $_(".sidebar-makeTeam-box").style.display = 'block';
            $_(".sidebar-makeTeam-name-box").focus();
        } else {
            $_(".sidebar-makeTeam-box").style.display = 'none';
        }
    })

    $_(".sidebar-makeTeam-title>span").addEventListener("click", (evt)=> {
        $_(".sidebar-makeTeam-box").style.display = 'none';
    })
}

function drawinitTeams() {
    fetchManager({
        url: "/api/teams",
        method: "GET",
        headers: {"content-type": "application/json"},
        callback : drawTeams
    });
}

function drawTeams(status, result) {
    let html = "";
    const template = Handlebars.templates["precompile/sidebar_template"];
    for(team of result) {
        html  += template(team);
    }
    $_(".sidebar-team-list").innerHTML += html;
    selectTeam();
}

function createTeam() {
    $_(".sidebar-makeTeam-submit-button").addEventListener("click", (evt)=>{
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
    if(status === 201) {
        $_(".sidebar-makeTeam-container .sidebar-makeTeam-box").style.display = 'none';
        $_(".sidebar-makeTeam-name-box").value = "";
        $_(".sidebar-makeTeam-description-box").value = "";

        const template = Handlebars.templates["precompile/sidebar_template"];
        $_(".sidebar-team-list").innerHTML += template(result.team);
        boardSummary.drawTeamBoards(result);

    } else {
        result.forEach(function(result){
            $_(".sidebar-makeTeam-name-box").value = "";
            document.getElementsByName(result.errorType)[0].placeholder = result.message;
        });
    }

}

function selectTeam() {
    lists = $_all(".sidebar-team-list > li");
    for(let i = 0; i < lists.length ; i ++) {
        lists[i].addEventListener("click", (evt)=>{
            //TODO 팀 페이지 생겼을때 할 것
            window.location.href = `/team/${evt.target.getAttribute("data-id")}`;
        })
    }

}


function checkValidInput(inputfield, button) {
    for(input of inputfield) {
        input.addEventListener("input", (evt)=> {

            if(checkNullInput(inputfield)) {
                button.style.backgroundColor = '#61bd4f';
                button.style.color = '#ffffff';
            } else {
                button.style.backgroundColor = '#f8f9f9';
                button.style.color = '#aaaaaa';
            }
        })
    }
}

function checkNullInput(inputfield) {
    for(input of inputfield) {
        if(input.value.length === 0) {
            return false;
        }
    }
    return true;
}