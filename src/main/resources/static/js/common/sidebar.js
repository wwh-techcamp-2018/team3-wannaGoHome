var boardSummary;
document.addEventListener("DOMContentLoaded", function(evt) {
    boardSummary = new BoardSummary($(".board-summary"));
    boardSummary.requestBoardSummary();
    init();
});

function init() {
    drawinitTeams();
    initClickEvent();
    createTeam();

    document.addEventListener("click", (evt)=>{
        $(".sidebar-makeTeam-container .sidebar-makeTeam-box").style.display = 'none';
    });
    $(".sidebar-makeTeam-container").addEventListener("click", (evt)=>{
        evt.stopPropagation();
    });

    const inputfield = [$(".sidebar-makeTeam-name-box"), $(".sidebar-makeTeam-description-box")];
    const button = $(".sidebar-makeTeam-submit-button");
    checkValidInput(inputfield, button);

}

function initClickEvent() {

    $(".sidebar-makeTeam-button").addEventListener("click", (evt) => {
        evt.preventDefault();
        if($(".sidebar-makeTeam-box").style.display === 'none') {
            $(".sidebar-makeTeam-box").style.display = 'block';
            $(".sidebar-makeTeam-name-box").focus();
        } else {
            $(".sidebar-makeTeam-box").style.display = 'none';
        }
    })

    $(".sidebar-makeTeam-title>span").addEventListener("click", (evt)=> {
        $(".sidebar-makeTeam-box").style.display = 'none';
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
    console.log("drawTeams");
    let html = "";
    const template = Handlebars.templates["precompile/sidebar_template"];
    for(team of result) {
        html  += template(team);
    }
    $(".sidebar-team-list").innerHTML += html;
    selectTeam();
}

function createTeam() {
    $(".sidebar-makeTeam-submit-button").addEventListener("click", (evt)=>{
        evt.preventDefault();
        const postObject = {
            "name": $_value(".sidebar-makeTeam-name-box"),
            "description": $_value(".sidebar-makeTeam-description-box")
        };

        console.log("submit");

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
        $(".sidebar-makeTeam-container .sidebar-makeTeam-box").style.display = 'none';
        $(".sidebar-makeTeam-name-box").value = "";
        $(".sidebar-makeTeam-description-box").value = "";

        const template = Handlebars.templates["precompile/sidebar_template"];
        $(".sidebar-team-list").innerHTML += template(result.team);
        boardSummary.drawTeamBoards(result);

    } else {
        result.forEach(function(result){
            $(".sidebar-makeTeam-name-box").value = "";
            document.getElementsByName(result.errorType)[0].placeholder = result.message;
        });
    }

}

function selectTeam() {
    lists = $_all(".sidebar-team-list > li");
    for(let i = 0; i < lists.length ; i ++) {
        lists[i].addEventListener("click", (evt)=>{
            console.log(evt.target);


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
            console.log("false");
            return false;
        }
    }
    return true;
}