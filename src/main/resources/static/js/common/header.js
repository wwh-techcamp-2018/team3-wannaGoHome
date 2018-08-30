document.addEventListener("DOMContentLoaded", function(evt) {
    initHeader();
    new Notification($_("header"));
});

function initHeader() {
    document.addEventListener("click", (evt)=>{
        $_(".header-button-boardlist").style.display = 'none';
    });
    $_(".header-button-boardlist").addEventListener("click", (evt)=>{
        evt.stopPropagation();
    });

    $_(".header-profile-button").addEventListener("click", (evt)=>{
        window.location.href = "/users/profile";
    });

    $_(".header-button").addEventListener("click", (evt) => {
        if($_(".header-button-boardlist").style.display !== 'none') {
            return;
        }
        fetchManager({
            url: "/api/boards",
            method: "GET",
            callback: drawBoardSummary
        });

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
    const title = '<li class="header-button-boardlist-title"><i class="far fa-clock"></i>Recent Boards</li>';
    recentlyBoardsNode.appendChild(createElementFromHTML(title));
    for(const board of recentlyViewBoards) {
        const boardNode = createElementFromHTML(template(board));
        recentlyBoardsNode.appendChild(boardNode);
        addBoardClickEvent(boardNode);
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
                const boardNode = createElementFromHTML(boardtemplate(board));
                teamBoardNode.appendChild(boardNode);
                addBoardClickEvent(boardNode);

            }
            headerTeamBoards.appendChild(teamBoardNode);
        }
    }
}

function addBoardClickEvent(board) {
    board.addEventListener("click", (evt)=>{
       evt.stopPropagation();
       window.location.href = `/board/${board.getAttribute("data-id")}`;
    });
}