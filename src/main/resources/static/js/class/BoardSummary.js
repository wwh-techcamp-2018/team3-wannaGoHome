document.addEventListener("DOMContentLoaded", function(evt) {
    new BoardSummary($(".board-summary")).requestBoardSummary();
});


class BoardSummary {
    constructor(boardSummary) {
        this.node = boardSummary;
        this.recentlyBoardList = this.node.querySelector(".recent-board-list");
        this.makeTeamContainer = this.node.querySelector(".makeTeam-container");
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
        this.drawAllTeamBoards(result.boardOfTeamDtos);
        this.addCreateNewBoardEvent();
    }

    drawRecentlyViewBoards(recentlyViewBoards) {
        recentlyViewBoards.forEach((board) => {
            this.recentlyBoardList.innerHTML += this.getBoardTemplate(board);
        });
    }
    drawAllTeamBoards(teamBoards) {
        for(const teamBoard of teamBoards) {
            this.drawTeamBoards(teamBoard)
        };
    }
    drawTeamBoards(teamBoard) {
        const template = Handlebars.templates["precompile/team_boards_template"];
        this.makeTeamContainer.insertAdjacentHTML("beforebegin", template(teamBoard.team));
        for(const board of teamBoard.boards) {
            const createBoard = this.node.querySelector(".board-card.create-board-card");
            createBoard.insertAdjacentHTML("beforebegin",this.getBoardTemplate(board));
        }
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