document.addEventListener("DOMContentLoaded", function(evt) {
    init();
    console.log("init");

});

function init() {
    getTeams();
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
function getTeams() {
    fetchManager({
        url: "/api/teams",
        method: "GET",
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