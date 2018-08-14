class Task {
    constructor(board, taskObject) {
        this.board = board;
        this.taskObject = taskObject;

        this.startDrag = {x: 0, y: 0};
        this.moving = false;

        // placeholder for task html element
        this.task = null;

        this.init();
        this.addListeners();
    }

    init() {
        this.taskListTemplate = Handlebars.templates["precompile/board/task_list_template"];

        const newTask = createElementFromHTML(this.taskListTemplate(this.taskObject));
        this.board.container.insertBefore(newTask, this.board.selector(".task-item:first-child"));

        this.task = newTask.querySelector(".task-list-content");
    }

    addListeners() {

        this.task.addEventListener("mousedown", function(evt) {
            this.moving = true;
            this.board.startDrag.x = evt.clientX;
            this.board.startDrag.y = evt.clientY;
            console.log(this.startDrag);

            this.setDraggable.call(this, this.task);
        }.bind(this));


    }

    getCurrentCoords(evt) {
        const coordObj = {x: evt.clientX - this.startDrag.x, y: evt.clientY - this.startDrag.y};
        return coordObj;
    }

    setDraggable(elem) {
        const parent = elem.parentNode;
        parent.style.width = elem.offsetWidth + "px";
        parent.style.height = elem.offsetHeight + "px";

        elem.style.width = elem.offsetWidth + "px";
        elem.style.height = elem.offsetHeight + "px";

        elem.style.position = "absolute";

        this.board.dragObject = this;
        this.board.dragCallBack = this.moveTaskPosition;
        this.board.dragEndCallBack = this.unsetDraggable;
    }

    moveTaskPosition(evt) {
        if(!this.moving) return;
        const coords = this.getCurrentCoords(evt);
        this.task.style.left = coords.x + "px";
        this.task.style.top = coords.y + "px";
    }

    unsetDraggable() {
        this.task.style.position = "static";
        this.task.style.left = "0px";
        this.task.style.top = "0px";

        const parent = this.task.parentNode;
        parent.style.height = null;

    }

    getBoundingRect() {
        const rect = element.getBoundingClientRect();
        console.log(rect.top, rect.right, rect.bottom, rect.left);
    }
}