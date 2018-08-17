document.addEventListener("DOMContentLoaded", function(evt) {
    new BoardSummary($(".board-summary")).requestBoardSummary();
});


class BoardSummary {
    constructor(boardSummary) {
        this.node = boardSummary;
        this.recentlyBoardList = this.node.querySelector(".recent-board-list");
        this.teamBoardList = this.node.querySelector(".team-board-list");
    }

    requestBoardSummary() {
        fetchManager({
            url : "api/boards",
            method : "GET",
            headers : {"Content-type" : "application/json"},
            callback : this.drawBoardSummary.bind(this)
        })
    }

    drawBoardSummary(status,result) {
        this.drawRecentlyViewBoards(result.recentlyViewBoards);
        this.drawAllTeamBoards(result.boardOfTeamDtos);
        this.addCreateNewBoardEvent();
    }

    drawRecentlyViewBoards(recentlyViewBoards) {
        recentlyViewBoards.forEach((board) => {
            //TODO : 클래스로 뽑기.
            this.recentlyBoardList.appendChild(new BoardCard(board).getBoardNode());
        });
    }
    drawAllTeamBoards(teamBoards) {
        for(const teamBoard of teamBoards) {
            this.drawTeamBoards(teamBoard)
        };
    }
    drawTeamBoards(teamBoard) {
        const template = Handlebars.templates["precompile/team_boards_template"];
        this.teamBoardList.innerHTML += template(teamBoard.team);
        for(const board of teamBoard.boards) {
            const boardList = this.node.querySelector(".board-list");
            boardList.appendChild(new BoardCard(board).getBoardNode());
            boardList.appendChild(new BoardCard(board).getBoardNode());
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