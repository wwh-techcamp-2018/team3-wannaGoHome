class Board {
    constructor() {
        this.taskList = [];
        this.stompClient = null;

        // placeholder to hold mousedown coords
        this.startDrag = {x: 0, y: 0};

        // placeholder for element that is being dragged
        this.dragObject = null;
        this.dragCallBack = function(evt) {};
        this.dragEndCallBack = function() {};

        this.init();
        this.addListeners();
    }

    init () {
        this.container = $(".board-container");
        this.addButton = $(".add-list-button");
        this.connectSocket();

    }

    addListeners() {
        this.addButton.addEventListener("click", function(evt) {
            const defaultTask = {"title": "blankText"};
            this.taskList.push(new Task(this, defaultTask));
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
        console.log(this.stompClient);
        this.stompClient.connect({}, function(frame) {
            console.log('Connected: ' + frame);
            console.log(this);
            this.stompClient.subscribe('/topic/board', function (board) {
                console.log(board);
            });

        }.bind(this))
    }

    sendBoard(obj) {

        this.stompClient.send("/app/message/board", {}, JSON.stringify(obj));
    }
}