class CardDetail {
    constructor(boardId) {
        this.boardId = boardId;
        this.cardId = null;
        this.stompClient = null;

        this.form = $_("#card-detail");

        this.initCardDetailView();
        this.initSummaryView();
        this.initSideMenuView();
        this.initHandlebarTemplates();
    }

    initCardDetailView() {
        this.cardTitleText = this.selector(".card-detail-title-text");
        this.cardTitleEditText = this.selector(".card-detail-title-edit-text");
        this.taskTitleText = this.selector(".card-detail-task-title");
        this.descriptionText = this.selector(".card-detail-desc");
        this.descriptionShowBox = this.selector(".card-detail-description-show-box");
        this.descriptionEditBox = this.selector(".card-detail-description-edit-box");
        this.commentListContainer = this.selector(".card-detail-comment-list-container");
        this.commentText = this.selector(".card-detail-comment");
        this.deleteButton = this.selector(".card-detail-side-button.delete");
        this.descriptionSaveButton = this.selector(".card-detail-save-button");

        this.deleteButton.addEventListener("click", (evt) => {
            evt.stopPropagation();
            this.onClickDeleteButton();
        });

        this.descriptionText.addEventListener("input", (evt) => {
            checkValidInput(this.descriptionText, this.descriptionSaveButton);
        });

        this.cardTitleText.addEventListener("click", (evt) => {
            this.setCardTitleEditMode();
        });

        this.cardTitleEditText.addEventListener("keypress", function (evt) {
            if (!detectEnter(evt)) {
                return;
            }
            evt.preventDefault();
            evt.currentTarget.blur();
            this.onEnterKeyPress(evt);
        }.bind(this));

        limitInputSize(this.cardTitleEditText, 20);
        limitInputSize(this.descriptionText, 255);
        limitInputSize(this.commentText, 255);

        this.selector(".card-comment-save-button").addEventListener("click", (evt) => {
            if (checkNullInput(this.commentText)) {
                this.onClickAddCommentButton();
            }
        });
        this.selector(".card-detail-description-edit-button").addEventListener("click", this.onClickDescriptionModeButton.bind(this));
        this.descriptionSaveButton.addEventListener("click", this.onClickUpdateDescription.bind(this));
    }

    initSummaryView() {
        this.summaryWrapper = this.selector(".card-detail-summary-wrapper");
        this.memberSummary = this.summaryWrapper.querySelector(".card-detail-summary-members");
        this.labelSummary = this.summaryWrapper.querySelector(".card-detail-summary-labels");
        this.dueDateSummary = this.summaryWrapper.querySelector(".card-detail-summary-due-date");
        this.attachmentSummary = this.summaryWrapper.querySelector(".card-detail-summary-attachment-wrapper");
        this.attachmentSummaryTitle = this.attachmentSummary.querySelector(".card-detail-summary-attachment-title");
        this.attachmentSummaryList = this.attachmentSummary.querySelector(".card-detail-summary-attachment-list");
    }

    initSideMenuView() {
        this.assigneeContainer = this.selector(".card-detail-assignee-container");
        this.assigneeListContainer = this.selector(".card-detail-assignee-list-container");
        this.assigneeSearchBox = this.selector(".card-detail-assignee-search");

        this.labelListContainer = this.selector(".card-detail-label-list");
        this.labelContainer = this.selector(".card-detail-label-container");

        this.dueDateContainer = this.selector(".card-detail-date-container");

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

        this.selector("#card-detail-file-upload").addEventListener("input", (evt) => {
            this.onClickAttachmentButton(evt.target.files);
        });

        this.attachmentSummaryTitle.addEventListener("click", (evt) => {
            this.onClickLoadAttachments();
        });

        this.labelContainer.addEventListener("click", (evt) => evt.stopPropagation());
        this.assigneeContainer.addEventListener("click", (evt) => evt.stopPropagation());
        this.assigneeListContainer.addEventListener("click", this.onClickAssignee.bind(this));
        this.assigneeSearchBox.addEventListener("input", this.onChangeAssigneeSearch.bind(this));
        this.dueDateContainer.addEventListener("click", (evt) => evt.stopPropagation());
        this.attachmentSummary.addEventListener("click", (evt) => evt.stopPropagation());
    }

    initHandlebarTemplates() {
        this.commentTemplate = Handlebars.templates["precompile/board/card_list_comment_template"];
        this.labelTemplate = Handlebars.templates["precompile/board/card_label_template"];
        this.assigneeTemplate = Handlebars.templates["precompile/board/card_assignee_item_template"];
        this.labelSummaryTemplate = Handlebars.templates["precompile/board/card_detail_label_summary_template"];
        this.attachmentListTemplate = Handlebars.templates["precompile/board/card_detail_file_list_template"];
    }

    hideAllSidePopup() {
        $_(".card-detail-assignee-container").classList.add("card-detail-assignee-container-hide");
        $_(".card-detail-label-container").style.display = 'none';
        $_(".card-detail-date-container").style.display = 'none';
        this.attachmentSummaryList.style.display = 'none';
    }

