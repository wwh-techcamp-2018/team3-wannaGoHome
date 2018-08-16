document.addEventListener("DOMContentLoaded", function(evt) {
    init();
    console.log("init");

});

function init() {
    getTeams();
    initClickEvent();
    createTeam();
}
function initClickEvent() {

    $(".sidebar-makeTeam-container .sidebar-makeTeam-button").addEventListener("click", (evt) => {
        console.log("click");
        if($(".sidebar-makeTeam-container .sidebar-makeTeam-box").style.display === 'none') {
            $(".sidebar-makeTeam-container .sidebar-makeTeam-box").style.display = 'block';
        } else {
            $(".sidebar-makeTeam-container .sidebar-makeTeam-box").style.display = 'none';
        }
    })

    $(".sidebar-makeTeam-container .sidebar-makeTeam-title>span").addEventListener("click", (evt)=> {
        console.log("x click");
        $(".sidebar-makeTeam-container .sidebar-makeTeam-box").style.display = 'none';
    })

    $(".makeTeam-container .create-team-button").addEventListener("click", (evt) => {
        console.log("click");
        if($(".makeTeam-container .sidebar-makeTeam-box").style.display === 'none') {
            $(".makeTeam-container .sidebar-makeTeam-box").style.display = 'block';
        } else {
            $(".makeTeam-container .sidebar-makeTeam-box").style.display = 'none';
        }
    })

    $(".makeTeam-container .sidebar-makeTeam-title>span").addEventListener("click", (evt)=> {
        console.log("x click");
        $(".makeTeam-container .sidebar-makeTeam-box").style.display = 'none';
    })
    
}
function getTeams() {
    console.log("getTeams");
    getManager({
        url: "/api/teams",
        method: "GET",
        headers: {"Content-type": "application/json"},
        callback : drawTeams
    });
}

function drawTeams(result) {
    console.log("drawTeams");
    let html = "";
    const template = Handlebars.templates["precompile/sidebar_template"];
    for(team of result) {
       html  += template(team);
    }

    $(".sidebar-team-list").innerHTML += html;

}

function createTeam() {
    $(".sidebar-makeTeam-submit-button").addEventListener("click", (evt)=>{
        const postObject = {
            "name": $_value(".sidebar-makeTeam-name-box"),
            "description": $_value(".sidebar-makeTeam-description-box")
        };
        console.log("submit");
        $(".sidebar-makeTeam-container .sidebar-makeTeam-box").style.display = 'none';

        fetchManager({
            url: "/api/teams",
            method: "POST",
            headers: {"content-type": "application/json"},
            body: JSON.stringify(postObject),
            callback: displayTeam
        });
        $(".sidebar-makeTeam-name-box").innerText = "";
        $(".sidebar-makeTeam-description-box").innerText = "";
    })

}

function displayTeam(status, result) {
    console.log(status);
    console.log(result.name);
    console.log(result.description);
    const template = Handlebars.templates["precompile/sidebar_template"];
    $(".sidebar-team-list").innerHTML += template(result);

    // if(!result) {
    //     window.location.href = "";
    // }

    // let appendText = "";
    // $(".error-message-holder").innerHTML = ""
    // for(message of result.errors) {
    //     appendText += message.errorMessage + "<br />";
    //
    // }
    // $(".error-message-holder").innerHTML = appendText;

}