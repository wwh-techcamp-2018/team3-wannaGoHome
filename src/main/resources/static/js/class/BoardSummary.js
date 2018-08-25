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
            callback: this.drawBoardSummary.bind(this)
        })
    }

    drawBoardSummary(status, result) {
        this.drawRecentlyViewBoards(result.recentlyViewBoards);
        this.drawAllTeamBoards(result.boardOfTeamDtos);
    }

    drawRecentlyViewBoards(recentlyViewBoards) {
        recentlyViewBoards.forEach((board) => {
            this.recentlyBoardList.appendChild(new BoardCard(board).boardNode);
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
        const createBoardCard = teamBoardNode.querySelector(".create-board-card");
        this.addCreateNewBoardEvent(createBoardCard);
        for (const board of teamBoard.boards) {
            createBoardCard.insertAdjacentElement("beforebegin",new BoardCard(board).boardNode);
        }


    }

    addCreateNewBoardEvent(createBoardCard) {
        createBoardCard.addEventListener("click", (evt) => {
            evt.preventDefault();
            const teamId = evt.target.parentElement.getAttribute("data-id");
            this.createBoard.showCreateBoardForm(teamId);
        });

    }
}