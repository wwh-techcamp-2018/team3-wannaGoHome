//task.js
class Task {
    constructor(board, taskObject) {
        this.board = board;
        this.taskObject = taskObject;
        this.title = taskObject.title;

        this.moving = false;

        // placeholder for task html element
        this.task = null;

        // placeholder for card list
        this.cardList = [];

        this.taskContainer = null;

        this.init();
        this.addListeners();
    }

    init() {
        this.taskListTemplate = Handlebars.templates["precompile/board/task_list_template"];

        const newTask = createElementFromHTML(this.taskListTemplate(this.taskObject));
        this.board.container.insertBefore(newTask, this.board.selector(".task-item:last-child"));

        this.incrementContainerWidth();

        this.taskContainer = newTask;
        this.task = newTask.querySelector(".task-list-content");
        this.taskWrapper = newTask.querySelector(".task-list-wrapper");
        this.addCardButton = this.task.querySelector(".add-card-button");
        this.boardIndex = window.location.href.trim().split("/").pop();
        this.cardWrapper = this.task.querySelector(".new-card-wrapper");
    }

    remove() {
        console.log("Remove called");
        this.taskContainer.remove();
    }

    addListeners() {
        document.addEventListener("click", (evt)=>{
            this.addCardButton.style.display = 'block';
            this.cardWrapper.style.display = 'none';
        });
        this.cardWrapper.addEventListener("click", (evt)=>{
            evt.stopPropagation();
        })
        this.addCardButton.addEventListener("click", (evt)=>{
            evt.stopPropagation();
            this.addCardButton.style.display = 'none';
            this.cardWrapper.style.display = 'block';
            this.cardWrapper.querySelector(".new-card-title").value = "";
            this.cardWrapper.querySelector(".new-card-title").focus();
        });

        this.cardWrapper.querySelector("i").addEventListener("click", (evt)=>{
            this.addCardButton.style.display = 'block';
            this.cardWrapper.style.display = 'none';
        });


        this.cardWrapper.querySelector(".new-card-button").addEventListener("click", (evt)=>{
            const obj = {};
            obj.title = this.cardWrapper.querySelector(".new-card-title").value;
            obj.createDate = new Date();
            this.cardWrapper.style.display = 'none';
            this.addCardButton.style.display = 'block';
            this.cardWrapper.querySelector(".new-card-title").value = "";
            this.addCard(obj);
        });

        this.task.querySelector(".task-list-title").addEventListener("mousedown", function (evt) {
            this.moving = true;

            this.board.startDrag.x = evt.clientX;
            this.board.startDrag.y = evt.clientY;

            this.setDraggable.call(this);
        }.bind(this));

    }

    incrementContainerWidth() {
        const currentHeight = this.board.container.style.width.trim();
        const rect = this.getBoundingRect(this.board.selector(".add-list-button"));
        if (!currentHeight) {
            this.board.container.style.width = (rect.right - rect.left + 6) * 2 + "px";
        } else {
            this.board.container.style.width = parseInt(currentHeight.substring(0, currentHeight.length - 2)) + (rect.right - rect.left + 6) + "px";
        }
    }

    remove() {
        this.taskContainer.remove();
    }

    getSocketObject() {
        const obj = {};
        obj.title = this.title;

        // need to implement later
        obj.cards = this.cardList;
        obj.deleted = false;
        return obj;
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

        this.task.style.boxShadow = "2px 2px 2px 2px rgba(51,51,51,0.3)";

        this.board.dragObject = this;
        this.board.dragCallBack = this.moveTaskPosition;
        this.board.dragEndCallBack = this.unsetDraggable;

        this.task.classList.toggle("task-list-dragging");

        for (let i = 0; i < this.board.taskList.length; ++i) {
            if (this.board.taskList[i] == this) {
                this.originIndex = this.taskObject.id;
                this.destinationIndex = i;
                break;
            }
        }
    }

    moveTaskPosition(evt) {
        if (!this.moving) return;

        const rect = this.getBoundingRect(this.task);
        const centerX = (rect.left + rect.right) / 2;

        // placeholders for current index and destination index
        let thisTaskIndex = -1;
        let destTaskIndex = -1;
        let i = 0;
        for (const task of this.board.taskList) {
            const insideBound = task.isInsideBound.call(task, centerX, this);
            if (insideBound) {
                destTaskIndex = i;
            }
            if (this == task) {
                thisTaskIndex = i;
            }
            i++;
        }

        if (destTaskIndex !== -1) {
            if (thisTaskIndex > destTaskIndex) {
                this.board.taskList[destTaskIndex].handleInsideBound.call(this.board.taskList[destTaskIndex], centerX, this, true);
                this.board.taskList.splice(thisTaskIndex, 1);
                this.board.taskList.splice(destTaskIndex, 0, this);
            } else {
                this.board.taskList[destTaskIndex].handleInsideBound.call(this.board.taskList[destTaskIndex], centerX, this, false);
                this.board.taskList.splice(thisTaskIndex, 1);
                this.board.taskList.splice(destTaskIndex, 0, this);
            }
            this.destinationIndex = destTaskIndex;
        }


        const coords = this.getCurrentCoords(evt);
        this.task.style.left = coords.x + "px";
        this.task.style.top = coords.y + "px";

    }

    unsetDraggable() {
        if (!this.moving) return;

        this.task.style.position = "static";
        this.task.style.left = "0px";
        this.task.style.top = "0px";

        this.task.style.zIndex = null;

        this.task.style.boxShadow = null;

        const parent = this.task.parentNode;
        parent.style.height = null;

        this.task.classList.toggle("task-list-dragging");

        this.moving = false;

        // this.board.updateBoardState();
        this.board.reorderTasks(this.originIndex, this.destinationIndex);

        // reset drag object
        this.board.unsetDraggable();

    }

    isInsideBound(x, task) {
        const rect = this.getBoundingRect(this.taskWrapper);
        return (rect.left < x && rect.right > x && (this != task));
    }

    handleInsideBound(x, task, prev) {
        const rect = this.getBoundingRect(this.taskWrapper);
        if (rect.left < x && rect.right > x && (this != task)) {

            const newRect = this.getBoundingRect(this.taskContainer);
            const originRect = task.getBoundingRect(task.taskWrapper);

            this.board.startDrag.x = this.board.startDrag.x + (newRect.left - originRect.left);

            if (prev) {
                this.board.container.insertBefore(task.taskContainer, this.taskContainer);
            } else {
                this.board.container.insertBefore(task.taskContainer, this.taskContainer.nextSibling);
            }
        }
    }

    selector(nodeSelector) {
        return this.taskContainer.querySelector(nodeSelector);
    }

    insertCardNode(card) {
        this.selector(".card-list-wrapper").appendChild(card.cardHolder);
    }

    setOverflow(value) {
        this.task.style.overflow = value;
    }

    // top right bottom left
    getBoundingRect(element) {
        const rect = element.getBoundingClientRect();
        return rect;
    }

    addCard(obj) {
        this.board.stompClient.send(`/app/message/add/${this.boardIndex}/${this.taskObject.id}/card`, {}, JSON.stringify(obj));
    }

    reorderCard(originId, destIndex) {
        console.log(this.taskObject.id, originId, destIndex);
        const obj = {
            originId: originId,
            destinationIndex: destIndex
        };
        this.board.stompClient.send(`/app/message/reorder/${this.boardIndex}/${this.taskObject.id}/card`, {}, JSON.stringify(obj));
    }
}
