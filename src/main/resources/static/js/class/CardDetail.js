class CardDetail {
    constructor(boardId) {
        this.smallCalendar;

        this.boardId = boardId;
        this.cardId = null;
        this.stompClient = null;

        this.form = $_("#card-detail");
        this.commentText = this.form.querySelector(".card-detail-comment");
        this.descriptionText = this.form.querySelector(".card-detail-desc");

        this.cardTitleText = this.form.querySelector(".card-detail-title-text");
        this.taskTitleText = this.form.querySelector(".card-detail-task-title");

        this.commentListContainer = this.form.querySelector(".card-detail-comment-list-container");
        this.assigneeContainer = this.form.querySelector(".card-detail-assignee-container");
        this.assigneeListContainer = this.form.querySelector(".card-detail-assignee-list-container");
        this.assigneeSearchBox = this.form.querySelector(".card-detail-assignee-search");
        this.descriptionShowBox = this.form.querySelector(".card-detail-description-show-box");
        this.descriptionEditBox = this.form.querySelector(".card-detail-description-edit-box");

        this.labelListContainer = this.form.querySelector(".card-detail-label-list");
        this.labelContainer = this.form.querySelector(".card-detail-label-container");

        this.deleteButton = this.form.querySelector(".card-detail-side-button.delete");
        this.commentTemplate = Handlebars.templates["precompile/board/card_list_comment_template"];
        this.labelTemplate = Handlebars.templates["precompile/board/card_label_template"];
        this.assigneeTemplate = Handlebars.templates["precompile/board/card_assignee_item_template"];

        this.deleteButton.addEventListener("click", (evt)=>{
            evt.stopPropagation();
            this.handleDeleteButton();

        });
        this.form.querySelector(".card-comment-save-button").addEventListener("click", this.addComment.bind(this));

        this.form.querySelector(".card-detail-side-button.label").addEventListener("click", (evt) =>{
            evt.stopPropagation();
            this.toggleLabels();
        });
        this.form.querySelector(".card-assignee").addEventListener("click", (evt)=> {
            evt.stopPropagation();
            this.handleAssigneeButton();
        });
        this.form.querySelector(".card-detail-side-button.due-date").addEventListener("click", (evt)=>{
            evt.stopPropagation();
            this.handleCalendar();
        });
        this.form.querySelector(".card-detail-description-edit-button").addEventListener("click", (evt) => {
            if (this.descriptionShowBox.classList.contains("card-detail-description-hide")) {
                this.setDescriptionNormalMode();
            }
            else {
                this.setDescriptionEditMode();
            }
        });

        this.form.querySelector(".card-detail-save-button").addEventListener("click", this.onClickUpdateDescription.bind(this));

        this.labelContainer.addEventListener("click", (evt)=> evt.stopPropagation());

        this.assigneeContainer.addEventListener("click", (evt) => evt.stopPropagation());
        this.assigneeListContainer.addEventListener("click", this.handleUserAssign.bind(this));
        this.assigneeSearchBox.addEventListener("input", this.handleAssigneeSearch.bind(this));
    }

    setClient(client) {
        this.stompClient = client;
    }

    setDueDate(date) {
        console.log(date);
        let mm = (date.getMonth()+1 > 9 ? '' : '0') +(date.getMonth()+1);
        let dd =(date.getDate() > 9 ? '' : '0') +date.getDate();
        const endDate = date.getFullYear() +"-" + mm + "-" + dd;
        const cardDetailDto = {
            endDate: endDate
        };
        fetchManager({
            url: `/api/cards/${this.cardId}/date`,
            method: "POST",
            body: JSON.stringify(cardDetailDto),
            callback: this.handleDueDate.bind(this)
        });
    }

    handleDueDate(status, card) {
        if(status !== 201) {
            return;
        }
        console.log(card.createDate, card.endDate);
        this.smallCalendar.drawDueDate(card);
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
            if(value.checked) {
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

    drawCardForm(status, body) {
        if (status !== 200) {
            return;
        }

        this.cardTitleText.innerText = body.cardTitle;
        this.taskTitleText.innerText = body.taskTitle;
        this.descriptionShowBox.innerText = body.description;
        body.comments.forEach(this.drawComment.bind(this));
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

    onClickUpdateDescription() {
        fetchManager({
            url: `/api/cards/${this.cardId}/description`,
            method: "POST",
            body: JSON.stringify({description: this.descriptionText.value}),
            callback: this.handleUpdateDescription.bind(this)
        })
    }

    handleUpdateDescription(status, body) {
        console.log("handleUpdateDescription is called");
        console.log(body);
        this.descriptionShowBox.innerText = body.description;
        this.setDescriptionNormalMode();
    }

    setDescriptionEditMode() {
        this.descriptionShowBox.classList.add("card-detail-description-hide");
        this.descriptionEditBox.classList.remove("card-detail-description-hide");

        this.descriptionText.value = this.descriptionShowBox.innerText;
    }

    setDescriptionNormalMode() {
        this.descriptionShowBox.classList.remove("card-detail-description-hide");
        this.descriptionEditBox.classList.add("card-detail-description-hide");
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
        $_("#smallCalendar").style.display = 'none';
        this.deleteButton.classList.remove("card-delete-button-danger");
    }

    handleCalendar() {
        if(this.form.querySelector("#smallCalendar").style.display === 'none'){
            this.form.querySelector("#smallCalendar").style.display = 'block';
            this.smallCalendar.createSchedule();
        } else {
            // this.form.querySelector("#smallCalendar").style.display = 'none';
        }

    }

    handleDeleteButton() {
        if(this.deleteButton.classList.contains("card-delete-button-danger")) {
            fetchManager({
                url: `/api/cards/${this.cardId}`,
                method: "DELETE",
                callback: this.onDeleteCard.bind(this)
            })
        } else {
            this.deleteButton.classList.add("card-delete-button-danger");
        }
    }

    onDeleteCard(status, card) {
        if (status !== 200) {
            return;
        }
        this.hide();
        this.refreshBoard();
    }

    refreshBoard() {
        this.stompClient.send(`/app/message/board/${this.boardId}`);
    }
}