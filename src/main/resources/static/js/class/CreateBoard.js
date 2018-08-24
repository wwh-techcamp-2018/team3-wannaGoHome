class CreateBoard {
    constructor(parent) {
        this.parent = parent;
        this.node = $_(".create-board-whole-container");
        this.titleNode = this.node.querySelector(".create-board-title");
        this.submitButtonNode = this.node.querySelector(".create-board-submit-button");
        this.colorBoxNode = this.node.querySelector(".color-box");
        this.teamSelectBoxNode = this.node.querySelector(".team-select");
        this.selectedColorNode = undefined;
        this.teamId = undefined;
        this.titleDefaultPlaceHolder = this.titleNode.placeholder;
        this.TR_COUNT = 3;
        this.TOP_MARGIN = 64;

        this.addBackgroundClickEvent();
        this.addTitleInputEvent();
        this.addSubmitButtonClickEvent();
    }

    showCreateBoardForm(teamId) {
        this.teamId = teamId;
        this.requestCreateBoardInfo();
    }

    hideCreateBoardForm() {
        this.node.parentElement.classList.toggle("scroll-overflow");
        this.node.style.display = "none";
    }

    requestCreateBoardInfo() {
        fetchManager({
            url : "api/boards/createBoardInfo",
            method : "GET",
            callback : this.drawCreateBoardInfo.bind(this)
        })
    }

    drawCreateBoardInfo(status, response) {
        this.addTeamSelectBox(response.teams);
        this.drawColorBox(response.colors);
        this.initializeCreateBoardForm();
    }

    initializeCreateBoardForm() {
        this.initializeFormPosition();
        this.initializeFormValue();
        this.node.parentElement.classList.toggle("scroll-overflow");
        this.node.style.display = "";
        this.titleNode.focus();
    }

    initializeFormPosition() {
        const createBoardContainer = this.node.querySelector(".create-board-container");
        createBoardContainer.style.top = (window.scrollY + this.TOP_MARGIN) + "px";
    }

    initializeFormValue() {
        this.clearTitleNode();
        this.teamSelectBoxNode.value = this.teamId;
        this.selectColor(this.colorBoxNode.getElementsByTagName("div")[0]);
    }

    addTeamSelectBox(teams) {
        teams.forEach((team, index) => {
            this.teamSelectBoxNode.options[index] = new Option(team.name, team.id);
        });
    }

    drawColorBox(colors) {
        this.colorBoxNode.innerHTML = "";
        let trColors = {};
        colors.forEach((color, index) => {
            const colorIndex = index % this.TR_COUNT;
            trColors["color" + colorIndex] = color;
            if((index + 1) % this.TR_COUNT === 0) {
                this.colorBoxNode.appendChild(
                    this.createElementFromTrHTML(this.getTrColorTemplate(trColors))
                );
            }
        });
        this.addColorClickEvent();
    }

    addColorClickEvent() {
        for(const div of this.colorBoxNode.getElementsByTagName("div")) {
            div.addEventListener("click", (evt) => {
                evt.preventDefault();
                this.selectColor(evt.target);
            });
        }
    }

    selectColor(colorNode) {
        if(this.selectedColorNode) {
            this.selectedColorNode.firstChild.remove();
        }
        this.selectedColorNode = colorNode;
        this.selectedColorNode.appendChild(this.getSelectColorIcon());
        this.node.querySelector(".create-board-descirption").style.backgroundColor
            = this.selectedColorNode.style.backgroundColor;
    }


    addTitleInputEvent() {
        this.titleNode.addEventListener("input", (evt) => {
            evt.preventDefault();
            this.titleNode.placeholder = this.titleDefaultPlaceHolder;
            if(this.titleNode.value === "") {
                this.submitButtonNode.classList.add("disable");
                this.submitButtonNode.disabled = true;
                return;
            }
            this.submitButtonNode.classList.remove("disable");
            this.submitButtonNode.disabled = false;
        });
    }

    addBackgroundClickEvent() {
        const backgroundNode = $_(".create-board-background");
        backgroundNode.addEventListener("click", (evt) => {
            evt.preventDefault();
            this.hideCreateBoardForm();
        });
    }

    addSubmitButtonClickEvent() {
        this.submitButtonNode.addEventListener("click", (evt) => {
            evt.preventDefault();
            fetchManager({
                url : "/api/boards",
                method : "POST",
                body : JSON.stringify(this.getCreateBoardRequestData()),
                callback : this.handleCreateBoard.bind(this)
            })
        });

    }

    getCreateBoardRequestData() {
        const data  = {
            "title" : this.titleNode.value,
            "teamId" : this.teamSelectBoxNode.value,
            "color" : rgbToHex(this.selectedColorNode.style.backgroundColor)
        };
        return data;
    }

    handleCreateBoard(status, response) {
        if(status === 400) {
            this.handleCreateBoardError(response)
            return;
        }

        this.drawCreatedBoard(response);
        this.hideCreateBoardForm();
    }

    drawCreatedBoard(board) {
        const teamBoardNode = this.parent.querySelector(`#team-${board.team.id}`);
        const createBoardCard = teamBoardNode.querySelector(".create-board-card");
        createBoardCard.insertAdjacentElement("beforebegin",new BoardCard(board).boardNode);
    }

    handleCreateBoardError(response) {
        this.clearTitleNode();
        this.titleNode.placeholder = response[0].message;
        this.titleNode.focus();
    }

    clearTitleNode() {
        this.titleNode.value = "";
        this.submitButtonNode.classList.add("disable");
        this.submitButtonNode.disabled = true;
    }

    getTrColorTemplate(trColors) {
        const template = Handlebars.templates["precompile/color_box_template"];
        return template(trColors);
    }

    createElementFromTrHTML(htmlString) {
        const table = document.createElement('table');
        table.innerHTML = htmlString.trim();
        return table.firstChild.firstChild;
    }

    getSelectColorIcon() {
        return createElementFromHTML('<i class="fa fa-check"></i>');
    }

}