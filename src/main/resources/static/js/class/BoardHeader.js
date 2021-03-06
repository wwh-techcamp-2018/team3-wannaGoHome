class BoardHeader {
    constructor(header, boardId) {
        this.boardId = boardId;

        const optionTemplate = Handlebars.templates["precompile/board/header/option_container_template"];

        this.boardTitle = header.querySelector(".board-header-title");
        this.boardTitleEditor = header.querySelector(".board-header-title-input");
        this.teamTitle = header.querySelector(".board-header-team-title");
        this.boardHeaderMemberContainer = header.querySelector(".board-header-team-member");
        this.boardHeaderMenuButton = header.querySelector(".board-header-menu-button");
        this.boardRemoveButton = header.querySelector(".board-header-remove-button");

        this.boardOptionHolder = createElementFromHTML(optionTemplate());
        header.appendChild(this.boardOptionHolder);

        this.boardHeaderMenuButton.addEventListener("click", this.onClickBoardOptionButton.bind(this));
        this.boardTitle.addEventListener("click", this.onClickBoardTitle.bind(this));
        this.boardTitleEditor.addEventListener("keypress", this.onChangeBoardTitle.bind(this));
        this.boardOptionHolder.addEventListener("click", (evt) => evt.stopPropagation());

        limitInputSize(this.boardTitleEditor, 20);
    }

    onClickBoardTitle(evt) {
        evt.stopPropagation();
        if (this.isAdminPermission()) {
            this.showBoardRenameOption(evt);
        }
    }

    onClickBoardRemoveButton() {
        fetchManager({
            url: `/api/boards/${this.boardId}`,
            method: "DELETE",
            callback: this.handleBoardRemove.bind(this)
        });
    }

    onClickBoardOptionButton(evt) {
        $_("body").click();
        evt.stopPropagation();
        this.showMenuOptions(evt)
    }

    onChangeBoardTitle(evt) {
        if (!detectEnter(evt)) {
            return;
        }

        fetchManager({
            url: `/api/boards/${this.boardId}/name`,
            method: "PUT",
            body: JSON.stringify({boardTitle: this.boardTitleEditor.value}),
            callback: this.handleBoardRename.bind(this)
        });
    }

    handleBoardRemove(status) {
        if (status === 200) {
            window.location.href = "/";
        }
    }

    handleBoardRename(status) {
        if (status === 200) {
            this.hideBoardRenameOption();
            return;
        }

        // TODO: 실패시 처리
    }

    setBoardHeader(boardHeader) {
        if (boardHeader.deleted) {
            showDialog("Alert", "Board is deleted by someone", () => {
                window.location.href = "/";
            });
        }

        this.permission = boardHeader.permission;

        addEscapedText(this.boardTitle, boardHeader.boardTitle);
        addEscapedText(this.teamTitle, boardHeader.teamTitle);
        this.teamTitle.addEventListener("click", (evt) => {
            window.location.href = "/team/" + boardHeader.teamId
        });
        this.drawBoardHeaderMembers(boardHeader.members);

        if (!this.isAdminPermission()) {
            this.boardHeaderMenuButton.remove();
        }

        $_("body").style.backgroundColor = boardHeader.color;
    }

    isAdminPermission() {
        return this.permission === "Admin";
    }

    drawBoardHeaderMembers(members) {
        if (members === null) {
            return;
        }
        this.boardHeaderMemberContainer.innerHTML = "";
        members.forEach((member) => {
            const html = `<div class="board-header-team-member-holder">
                            <img src="${member.profile}" alt="${member.name}">
                          </div>`;
            const imageNode = createElementFromHTML(html)
            this.boardHeaderMemberContainer.appendChild(imageNode);

            imageNode.querySelector("img").addEventListener("load", function (evt) {
                if (imageDimensions(evt.currentTarget)) {
                    evt.currentTarget.classList.toggle("profile-image-wide")
                } else {
                    evt.currentTarget.classList.toggle("profile-image-long")
                }
                evt.currentTarget.style.display = "inline-block";
            });
        });
    }

    showOption(evt) {
        this.boardOptionHolder.style.display = "block";

        this.boardOptionHolder.style.left = evt.clientX + "px";
        this.boardOptionHolder.style.top = evt.clientY + "px";
    }

    showBoardRenameOption() {
        this.boardTitleEditor.classList.remove("board-header-title-hide");
        this.boardTitle.classList.add("board-header-title-hide");

        this.boardTitleEditor.value = this.boardTitle.innerText;
        this.boardTitleEditor.focus();
    }

    hideBoardRenameOption() {
        this.boardTitleEditor.classList.add("board-header-title-hide");
        this.boardTitle.classList.remove("board-header-title-hide");

        this.boardTitleEditor.value = "";
    }

    showMenuOptions(evt) {
        this.showOption(evt);
        this.boardOptionHolder.querySelector(".board-options-title").innerText = "Delete Board";

        const optionContainer = this.boardOptionHolder.querySelector(".board-options-container");
        const deleteButton = createElementFromHTML("<li class='board-options-item'>Delete this Board!</li>");
        optionContainer.innerHTML = "";
        optionContainer.appendChild(deleteButton);

        deleteButton.addEventListener("click", this.onClickBoardRemoveButton.bind(this));
    }

    hideOption() {
        this.boardOptionHolder.style.display = "none";
    }
}