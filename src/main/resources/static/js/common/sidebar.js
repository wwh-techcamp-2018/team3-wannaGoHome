document.addEventListener("DOMContentLoaded", function(evt) {
    init();
    console.log("init");

});

function init() {
    getTeams();
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