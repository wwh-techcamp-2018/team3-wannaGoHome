class BoardCard {
    constructor(board) {
        this.board = this.createNodeByHtmlText(this.getBoardTemplate(board));
        this.addClickEvent();
    }

    getBoardTemplate(board) {
        const template = Handlebars.templates["precompile/board_template"];
        return template(board);
    }

    addClickEvent() {
        this.board.addEventListener("click", (evt) => {
            evt.preventDefault();
        })
    }

    getBoardNode() {
        return this.board;
    }

    createNodeByHtmlText(htmlString) {
        let div = document.createElement("p");
        div.innerHTML = htmlString;
        return div.firstChild;
    }
}