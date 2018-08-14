class Task {
    constructor(board, taskObject) {
        this.board = board;
        this.taskObject = taskObject;
        this.title = taskObject.title;

        this.dragPoint = {x: 0, y: 0};
        this.moving = false;

        // placeholder for task html element
        this.task = null;

        this.taskContainer = null;

        this.init();
        this.addListeners();
    }

    init() {
        this.taskListTemplate = Handlebars.templates["precompile/board/task_list_template"];

        const newTask = createElementFromHTML(this.taskListTemplate(this.taskObject));
        this.board.container.insertBefore(newTask, this.board.selector(".task-item:last-child"));

        this.taskContainer = newTask;
        this.task = newTask.querySelector(".task-list-content");
        this.taskWrapper = newTask.querySelector(".task-list-wrapper");
    }

    addListeners() {

        this.task.addEventListener("mousedown", function(evt) {
            this.moving = true;

            this.board.startDrag.x = evt.clientX;
            this.board.startDrag.y = evt.clientY;

            this.setDraggable.call(this, this.task);
        }.bind(this));

    }

    getCurrentCoords(evt) {
        const coordObj = {x: evt.clientX - this.board.startDrag.x, y: evt.clientY - this.board.startDrag.y};
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

        elem.classList.toggle("task-list-dragging");
    }

    moveTaskPosition(evt) {
        if(!this.moving) return;
        const coords = this.getCurrentCoords(evt);
        this.task.style.left = coords.x + "px";
        this.task.style.top = coords.y + "px";


    }

    unsetDraggable() {
        if(!this.moving) return;

        this.task.style.position = "static";
        this.task.style.left = "0px";
        this.task.style.top = "0px";

        const parent = this.task.parentNode;
        parent.style.height = null;

        this.task.classList.toggle("task-list-dragging");

        this.moving = false;

        const rect = this.getBoundingRect(this.task);
        const centerX = (rect.left + rect.right) / 2;
        for(const task of this.board.taskList) {
            task.handleInsideBound.call(task, centerX, this);
        }
    }

    handleInsideBound(x, task) {
        const rect = this.getBoundingRect(this.taskWrapper);
        if(rect.left < x && rect.right > x && (this != task)) {
            console.log(this.task.title, "inside!");

            const newRect = this.getBoundingRect(this.taskContainer.nextSibling);
            const originRect = task.getBoundingRect(task.taskContainer);

            // this.board.startDrag.x = this.board.startDrag.x + (newRect - originRect);

            this.board.container.insertBefore(task.taskContainer, this.taskContainer.nextSibling);
        }


        return (rect.left < x && rect.right > x);
    }

    // top right bottom left
    getBoundingRect(element) {
        const rect = element.getBoundingClientRect();
        return rect;
    }
}