    onClickAttachmentButton(files) {
        if (files.length === 0) {
            return;
        }
        fileFetchManager({
            url: `/api/cards/${this.cardId}/file`,
            body: getFileFormData(files),
            callback: this.handleAttachment.bind(this)
        })
    }

    onClickLoadAttachments() {
        if (this.attachmentSummaryList.style.display === 'none') {
            this.hideAllSidePopup();
            this.attachmentSummaryList.style.display = 'block';
        } else {
            this.attachmentSummaryList.style.display = 'none';
        }
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
        this.fetchCard({
            url: `/api/cards/${this.cardId}/comments`,
            method: "POST",
            body: JSON.stringify({contents: contents}),
            callback: this.handleAddComment.bind(this)
        });
    }

    onClickLabelButton() {
        if (this.labelContainer.style.display === 'none') {
            this.hideAllSidePopup();
            this.labelContainer.style.display = 'block';
        } else {
            this.labelContainer.style.display = 'none';
        }
    }

    onClickAssigneeButton() {
        if (this.assigneeContainer.classList.contains("card-detail-assignee-container-hide")) {
            this.hideAllSidePopup();
            this.fetchCard({
                url: `/api/cards/${this.cardId}/members?keyword=` + encodeURI(this.assigneeSearchKeyword),
                method: "GET",
                callback: this.handleUpdateAssignee.bind(this)
            })
        } else {
            this.hideBoardMembers();
        }


    }

