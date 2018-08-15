class Board {
    constructor() {
        this.init();
        this.addListeners();

        // placeholder to hold mousedown coords
        this.startDrag = {x: 0, y: 0};

        // placeholder for element that is being dragged
        this.dragObject = null;
        this.dragCallBack = function(evt) {};
        this.dragEndCallBack = function() {};
    }

    init () {
        this.container = $(".board-container");
        this.addButton = $(".add-list-button");
        this.taskList = [];

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
            console.log("mouseout!");
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
        console.log("Updating Board State");
    }

    selector(nodeSelector) {
        return this.container.querySelector(nodeSelector);
    }
}