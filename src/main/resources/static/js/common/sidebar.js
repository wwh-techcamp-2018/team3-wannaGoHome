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
        $(".sidebar-team-list").innerHTML += template(result);

    } else {
        result.errors.forEach(function(error){
            document.getElementsByName(error.field)[0].placeholder = error.defaultMessage;
        });
    }

}