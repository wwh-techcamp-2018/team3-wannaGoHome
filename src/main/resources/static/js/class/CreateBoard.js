class CreateBoard {
    constructor(parent) {
        this.parent = parent;
        this.node = $(".create-board-whole-container")
        this.titleNode = this.node.querySelector(".create-board-title");
        this.submitButtonNode = this.node.querySelector(".create-board-submit-button");
        this.colorBoxNode = this.node.querySelector(".color-box");
        this.teamSelectBoxNode = this.node.querySelector(".team-select");
        this.selectedColorNode = undefined;
        this.teamId = undefined;
        this.TR_COUNT = 3;
        this.TOP_MARGIN = 64;

        this.addBackgroundClickEvent();
        this.addTitleInputEvent();
        this.addSubmitButtonClickEvent();
    }

    displayCreateBoardForm(teamId) {
        const constCreateBoardContainer = this.node.querySelector(".create-board-container");
        constCreateBoardContainer.style.top = (window.scrollY + this.TOP_MARGIN) + "px";
        this.node.parentElement.classList.toggle("overflow");
        this.teamId = teamId;
        this.requestCreateBoardInfo();
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
        this.drawColorBox(response.colors)
        this.initializeCreateBoardForm();
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



    initializeCreateBoardForm() {
        this.node.style.display = "";
        this.titleNode.value = "";
        this.submitButtonNode.classList.add("disable");
        this.submitButtonNode.disabled = true;
        this.teamSelectBoxNode.value = this.teamId;
        this.selectColor(this.colorBoxNode.getElementsByTagName("div")[0]);
    }


    addTitleInputEvent() {
        this.titleNode.addEventListener("input", (evt) => {
            evt.preventDefault();
            if(evt.target.value == 0) {
                this.submitButtonNode.classList.add("disable");
                this.submitButtonNode.disabled = true;
                return;
            }
            this.submitButtonNode.classList.remove("disable");
            this.submitButtonNode.disabled = false;
        });
    }



    addBackgroundClickEvent() {
        const backgroundNode = $(".create-board-background");
        backgroundNode.addEventListener("click", (evt) => {
            evt.preventDefault();
            this.node.parentElement.classList.toggle("overflow");
            this.node.style.display = "none";
        });
    }

    addSubmitButtonClickEvent() {
        this.submitButtonNode.addEventListener("click", (evt) => {
            evt.preventDefault();
            const data  = {
                "title" : this.titleNode.value,
                "teamId" : this.teamSelectBoxNode.value,
                "color" : rgbToHex(this.selectedColorNode.style.backgroundColor)
            };

            fetchManager({
                url : "/api/boards",
                method : "POST",
                body : JSON.stringify(data),
                callback : this.handleCreateBoard.bind(this)
            })
        });

    }

    handleCreateBoard(status, response) {
        const teamBoardNode = this.parent.querySelector(`#team-${response.team.id}`);
        const createBoardCard = teamBoardNode.querySelector(".create-board-card");
        createBoardCard.insertAdjacentElement("beforebegin",new BoardCard(response).getBoardNode());
        this.node.style.display = "none";
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