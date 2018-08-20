document.addEventListener("DOMContentLoaded", function(evt) {
    initHeader();
});

function initHeader() {
    document.addEventListener("click", (evt)=>{
        $(".header-button-boardlist").style.display = 'none';
    });
    $(".header-button-boardlist").addEventListener("click", (evt)=>{
        evt.stopPropagation();
    });
    $(".header-button").addEventListener("click", (evt) => {
        fetchManager({
            url: "api/boards",
            method: "GET",
            callback: drawBoardSummary
        });

        console.log("click");
    });
}

function drawBoardSummary(status, result) {

    drawRecentlyBoard(result.recentlyViewBoards);
    drawTeamBoard(result.boardOfTeamDtos);
    if($(".header-button-boardlist").style.display === 'none') {
        $(".header-button-boardlist").style.display = 'block';
    } else {
        $(".header-button-boardlist").style.display = 'none';
    }
}

function drawRecentlyBoard(recentlyViewBoards) {
    const template = Handlebars.templates["precompile/header_board_template"];
    const recentlyBoardsNode = $(".header-recently-boards");
    recentlyBoardsNode.innerHTML = "";
    const title = '<li class="header-button-boardlist-title"><i class="fa fa-clock-o"></i> RECENT BOARD</li>'
    recentlyBoardsNode.appendChild(createElementFromHTML(title));
    for(const board of recentlyViewBoards) {
        recentlyBoardsNode.appendChild(createElementFromHTML(template(board)));
    }
}

function drawTeamBoard(teamBoards) {
    const template = Handlebars.templates["precompile/header_team_template"];
    const boardtemplate = Handlebars.templates["precompile/header_board_template"];
    const headerTeamBoards = $(".header-team-boards");
    headerTeamBoards.innerHTML = "";
    for(const teamBoard of teamBoards) {
        const teamBoardNode = createElementFromHTML(template(teamBoard.team));
        if (teamBoard.boards.length != 0) {
            for (const board of teamBoard.boards) {
                teamBoardNode.appendChild(createElementFromHTML(boardtemplate(board)));
            }
            headerTeamBoards.appendChild(teamBoardNode);
        }
    }
}
