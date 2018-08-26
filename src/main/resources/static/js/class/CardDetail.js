class CardDetail {
    constructor() {
        this.form = $_("#card-detail");
        this.commentText = this.form.querySelector(".card-detail-comment");
        this.descriptionText = this.form.querySelector(".card-detail-desc");
        this.commentListContainer = this.form.querySelector(".card-detail-comment-list-container");

        this.commentTemplate = Handlebars.templates["precompile/board/card_list_comment_template"];

        this.form.querySelector(".card-comment-save-button").addEventListener("click", this.addComment.bind(this));
        this.form.querySelector(".card-detail-side-button.due-date").addEventListener("click", this.addDueDate.bind(this));
        this.cardId;
    }

    addDueDate() {
        this.form.querySelector(".card-detail-due-date-input").style.display = "block";
        const picker = datepicker('.card-detail-due-date-input');
    }

    addComment() {
        const contents = this.commentText.value;
        fetchManager({
            url: `/api/cards/${this.cardId}/comments`,
            method: "POST",
            body: JSON.stringify({contents: contents}),
            callback: this.handleComment.bind(this)
        })
    }

    handleComment(status, comment) {
        if (status === 201) {
            this.commentText.value = "";
            this.drawComment(comment);
        }
    }

    drawComment(comment) {
        this.commentListContainer.prepend(createElementFromHTML(this.commentTemplate(comment)));
        this.commentListContainer.scrollTop = 0;
    }

    showCardDetailForm(cardId) {
        this.form.style.display = "block";
        this.cardId = cardId;

        this.initCardForm();
    }

    initCardForm() {
        fetchManager({
            url: "/api/cards/" + this.cardId,
            method: "GET",
            callback: this.drawCardForm.bind(this)
        });
    }

    drawCardForm(status, card) {
        if (status !== 200) {
            return;
        }
        this.descriptionText.value = card.description;
        card.comments.forEach(this.drawComment.bind(this));
    }

    hide() {
        this.form.style.display = "none";
        this.cardId = undefined;
        this.commentText.value = "";
        this.descriptionText.value = "";
        this.commentListContainer.innerHTML = "";
    }
}