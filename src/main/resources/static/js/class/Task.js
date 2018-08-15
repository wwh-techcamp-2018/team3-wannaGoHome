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

            this.setDraggable.call(this);
        }.bind(this));

    }

    getCurrentCoords(evt) {
        const coordObj = {x: evt.clientX - this.board.startDrag.x, y: evt.clientY - this.board.startDrag.y};
        return coordObj;
    }

    setDraggable() {
        const parent = this.task.parentNode;
        parent.style.width = this.task.offsetWidth + "px";
        parent.style.height = this.task.offsetHeight + "px";

        this.task.style.width = this.task.offsetWidth + "px";
        this.task.style.height = this.task.offsetHeight + "px";

        this.task.style.zIndex = '10';

        this.task.style.position = "absolute";

        this.board.dragObject = this;
        this.board.dragCallBack = this.moveTaskPosition;
        this.board.dragEndCallBack = this.unsetDraggable;

        this.task.classList.toggle("task-list-dragging");
    }

    moveTaskPosition(evt) {
        if(!this.moving) return;

        const rect = this.getBoundingRect(this.task);
        const centerX = (rect.left + rect.right) / 2;

        // placeholders for current index and destination index
        let thisTaskIndex = -1;
        let destTaskIndex = -1;
        let i = 0;
        for(const task of this.board.taskList) {
            const insideBound = task.isInsideBound.call(task, centerX, this);
            if(insideBound) {
                destTaskIndex = i;
            }
            if(this == task) {
                thisTaskIndex = i;
            }
            i++;
        }
        console.log(destTaskIndex);
        if(destTaskIndex !== -1) {
            if(thisTaskIndex > destTaskIndex) {
                this.board.taskList[destTaskIndex].handleInsideBound.call(this.board.taskList[destTaskIndex], centerX, this, true);
                this.board.taskList.splice(thisTaskIndex, 1);
                this.board.taskList.splice(destTaskIndex, 0, this);
            } else {
                this.board.taskList[destTaskIndex].handleInsideBound.call(this.board.taskList[destTaskIndex], centerX, this, false);
                this.board.taskList.splice(thisTaskIndex, 1);
                this.board.taskList.splice(destTaskIndex, 0, this);
            }
        }

        console.log(this.board.taskList);

        const coords = this.getCurrentCoords(evt);
        this.task.style.left = coords.x + "px";
        this.task.style.top = coords.y + "px";

    }

    unsetDraggable() {
        if(!this.moving) return;

        this.task.style.position = "static";
        this.task.style.left = "0px";
        this.task.style.top = "0px";

        this.task.style.zIndex = null;

        const parent = this.task.parentNode;
        parent.style.height = null;

        this.task.classList.toggle("task-list-dragging");

        this.moving = false;

    }

    isInsideBound(x, task) {
        const rect = this.getBoundingRect(this.taskWrapper);
        return (rect.left < x && rect.right > x && (this != task));
    }

    handleInsideBound(x, task, prev) {
        const rect = this.getBoundingRect(this.taskWrapper);
        if(rect.left < x && rect.right > x && (this != task)) {
            console.log(this.task.title, "inside!");
            const newRect = this.getBoundingRect(this.taskContainer);
            const originRect = task.getBoundingRect(task.taskWrapper);
            console.log(newRect, originRect);
            console.log(this.board.startDrag.x);
            this.board.startDrag.x = this.board.startDrag.x + (newRect.left - originRect.left);
            console.log(this.board.startDrag.x);
            if(prev) {
                this.board.container.insertBefore(task.taskContainer, this.taskContainer);
            } else {
                this.board.container.insertBefore(task.taskContainer, this.taskContainer.nextSibling);
            }


        }

    }

    // top right bottom left
    getBoundingRect(element) {
        const rect = element.getBoundingClientRect();
        return rect;
    }
}