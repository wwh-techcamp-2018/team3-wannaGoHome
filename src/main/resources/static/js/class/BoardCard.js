class BoardCard {
    constructor(board) {
        this.board = createElementFromHTML(this.getBoardTemplate(board));
        this.addClickEvent();
    }

    getBoardTemplate(board) {
        const template = Handlebars.templates["precompile/board_template"];
        return template(board);
    }

    addClickEvent() {
        this.board.addEventListener("click", (evt) => {
            evt.preventDefault();
            const boardId = this.board.getAttribute("id").split("-")[1];
            window.location.href = "/board";
        })
    }

    getBoardNode() {
        return this.board;
    }

}