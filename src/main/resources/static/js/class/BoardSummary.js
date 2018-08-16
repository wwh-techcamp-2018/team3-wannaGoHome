document.addEventListener("DOMContentLoaded", function(evt) {
    new BoardSummary($(".board-summary"));
});


class BoardSummary {
    constructor(boardSummary) {
        this.boardSummary = boardSummary;
    }

    requestBoardSummary() {
        getManager()
    }
}