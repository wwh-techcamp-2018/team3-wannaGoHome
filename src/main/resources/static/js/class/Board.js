class Board {
    constructor() {
        this.taskList = [];
        this.stompClient = null;

        // placeholder to hold mousedown coords
        this.startDrag = {x: 0, y: 0};

        // placeholder for element that is being dragged
        this.dragObject = null;
        this.dragCallBack = function(evt) {};
        this.dragEndCallBack = function(evt) {};

        this.init();
        this.addListeners();
    }

    init () {
        this.container = $(".board-container");
        this.addButton = $(".add-button");
        this.connectSocket();
    }

    addListeners() {
        this.addButton.addEventListener("click", function(evt) {
            evt.stopPropagation();
            this.selector(".hidden-list-title-form").style.display = "block";
            this.selector(".hidden-list-title-form input").value = "";
            this.selector(".hidden-list-title-form input").focus();
        }.bind(this));

        this.selector(".hidden-list-title-form").addEventListener("click", function(evt) {
            evt.stopPropagation();
        }.bind(this));

        this.selector(".add-list-inner-button").addEventListener("click", function(evt) {
            const obj = {};
            obj.title = this.selector(".hidden-list-title-form input").value.trim();
            this.addTask(obj);
            this.selector(".hidden-list-title-form").style.display = "none";
        }.bind(this));

        this.selector(".hidden-list-title-form input").addEventListener("keyup", function(evt) {
            evt.preventDefault();
            if (event.keyCode === 13) {
                this.selector(".add-list-inner-button").click();
            }
        }.bind(this));

        document.addEventListener("click", function(evt) {
            this.selector(".hidden-list-title-form").style.display = "none";
        }.bind(this));

        this.container.addEventListener("mousemove", function(evt) {
            this.dragCallBack.call(this.dragObject, evt);
        }.bind(this));

        this.container.addEventListener("mouseleave", function(evt) {
            this.dragEndCallBack.call(this.dragObject);
        }.bind(this));

        this.container.addEventListener("mouseup", function(evt) {
            this.dragEndCallBack.call(this.dragObject);
        }.bind(this));
    }

    setBoardTasks(tasks) {
        while(this.taskList.length) {
            const task = this.taskList[0];
            task.remove();
            this.taskList.splice(0, 1);
        }

        console.log(tasks);
        this.container.style.width = "300px";
        for(const task of tasks) {
            this.taskList.push(new Task(this, task));
        }
    }

    updateBoardState() {
        const obj = {};
        obj.title = "Any title";
        obj.tasks = [];
        for(const task of this.taskList) {
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
        this.stompClient.connect({}, function(frame) {
            this.stompClient.subscribe('/topic/board/1', function (board) {
                this.setBoardTasks(JSON.parse(board.body).tasks);
            }.bind(this));
            this.fetchBoardState({});

        }.bind(this))
    }

    sendBoard(obj) {
        this.stompClient.send("/app/message/board/1", {}, JSON.stringify(obj));
    }

    fetchBoardState(obj) {
        this.stompClient.send("/app/message/board/1", {}, JSON.stringify(obj));
    }

    addTask(obj) {
        this.stompClient.send("/app/message/add/1/task", {}, JSON.stringify(obj));
    }

    reorderTasks(thisTaskIndex, destTaskIndex) {
        const obj = {
            originId: thisTaskIndex,
            destinationIndex: destTaskIndex
        };
        this.stompClient.send("/app/message/reorder/1/task", {}, JSON.stringify(obj));
    }
}