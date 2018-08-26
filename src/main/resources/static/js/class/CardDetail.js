class CardDetail {
    constructor() {
        this.form = $_("#card-detail");
        this.commentText = this.form.querySelector(".card-detail-comment");
        this.descriptionText = this.form.querySelector(".card-detail-desc");
        this.commentListContainer = this.form.querySelector(".card-detail-comment-list-container");
        this.labelListContainer = this.form.querySelector(".card-detail-label-list");
        this.labelContainer = this.form.querySelector(".card-detail-label-container");

        this.commentTemplate = Handlebars.templates["precompile/board/card_list_comment_template"];
        this.labelTemplate = Handlebars.templates["precompile/board/card_label_template"];

        this.form.querySelector(".card-comment-save-button").addEventListener("click", this.addComment.bind(this));
        this.form.querySelector(".card-detail-side-button.label").addEventListener("click", this.toggleLabels.bind(this));
        this.labelContainer.addEventListener("click", (evt)=>{
            evt.stopPropagation();
        });

        // this.form.querySelector(".card-detail-side-button.due-date").addEventListener("click", this.addDueDate.bind(this));
        this.cardId;
    }

    addDueDate() {
        // this.form.querySelector("#card-detail-due-date-input").style.display = "block";
    }

    addLabel() {

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
        this.initLabel();
    }

    initLabel() {
        fetchManager({
            url: "/api/cards/"+this.cardId + "/label",
            method: "GET",
            callback: this.drawLabels.bind(this)
        })
    }

    drawLabels(status, labels) {
        if(status !== 200 && status !== 201) {
            return;
        }
        this.labelListContainer.innerHTML = "";
        labels.forEach(function(value) {
            let html = createElementFromHTML(this.labelTemplate(value));
            if(value.checked == true) {
                html.querySelector("i").style.display = "inline-block";
            }
            this.labelListContainer.append(html);
        }.bind(this));
        this.labelList = this.labelListContainer.querySelectorAll("li");
        this.handleLabels();
    }

    toggleLabels() {
        if(this.labelContainer.style.display === 'none') {
            this.labelContainer.style.display = 'block';
        } else {
            this.labelContainer.style.display = 'none';
        }
    }

    handleLabels() {
        for (let i = 0; i < this.labelList.length; i++) {
            this.labelList[i].addEventListener("click", (evt)=>{
                let method;
                if(this.labelList[i].querySelector("i").style.display === 'inline-block') {
                    method = "DELETE";
                } else {
                    method = "POST";
                }
                fetchManager({
                    url: "/api/cards/"+ this.cardId + "/label",
                    method: method,
                    body: JSON.stringify({id: evt.target.getAttribute("data-id")}),
                    callback: this.drawLabels.bind(this)
                })
            })
        }

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
        this.labelContainer.style.display = 'none';
        this.form.style.display = "none";
        this.cardId = undefined;
        this.commentText.value = "";
        this.descriptionText.value = "";
        this.commentListContainer.innerHTML = "";
    }
}