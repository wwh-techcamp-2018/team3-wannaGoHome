class CardDetail {
    constructor(boardId) {
        this.boardId = boardId;
        this.cardId = null;
        this.stompClient = null;
        this.smallCalendar = null;

        this.form = $_("#card-detail");

        this.initCardDetailView();
        this.initSummaryView();
        this.initSideMenuView();
        this.initHandlebarTemplates();
    }

    initCardDetailView() {
        this.cardTitleText = this.selector(".card-detail-title-text");
        this.taskTitleText = this.selector(".card-detail-task-title");
        this.descriptionText = this.selector(".card-detail-desc");
        this.descriptionShowBox = this.selector(".card-detail-description-show-box");
        this.descriptionEditBox = this.selector(".card-detail-description-edit-box");
        this.commentListContainer = this.selector(".card-detail-comment-list-container");
        this.commentText = this.selector(".card-detail-comment");
        this.deleteButton = this.selector(".card-detail-side-button.delete");

        this.deleteButton.addEventListener("click", (evt) => {
            evt.stopPropagation();
            this.onClickDeleteButton();
        });

        this.selector(".card-comment-save-button").addEventListener("click", this.onClickAddCommentButton.bind(this));
        this.selector(".card-detail-description-edit-button").addEventListener("click", this.onClickDescriptionModeButton.bind(this));
        this.selector(".card-detail-save-button").addEventListener("click", this.onClickUpdateDescription.bind(this));
    }

    initSummaryView() {
        this.summaryWrapper = this.selector(".card-detail-summary-wrapper");
        this.memberSummary = this.summaryWrapper.querySelector(".card-detail-summary-members");
        this.labelSummary = this.summaryWrapper.querySelector(".card-detail-summary-labels");
        this.dueDateSummary = this.summaryWrapper.querySelector(".card-detail-summary-due-date");
    }

    initSideMenuView() {
        this.assigneeContainer = this.selector(".card-detail-assignee-container");
        this.assigneeListContainer = this.selector(".card-detail-assignee-list-container");
        this.assigneeSearchBox = this.selector(".card-detail-assignee-search");

        this.labelListContainer = this.selector(".card-detail-label-list");
        this.labelContainer = this.selector(".card-detail-label-container");

        this.selector(".card-detail-side-button.card-assignee").addEventListener("click", (evt) => {
            evt.stopPropagation();
            this.onClickAssigneeButton();
        });
        this.selector(".card-detail-side-button.due-date").addEventListener("click", (evt) => {
            evt.stopPropagation();
            this.onClickDueDateButton();
        });
        this.selector(".card-detail-side-button.label").addEventListener("click", (evt) => {
            evt.stopPropagation();
            this.onClickLabelButton();
        });

        this.labelContainer.addEventListener("click", (evt) => evt.stopPropagation());
        this.assigneeContainer.addEventListener("click", (evt) => evt.stopPropagation());
        this.assigneeListContainer.addEventListener("click", this.onClickAssignee.bind(this));
        this.assigneeSearchBox.addEventListener("input", this.onChangeAssigneeSearch.bind(this));
    }

    initHandlebarTemplates() {
        this.commentTemplate = Handlebars.templates["precompile/board/card_list_comment_template"];
        this.labelTemplate = Handlebars.templates["precompile/board/card_label_template"];
        this.assigneeTemplate = Handlebars.templates["precompile/board/card_assignee_item_template"];
        this.labelSummaryTemplate = Handlebars.templates["precompile/board/card_detail_label_summary_template"];
    }

    onClickDescriptionModeButton() {
        if (this.descriptionShowBox.classList.contains("card-detail-description-hide")) {
            this.setDescriptionNormalMode();
        }
        else {
            this.setDescriptionEditMode();
        }
    }

    onClickAddCommentButton() {
        const contents = this.commentText.value;
        fetchManager({
            url: `/api/cards/${this.cardId}/comments`,
            method: "POST",
            body: JSON.stringify({contents: contents}),
            callback: this.handleAddComment.bind(this)
        });
    }

    onClickLabelButton() {
        if (this.labelContainer.style.display === 'none') {
            this.labelContainer.style.display = 'block';
        } else {
            this.labelContainer.style.display = 'none';
        }
    }

    onClickAssigneeButton() {
        if (this.assigneeContainer.classList.contains("card-detail-assignee-container-hide")) {
            fetchManager({
                url: `/api/cards/${this.cardId}/members?keyword=` + encodeURI(this.assigneeSearchKeyword),
                method: "GET",
                callback: this.handleUpdateAssignee.bind(this)
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

    onClickAssignee(evt) {
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
            callback: this.handleUpdateAssignee.bind(this)
        })
    }

    onClickDueDateButton() {
        if (this.selector("#smallCalendar").style.display === 'none') {
            this.selector("#smallCalendar").style.display = 'block';
            this.smallCalendar.createSchedule();
        } else {
            // this.selector("#smallCalendar").style.display = 'none';
        }

    }

    onClickDeleteButton() {
        if (this.deleteButton.classList.contains("card-delete-button-danger")) {
            fetchManager({
                url: `/api/cards/${this.cardId}`,
                method: "DELETE",
                callback: this.handleDeleteCard.bind(this)
            })
        } else {
            this.deleteButton.classList.add("card-delete-button-danger");
        }
    }

    onChangeAssigneeSearch() {
        fetchManager({
            url: `/api/cards/${this.cardId}/members?keyword=` + encodeURI(this.assigneeSearchKeyword),
            method: "GET",
            callback: this.handleUpdateAssignee.bind(this)
        })
    }

    handleDueDate(status, card) {
        if (status !== 201) {
            return;
        }
        console.log(card.createDate, card.endDate);
        this.smallCalendar.drawDueDate(card);
    }

    handleAddComment(status, comment) {
        if (status === 201) {
            this.commentText.value = "";
            this.drawComment(comment);
        }
    }

    handleDeleteCard(status, card) {
        if (status !== 200) {
            return;
        }
        this.hide();
        this.refreshBoard();
    }

    handleUpdateLabel(status, labels) {
        this.drawLabels(labels);
        this.refreshBoard();
    }

    handleLabels() {
        for (let i = 0; i < this.labelList.length; i++) {
            this.labelList[i].addEventListener("click", (evt) => {
                let method;
                if (this.labelList[i].querySelector("i").style.display === 'inline-block') {
                    method = "DELETE";
                } else {
                    method = "POST";
                }
                fetchManager({
                    url: "/api/cards/" + this.cardId + "/label",
                    method: method,
                    body: JSON.stringify({id: evt.target.getAttribute("data-id")}),
                    callback: this.handleUpdateLabel.bind(this)
                })
            })
        }
    }

    handleInitCardForm(status, body) {
        if (status !== 200) {
            return;
        }

        this.drawCardForm(body)
    }

    handleUpdateDescription(status, body) {
        this.descriptionShowBox.innerText = body.description;
        this.setDescriptionNormalMode();
    }

    handleUpdateAssignee(status, members) {
        if (status !== 200) {
            return;
        }
        this.drawBoardMembers(members);
    }

    hideBoardMembers() {
        this.assigneeContainer.classList.add("card-detail-assignee-container-hide")
    }

    drawComments(comments) {
        this.commentListContainer.innerHTML = "";
        comments.forEach(this.drawComment.bind(this));
    }

    drawComment(comment) {
        this.commentListContainer.prepend(createElementFromHTML(this.commentTemplate(comment)));
        this.commentListContainer.scrollTop = 0;
    }

    drawSummaryLabels(labels) {
        this.labelSummary.innerHTML = "";
        labels.forEach(this.drawSummaryLabel.bind(this));
    }

    drawSummaryLabel(label) {
        this.labelSummary.appendChild(createElementFromHTML(this.labelSummaryTemplate(label)));
    }

    drawInitLabels(labels) {
        this.labelListContainer.innerHTML = "";
        this.labelSummary.innerHTML = "";
        labels.forEach(function (value) {
            let html = createElementFromHTML(this.labelTemplate(value));
            this.labelListContainer.append(html);
        }.bind(this));
        this.labelList = this.labelListContainer.querySelectorAll("li");
        this.handleLabels();
        this.refreshBoard();
    }

    drawLabels(labels) {
        const labelIdList = labels.map((label) => String(label.id));
        this.labelListContainer.childNodes.forEach((labelNode) => {
            const labelId = labelNode.getAttribute("data-id");
            if (labelIdList.includes(labelId)) {
                labelNode.firstElementChild.style.display = "inline-block";
            }
            else {
                labelNode.firstElementChild.style.display = "none";
            }
        });
        this.drawSummaryLabels(labels);
    }

    drawCardForm(body) {
        this.cardTitleText.innerText = body.cardTitle;
        this.taskTitleText.innerText = body.taskTitle;

        this.descriptionShowBox.innerText = body.description;

        this.drawInitLabels(body.allLabels);
        this.drawLabels(body.labels);
        this.drawSummaryLabels(body.labels);
        this.drawSummaryAssignees(body.assignees);
        this.drawComments(body.comments);
    }

    drawSummaryAssignees(assignees) {
        this.memberSummary.innerHTML = "";
        assignees.forEach(this.drawSummaryAssignee.bind(this));
    }

    drawSummaryAssignee(assignee) {
        const html = `<img src="${assignee.profile}" style="width:35px; height: 35px; border-radius: 50%;">`;
        this.memberSummary.appendChild(createElementFromHTML(html));
    }

    drawBoardMembers(members) {
        this.assigneeContainer.classList.remove("card-detail-assignee-container-hide");
        this.assigneeListContainer.innerHTML = "";
        this.memberSummary.innerHTML = "";
        members.forEach((member) => {
            const assigneeItem = createElementFromHTML(this.assigneeTemplate(member));
            const assigneeCheck = assigneeItem.querySelector(".assignee-check");
            if (member.assigned) {
                this.drawSummaryAssignee(member);
                assigneeCheck.classList.remove("assignee-check-hide");
            }
            else {
                assigneeCheck.classList.add("assignee-check-hide");
            }

            this.assigneeListContainer.appendChild(assigneeItem);
        });
    }

    show(cardId) {
        this.form.style.display = "block";
        this.cardId = cardId;

        fetchManager({
            url: "/api/cards/" + this.cardId,
            method: "GET",
            callback: this.handleInitCardForm.bind(this)
        });
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

    setClient(client) {
        this.stompClient = client;
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

    setDueDate(date) {
        console.log(date);
        let mm = (date.getMonth() + 1 > 9 ? '' : '0') + (date.getMonth() + 1);
        let dd = (date.getDate() > 9 ? '' : '0') + date.getDate();
        const endDate = date.getFullYear() + "-" + mm + "-" + dd;
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

    get assigneeSearchKeyword() {
        return this.assigneeSearchBox.value;
    }

    refreshBoard() {
        this.stompClient.send(`/app/message/board/${this.boardId}`);
    }

    selector(selector) {
        return this.form.querySelector(selector);
    }
}