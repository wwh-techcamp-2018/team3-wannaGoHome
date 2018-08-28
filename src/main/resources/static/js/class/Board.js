class Board {
    constructor() {
        this.calendar;
        this.taskList = [];
        this.stompClient = null;
        this.boardIndex = window.location.href.trim().split("/").pop();
        this.permission = null;

        this.cardDetailForm = new CardDetail(this.boardIndex);
        // placeholder to hold mousedown coords
        this.startDrag = {x: 0, y: 0};

        // placeholder for element that is being dragged
        // always reset dragObject to null after drag event is finished
        this.dragObject = null;
        this.dragCallBack = function (evt) {
        };
        this.dragEndCallBack = function (evt) {
        };

        this.init();
        this.addListeners();
    }

    init() {
        this.scrollContainer = $_(".board-scroll-container");
        this.container = $_(".board-container");
        this.addButton = $_(".add-button");
        this.addListButton = $_(".add-list-button");
        this.initBoardHeader();

        this.connectSocket();
    }

    initBoardHeader() {
        const boardHeader = $_(".board-header");
        this.boardTitle = boardHeader.querySelector(".board-header-title");
        this.teamTitle = boardHeader.querySelector(".board-header-team-title");
        this.boardHeaderMemberContainer = boardHeader.querySelector(".board-header-team-member");
        this.boardRemoveButton = boardHeader.querySelector(".board-header-remove-button");

        this.boardRemoveButton.addEventListener("click", this.onClickBoardRemoveButton.bind(this));
    }

    addListeners() {
        this.addButton.addEventListener("click", function (evt) {
            document.querySelector("body").click();
            evt.stopPropagation();
            this.selector(".hidden-list-title-form").style.display = "block";
            this.selector(".hidden-list-title-form input").value = "";
            this.selector(".hidden-list-title-form input").focus();
        }.bind(this));

        this.selector(".hidden-list-title-form").addEventListener("click", function (evt) {
            evt.stopPropagation();
        }.bind(this));

        this.selector(".add-list-inner-button").addEventListener("click", function (evt) {
            evt.preventDefault();
            const obj = {};
            obj.title = this.selector(".hidden-list-title-form input").value.trim();

            this.selector(".hidden-list-title-form").style.display = "none";

            if(obj.title.length > 0) {
                // hide addListButton temporarily
                this.addListButton.style.display = "none";
                this.addTask(obj);

            }

        }.bind(this));

        this.selector(".hidden-list-title-form input").addEventListener("keyup", function (evt) {
            evt.preventDefault();
            if (event.keyCode === 13) {
                this.selector(".add-list-inner-button").click();
            }
        }.bind(this));

        document.addEventListener("click", function (evt) {
            this.selector(".hidden-list-title-form").style.display = "none";
        }.bind(this));

        this.addMouseDragListeners();
    }

    addMouseDragListeners() {
        this.container.addEventListener("mousemove", function (evt) {
            this.dragCallBack.call(this.dragObject, evt);
        }.bind(this));

        this.container.addEventListener("mouseleave", function (evt) {
            this.dragEndCallBack.call(this.dragObject);
        }.bind(this));

        this.container.addEventListener("mouseup", function (evt) {
            this.dragEndCallBack.call(this.dragObject);
        }.bind(this));
    }

    unsetDraggable() {
        this.dragObject = null;
        this.dragCallBack = function (evt) {
        };
        this.dragEndCallBack = function (evt) {
        };
    }

    setBoard(unsortedTasks) {
        this.scrollLeft = this.scrollContainer.scrollLeft;
        const originalTaskListLength = this.taskList.length;
        while (this.taskList.length) {
            const task = this.taskList[0];
            task.remove();
            this.taskList.splice(0, 1);
        }
        const tasks = unsortedTasks.sort((a, b) => {
            return a.orderId - b.orderId;
        });
        this.container.style.width = "280px";
        for (const task of tasks) {
            const taskObject = new Task(this, task);
            this.taskList.push(taskObject);
            for (const card of task.cards) {
                const newCard = new Card(card, taskObject, this, this.cardDetailForm);
                taskObject.cardList.push(newCard);
            }

        }
        // show add List button after loading inline-block
        this.addListButton.style.display = "inline-block";
        // reset scroll Left
        this.scrollContainer.scrollLeft = this.scrollLeft;// + (tasks.length - originalTaskListLength) * 278;

        // dispatch event to resize screen objects
        window.dispatchEvent(new Event("resize"));

    }

    setBoardInfo(boardObj) {
        if (boardObj.deleted) {
            showDialog("Alert", "Board is deleted by someone", () => {
                window.location.href = "/";
            });
        }

        this.permission = boardObj.permission;

        addEscapedText(this.boardTitle, boardObj.boardTitle);
        addEscapedText(this.teamTitle, boardObj.teamTitle);
        this.drawBoardHeaderMembers(boardObj.members);
        if (this.isAdminPermission()) {
            this.boardRemoveButton.classList.remove("board-header-remove-button-hide");
        }
        else {
            this.boardRemoveButton.classList.add("board-header-remove-button-hide");
        }

        $_("body").style.backgroundColor = boardObj.color;
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

    onClickBoardRemoveButton() {
        fetchManager({
            url: `/api/boards/${this.boardIndex}`,
            method: "DELETE",
            callback: this.handleBoardRemove.bind(this)
        });
    }

    handleBoardRemove(status) {
        if (status === 200)
            window.location.href = "/";
    }

    updateBoardState() {
        const obj = {};
        obj.title = "Any title";
        obj.tasks = [];
        for (const task of this.taskList) {
            obj.tasks.push(task.getSocketObject());
        }
        this.sendBoard(obj);
    }

    selector(nodeSelector) {
        return this.container.querySelector(nodeSelector);
    }

    connectSocket() {
        const socket = new SockJS('/websocket');
        this.stompClient = Stomp.over(socket);
        this.stompClient.debug = null;
        this.stompClient.connect({}, function (frame) {
            this.stompClient.subscribe('/topic/board/' + this.boardIndex, function (frame) {
                // return if in the middle of a drag event
                if (this.dragObject) {
                    return;
                }
                const body = JSON.parse(frame.body);
                this.setBoard(body.tasks);
            }.bind(this));
            this.stompClient.subscribe(`/topic/board/${this.boardIndex}/header`, function (frame) {
                const body = JSON.parse(frame.body());
                this.setBoardInfo(body);
            }.bind(this));

            this.cardDetailForm.setClient(this.stompClient);
            this.fetchBoardState();
        }.bind(this))
    }

    sendBoard(obj) {
        this.stompClient.send(`/app/message/board/${this.boardIndex}`, {}, JSON.stringify(obj));
    }

    fetchBoardState() {
        fetchManager({
            url: `/api/boards/${this.boardIndex}`,
            method: "GET",
            callback: (status, result) => {
                this.setBoard(result.tasks);
                this.setBoardInfo(result);
            }
        });
    }

    addTask(obj) {
        this.stompClient.send(`/app/message/add/${this.boardIndex}/task`, {}, JSON.stringify(obj));
    }

    reorderTasks(originId, destTaskIndex) {
        const obj = {
            originId: originId,
            destinationIndex: destTaskIndex
        };
        this.stompClient.send(`/app/message/reorder/${this.boardIndex}/task`, {}, JSON.stringify(obj));
    }


    hideCardDetailForm() {
        this.cardDetailForm.hide();
    }
}