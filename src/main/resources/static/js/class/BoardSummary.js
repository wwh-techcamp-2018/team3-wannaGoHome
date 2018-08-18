document.addEventListener("DOMContentLoaded", function (evt) {
    new BoardSummary($(".board-summary")).requestBoardSummary();
});


class BoardSummary {
    constructor(boardSummary) {
        this.node = boardSummary;
        this.recentlyBoardList = this.node.querySelector(".recent-board-list");
        this.teamBoardList = this.node.querySelector(".team-board-list");
        this.createBoard = new CreateBoard(this.node);
    }

    requestBoardSummary() {
        fetchManager({
            url: "api/boards",
            method: "GET",
            headers: {"Content-type": "application/json"},
            callback: this.drawBoardSummary.bind(this)
        })
    }

    drawBoardSummary(status, result) {
        this.drawRecentlyViewBoards(result.recentlyViewBoards);
        this.drawAllTeamBoards(result.boardOfTeamDtos);
        this.addCreateNewBoardEvent();
    }

    drawRecentlyViewBoards(recentlyViewBoards) {
        recentlyViewBoards.forEach((board) => {
            this.recentlyBoardList.appendChild(new BoardCard(board).getBoardNode());
        });
    }

    drawAllTeamBoards(teamBoards) {
        for (const teamBoard of teamBoards) {
            this.drawTeamBoards(teamBoard)
        };
    }

    drawTeamBoards(teamBoard) {
        const template = Handlebars.templates["precompile/team_boards_template"];
        const teamBoardNode = createElementFromHTML(template(teamBoard.team));
        this.teamBoardList.appendChild(teamBoardNode);
        for (const board of teamBoard.boards) {
            const createBoardCard = teamBoardNode.querySelector(".create-board-card");
            createBoardCard.insertAdjacentElement("beforebegin",new BoardCard(board).getBoardNode());
        }
    }

    addCreateNewBoardEvent() {
        const createNewBoard = this.node.querySelectorAll(".board-card.create-board-card");
        createNewBoard.forEach((node) => {
            node.addEventListener("click", (evt) => {
                evt.preventDefault();
                const teamId = evt.target.parentElement.getAttribute("id").split("-")[1];
                this.createBoard.displayCreateBoardForm(teamId);
            });
        });
    }
}