    onClickUpdateDescription() {
        this.fetchCard({
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

        this.fetchCard({
            url: `/api/cards/${this.cardId}/assign`,
            method: method,
            body: JSON.stringify({userId: userId}),
            callback: this.handleUpdateAssignee.bind(this)
        })
    }

    onClickDueDateButton() {
        if (this.dueDateContainer.style.display === 'none') {
            this.hideAllSidePopup();
            this.dueDateContainer.style.display = 'block';
            this.dueDateContainer.querySelector("input").addEventListener("input", (evt) => {
                this.setDueDate(evt.target.value);
            })
        } else {
            this.dueDateContainer.style.display = 'none';
        }

    }

    onClickDeleteButton() {
        if (this.deleteButton.classList.contains("card-delete-button-danger")) {
            this.fetchCard({
                url: `/api/cards/${this.cardId}`,
                method: "DELETE",
                callback: this.handleDeleteCard.bind(this)
            })
        } else {
            this.deleteButton.classList.add("card-delete-button-danger");
        }
    }

    onClickDeleteDueDateButton() {
        this.fetchCard({
            url: `/api/cards/${this.cardId}/date`,
            method: "DELETE",
            callback: this.handleDeleteDueDate.bind(this)
        });
    }

    onChangeAssigneeSearch() {
        this.fetchCard({
            url: `/api/cards/${this.cardId}/members?keyword=` + encodeURI(this.assigneeSearchKeyword),
            method: "GET",
            callback: this.handleUpdateAssignee.bind(this)
        });
    }

    onEnterKeyPress(evt) {
        const cardTitle = evt.currentTarget.value;
        this.fetchCard({
            url: `/api/cards/${this.cardId}/title`,
            method: "PUT",
            body: JSON.stringify({cardTitle: cardTitle}),
            callback: this.handleCardTitleChange.bind(this)
        });
    }

    onClickAttachmentDeleteButton(fileId) {
        this.fetchCard({
            url: `/api/cards/${this.cardId}/file/${fileId}`,
            method: "DELETE",
            callback: this.handleDeleteAttachment.bind(this)
        })
    }

    handleDeleteAttachment(status, attachments) {
        if (status !== 200) {
            return;
        }
        this.drawAttachmentTitle(attachments);
    }

    handleAttachment(status, attachments) {
        if (status !== 201) {
            showDialog("파일 첨부 실패", attachments[0].message);
        } else {
            this.drawAttachmentTitle(attachments);
        }
        this.selector(".card-detail-file-upload-form").reset();
    }

    handleDueDate(status, card) {
        if (status !== 201) {
            return;
        }
        this.drawSummaryDueDate(card.endDate.slice(0, 10));
    }

    handleDeleteDueDate(status, card) {
        if (status !== 200) {
            return;
        }
        this.dueDateSummary.innerHTML = "";
        this.dueDateContainer.querySelector("input").value = "";
    }

    handleCardTitleChange(status, card) {
        this.cardTitleEditText.value = "";
        this.cardTitleText.innerText = card.cardTitle;
        this.setCardTitleNormalMode();
    }

    handleAddComment(status, comment) {
        if (status === 201) {
            this.commentText.value = "";
            checkValidInput(this.commentText, this.selector(".card-comment-save-button"));
            this.drawComment(comment);
        } else {
            this.drawErrorComment(comment);
        }
    }

    handleDeleteCard(status, card) {
        if (status !== 200) {
            return;
        }
        $_("#calendar").style.display = 'none';
        this.hide();
    }

    handleUpdateLabel(status, labels) {
        this.drawLabels(labels);
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
                this.fetchCard({
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

    drawSummaryDueDate(endDate) {
        this.dueDateSummary.innerHTML = "";
        this.dueDateSummary.appendChild(createElementFromHTML(`<span><i class="far fa-calendar-alt"></i> ${endDate} <i class="fas fa-times-circle date-delete-button"></i></span>`));
        this.dueDateSummary.querySelector(".date-delete-button").addEventListener("click", (evt) => {
            this.onClickDeleteDueDateButton();
        })
    }

    drawComments(comments) {
        this.commentListContainer.innerHTML = "";
        comments.forEach(this.drawComment.bind(this));
    }

    drawComment(comment) {
        this.commentListContainer.prepend(createElementFromHTML(this.commentTemplate(comment)));
        this.commentListContainer.scrollTop = 0;
    }

    drawErrorComment(error) {
        //TODO : 에러경우 처리하세요
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

    drawAttachmentTitle(attachments) {
        this.attachmentSummaryTitle.innerHTML = "";
        if (attachments.length !== 0) {
            this.attachmentSummaryTitle.appendChild(createElementFromHTML(`<span><i class="fas fa-file-upload"></i> ${attachments.length}개의 첨부 파일</span>`));
            this.attachmentSummaryList.innerHTML = "";
            this.drawAttachmentList(attachments);

        } else {
            this.attachmentSummaryList.style.display = 'none';
        }
        const fileList = this.attachmentSummaryList.querySelectorAll(".file-delete-button");
        for (let file of fileList) {
            file.addEventListener("click", (evt) => {
                this.onClickAttachmentDeleteButton(evt.target.closest("p").getAttribute("data-id"));
            })
        }
    }

    drawAttachmentList(attachments) {
        attachments.forEach(this.drawAttachment.bind(this));
    }

    drawAttachment(attachment) {
        this.attachmentSummaryList.appendChild(createElementFromHTML(this.attachmentListTemplate(attachment)));

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
        this.attachmentSummary.style.display = 'block';
        this.drawAttachmentTitle(body.attachments);
        checkValidInput(this.commentText, this.selector(".card-comment-save-button"));
        if (body.endDate) {
            const endDate = body.endDate.slice(0, 10);
            this.dueDateContainer.querySelector("input").value = endDate;
            this.drawSummaryDueDate(endDate);
        }
        else {
            this.dueDateSummary.innerHTML = "";
            this.dueDateContainer.querySelector("input").value = "";
        }
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

        this.fetchCard({
            url: "/api/cards/" + this.cardId,
            method: "GET",
            callback: this.handleInitCardForm.bind(this)
        });
    }

    hide() {
        this.assigneeContainer.classList.add("card-detail-assignee-container-hide");
        this.dueDateContainer.style.display = 'none';
        this.labelContainer.style.display = 'none';
        this.form.style.display = "none";
        this.cardId = null;
        this.commentText.value = "";
        this.descriptionText.value = "";
        this.commentListContainer.innerHTML = "";
        this.attachmentSummaryList.innerHTML = "";
        this.attachmentSummaryList.style.display = 'none';
        this.deleteButton.classList.remove("card-delete-button-danger");
        checkValidInput(this.descriptionText, this.descriptionSaveButton);
        this.setCardTitleNormalMode();
        this.setDescriptionNormalMode();
    }

    setClient(client) {
        this.stompClient = client;
    }

    setCardTitleEditMode() {
        this.cardTitleEditText.focus();
        this.cardTitleText.classList.add("card-detail-title-hide");
        this.cardTitleEditText.classList.remove("card-detail-title-hide");
        this.cardTitleEditText.value = this.cardTitleText.innerText;
    }

    setCardTitleNormalMode() {
        this.cardTitleText.classList.remove("card-detail-title-hide");
        this.cardTitleEditText.classList.add("card-detail-title-hide");
    }

    setDescriptionEditMode() {
        this.descriptionShowBox.classList.add("card-detail-description-hide");
        this.descriptionEditBox.classList.remove("card-detail-description-hide");

        this.descriptionText.value = this.descriptionShowBox.innerText;
        checkValidInput(this.descriptionText, this.descriptionSaveButton);
    }

    setDescriptionNormalMode() {
        this.descriptionShowBox.classList.remove("card-detail-description-hide");
        this.descriptionEditBox.classList.add("card-detail-description-hide");
    }

    setDueDate(date) {
        const cardDetailDto = {
            endDate: date
        };
        this.fetchCard({
            url: `/api/cards/${this.cardId}/date`,
            method: "POST",
            body: JSON.stringify(cardDetailDto),
            callback: this.handleDueDate.bind(this)
        });
    }

    fetchCard({url, method, body, callback}) {
        fetchManager({
            url: url,
            method: method,
            body: body,
            callback: (status, body) => {
                if (status === 400 && body.filter((error) => error.errorType === "cardId").length !== 0) {
                    showDialog("Deleted", "삭제된 카드입니다.");
                    this.exit();
                    return;
                }
                callback(status, body);
            }
        });
    }


    get assigneeSearchKeyword() {
        return this.assigneeSearchBox.value;
    }

    exit() {
        document.querySelector("body").click();
    }

    selector(selector) {
        return this.form.querySelector(selector);
    }
}