document.addEventListener("DOMContentLoaded", function(evt) {
    init();
    console.log("init");

});

function init() {
    getTeams();
    initCreateTeamEvent();
}
function initCreateTeamEvent() {

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

    $(".sidebar-team-list").innerHTML = html;

}