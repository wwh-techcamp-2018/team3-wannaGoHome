class BoardHeader {
    constructor(header, boardId) {
        this.boardId = boardId;

        const optionTemplate = Handlebars.templates["precompile/board/header/option_container_template"];

        this.boardTitle = header.querySelector(".board-header-title");
        this.teamTitle = header.querySelector(".board-header-team-title");
        this.boardHeaderMemberContainer = header.querySelector(".board-header-team-member");
        this.boardHeaderMenuButton = header.querySelector(".board-header-menu-button");
        this.boardRemoveButton = header.querySelector(".board-header-remove-button");

        this.boardOptionHolder = createElementFromHTML(optionTemplate());
        header.appendChild(this.boardOptionHolder);

        this.boardHeaderMenuButton.addEventListener("click", this.onClickBoardOptionButton.bind(this));
        this.teamTitle.addEventListener("click", this.onClickBoardTitle.bind(this));
    }

    onClickBoardTitle(evt) {
        this.showBoardRenameOption(evt);
    }

    onClickBoardRemoveButton() {
        fetchManager({
            url: `/api/boards/${this.boardId}`,
            method: "DELETE",
            callback: this.handleBoardRemove.bind(this)
        });
    }

    onClickBoardOptionButton(evt) {
        evt.stopPropagation();
        this.showMenuOptions(evt)
    }

    handleBoardRemove(status) {
        if (status === 200)
            window.location.href = "/";
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
        this.drawBoardHeaderMembers(boardHeader.members);
        if (this.isAdminPermission()) {
            this.boardRemoveButton.classList.remove("board-header-remove-button-hide");
        }
        else {
            this.boardRemoveButton.classList.add("board-header-remove-button-hide");
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
            const html = `<img src="${member.profile}" class="board-header-team-member-img" alt="${member.name}">`;
            this.boardHeaderMemberContainer.appendChild(createElementFromHTML(html));
        });
    }

    showOption(evt) {
        this.boardOptionHolder.style.display = "block";

        this.boardOptionHolder.style.left = evt.clientX + "px";
        this.boardOptionHolder.style.top = evt.clientY + "px";
    }

    showBoardRenameOption(evt) {
        this.showOption(evt);

        const optionContainer = this.boardOptionHolder.querySelector(".board-options-container");
        const inputField = createElementFromHTML("<input>");

        optionContainer.innerHTML = "";
        optionContainer.appendChild(inputField);

        inputField.addEventListener("input", (evt) => {
            if (detectEnter(evt)) {
                // TODO: request rename;
            }
        })
    }

    showMenuOptions(evt) {
        this.showOption(evt);
        this.boardOptionHolder.querySelector(".board-options-title").innerText = "Board Menu";

        const optionContainer = this.boardOptionHolder.querySelector(".board-options-container");
        const deleteButton = createElementFromHTML("<li class='board-options-item'>Delete Board</li>");
        optionContainer.innerHTML = "";
        optionContainer.appendChild(deleteButton);

        deleteButton.addEventListener("click", this.onClickBoardRemoveButton.bind(this));
    }

    hideOption() {
        this.boardOptionHolder.style.display = "none";
    }
}