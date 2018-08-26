class CardDetail {
    constructor() {
        this.cardId = null;

        this.form = $_("#card-detail");
        this.commentText = this.form.querySelector(".card-detail-comment");
        this.descriptionText = this.form.querySelector(".card-detail-desc");

        this.commentListContainer = this.form.querySelector(".card-detail-comment-list-container");
        this.assigneeContainer = this.form.querySelector(".card-detail-assignee-container");
        this.assigneeListContainer = this.form.querySelector(".card-detail-assignee-list-container");
        this.assigneeSearchBox = this.form.querySelector(".card-detail-assignee-search");

        this.labelListContainer = this.form.querySelector(".card-detail-label-list");
        this.labelContainer = this.form.querySelector(".card-detail-label-container");

        this.commentTemplate = Handlebars.templates["precompile/board/card_list_comment_template"];
        this.labelTemplate = Handlebars.templates["precompile/board/card_label_template"];
        this.assigneeTemplate = Handlebars.templates["precompile/board/card_assignee_item_template"]

        this.form.querySelector(".card-comment-save-button").addEventListener("click", this.addComment.bind(this));
        this.form.querySelector(".card-detail-side-button.label").addEventListener("click", this.toggleLabels.bind(this));
        this.form.querySelector(".card-assignee").addEventListener("click", this.handleAssigneeButton.bind(this));

        this.labelContainer.addEventListener("click", (evt)=> evt.stopPropagation());

        this.assigneeContainer.addEventListener("click", (evt) => evt.stopPropagation());
        this.assigneeListContainer.addEventListener("click", this.handleUserAssign.bind(this));
        this.assigneeSearchBox.addEventListener("input", this.handleAssigneeSearch.bind(this));
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

    get assigneeSearchKeyword() {
        return this.assigneeSearchBox.value;
    }

    handleAssigneeSearch() {
        fetchManager({
            url: `/api/cards/${this.cardId}/members?keyword=` + encodeURI(this.assigneeSearchKeyword),
            method: "GET",
            callback: this.handleBoardMemberSearch.bind(this)
        })
    }

    handleAssigneeButton() {
        if (this.assigneeContainer.classList.contains("card-detail-assignee-container-hide")) {
            fetchManager({
                url: `/api/cards/${this.cardId}/members?keyword=` + encodeURI(this.assigneeSearchKeyword),
                method: "GET",
                callback: this.handleBoardMemberSearch.bind(this)
            })
        }
        else {
            this.hideBoardMembers();
        }
    }

    handleUserAssign(evt) {
        let userLi;
        if (evt.target.tagName === "LI") {
            userLi = evt.target;
        }
        else if (evt.target.tagName === "IMG" || evt.target.tagName === "SPAN") {
            userLi = evt.target.parentNode;
        }

        const userId = userLi.getAttribute("data-id");
        const method = userLi.getAttribute("data-assigned") === "true" ? "DELETE" : "POST";

        fetchManager({
            url: `/api/cards/${this.cardId}/assign`,
            method: method,
            body: JSON.stringify({userId: userId}),
            callback: this.toggleAssignee.bind(this)
        })
    }

    toggleAssignee(status, members) {
        if (status !== 200) {
            return;
        }

        this.showBoardMembers(members);
    }

    handleBoardMemberSearch(status, members) {
        if (status === 200) {
            this.showBoardMembers(members);
        }
    }

    showBoardMembers(members) {
        this.assigneeContainer.classList.remove("card-detail-assignee-container-hide");
        this.assigneeListContainer.innerHTML = "";
        members.forEach((member) => {
            const assigneeItem = createElementFromHTML(this.assigneeTemplate(member));
            const assigneeCheck = assigneeItem.querySelector(".assignee-check");
            if (member.assigned) {
                assigneeCheck.classList.remove("assignee-check-hide");
            }
            else {
                assigneeCheck.classList.add("assignee-check-hide");
            }

            this.assigneeListContainer.appendChild(assigneeItem);
        });
    }

    hideBoardMembers() {
        this.assigneeContainer.classList.add("card-detail-assignee-container-hide")
    }

    hide() {
        this.labelContainer.style.display = 'none';
        this.form.style.display = "none";
        this.cardId = null;
        this.commentText.value = "";
        this.descriptionText.value = "";
        this.commentListContainer.innerHTML = "";
    }
}