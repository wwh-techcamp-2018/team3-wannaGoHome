document.addEventListener("DOMContentLoaded", function(evt) {
    initHeader();
    new Notification();
});

function initHeader() {
    document.addEventListener("click", (evt)=>{
        $_(".header-button-boardlist").style.display = 'none';
    });
    $_(".header-button-boardlist").addEventListener("click", (evt)=>{
        evt.stopPropagation();
    });
    $_(".header-button").addEventListener("click", (evt) => {
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
    if($_(".header-button-boardlist").style.display === 'none') {
        $_(".header-button-boardlist").style.display = 'block';
    } else {
        $_(".header-button-boardlist").style.display = 'none';
    }
}

function drawRecentlyBoard(recentlyViewBoards) {
    const template = Handlebars.templates["precompile/header_board_template"];
    const recentlyBoardsNode = $_(".header-recently-boards");
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
    const headerTeamBoards = $_(".header-team-boards");
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
