document.addEventListener("DOMContentLoaded", function(evt) {
    new BoardSummary($(".board-summary")).requestBoardSummary();
});


class BoardSummary {
    constructor(boardSummary) {
        this.node = boardSummary;
    }

    requestBoardSummary() {
        getManager({
            url : "api/boards",
            method : "GET",
            headers : {"Content-type" : "application/json"},
            callback : this.drawBoardSummary.bind(this)
        })
    }

    drawBoardSummary(result) {
        this.drawRecentlyViewBoards(result.recentlyViewBoards);
        this.drawTeamBoards(result.boardOfTeamDtos);
        this.addCreateNewBoardEvent();
    }

    drawRecentlyViewBoards(recentlyViewBoards) {
        const recentlyBoardList = this.node.querySelector(".recent-board-list");
        recentlyViewBoards.forEach((board) => {
            recentlyBoardList.innerHTML += this.getBoardTemplate(board);
        });
    }
    drawTeamBoards(teamBoards) {
        const makeTeamContainer = this.node.querySelector(".makeTeam-container");
        const template = Handlebars.templates["precompile/team_boards_template"];
        for(const teamBoard of teamBoards) {
            makeTeamContainer.insertAdjacentHTML("beforebegin", template(teamBoard.team));
            for(const board of teamBoard.boards) {
                const createBoard = this.node.querySelector(".board-card.create-board-card");
                createBoard.insertAdjacentHTML("beforebegin",this.getBoardTemplate(board));
            }
        };
    }
    getBoardTemplate(board) {
        const template = Handlebars.templates["precompile/board_template"];
        return template(board);
    }

    addCreateNewBoardEvent() {
        const createNewBoard = this.node.querySelector(".board-card.create-board-card");
        createNewBoard.addEventListener("click",(evt) => {
            evt.preventDefault();
            console.log(evt.target);
        });
    }
}