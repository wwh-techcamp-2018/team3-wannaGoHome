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
        this.optionsTemplate = Handlebars.templates["precompile/board/task_options_template"]

        const newTask = createElementFromHTML(this.taskListTemplate(this.taskObject));
        this.board.container.insertBefore(newTask, this.board.selector(".task-item:last-child"));

        this.incrementContainerWidth();


        this.taskContainer = newTask;
        this.task = newTask.querySelector(".task-list-content");
        this.taskWrapper = newTask.querySelector(".task-list-wrapper");
        this.taskTitleInput = newTask.querySelector(".task-list-header input");
        this.addCardButton = this.task.querySelector(".add-card-button");
        this.boardIndex = window.location.href.trim().split("/").pop();
        this.taskListTitle = this.task.querySelector(".task-list-title");
        this.taskListOptionButton = this.task.querySelector(".task-list-option");
        this.cardWrapper = this.task.querySelector(".new-card-wrapper");
        this.cardListContainer = this.task.querySelector(".card-list-container");

        this.taskListOptionHolder = createElementFromHTML(this.optionsTemplate({}));
        this.taskListDeleteButton = this.taskListOptionHolder.querySelector(".delete-options");
        $_(".board-scroll-container").appendChild(this.taskListOptionHolder);

        limitInputSize(this.taskTitleInput, 30);
    }

    remove() {
        this.taskContainer.remove();
    }

    addListeners() {
        limitInputSize(this.cardWrapper.querySelector(".new-card-title"), 20);

        document.addEventListener("click", (evt) => {
            this.addCardButton.style.display = 'block';
            this.cardWrapper.style.display = 'none';

            // make the board updatable again
            this.board.unsetDraggable();

            // hide options holder
            this.taskListOptionHolder.style.display = "none";

            // dispatch event to resize screen objects
            window.dispatchEvent(new Event("resize"));
        });

        this.cardWrapper.addEventListener("click", (evt) => {
            evt.stopPropagation();
        });

        this.addCardButton.addEventListener("click", (evt) => {
            evt.stopPropagation();
            // click body to reset any opened up boxes
            document.querySelector("body").click();
            // set dragObject to true in order to prevent reloading
            this.board.dragObject = true;

            this.addCardButton.style.display = 'none';
            this.cardWrapper.style.display = 'block';
            this.cardWrapper.querySelector(".new-card-title").value = "";
            this.cardWrapper.querySelector(".new-card-title").focus();
            this.cardListContainer.scrollTop = this.cardListContainer.scrollHeight;

            // dispatch event to resize screen objects
            window.dispatchEvent(new Event("resize"));
        });

        this.cardWrapper.querySelector("i").addEventListener("click", (evt) => {
            this.addCardButton.style.display = 'block';
            this.cardWrapper.style.display = 'none';

            this.board.unsetDraggable();

            // dispatch event to resize screen objects
            window.dispatchEvent(new Event("resize"));
        });


        this.cardWrapper.querySelector(".new-card-button").addEventListener("click", (evt) => {
            const obj = {};
            obj.title = this.cardWrapper.querySelector(".new-card-title").value;
            obj.createDate = new Date();
            this.cardWrapper.style.display = 'none';
            this.addCardButton.style.display = 'block';
            this.cardWrapper.querySelector(".new-card-title").value = "";

            this.board.unsetDraggable();

            this.addCard(obj);

        });

        this.cardWrapper.querySelector(".new-card-title").addEventListener("keypress", function (evt) {
            if (detectShiftEnter(evt)) {
                evt.preventDefault();
                pasteIntoInput(evt.currentTarget, "\n");
            } else if (detectEnter(evt)) {
                evt.preventDefault();
                this.cardWrapper.querySelector(".new-card-button").click();
            }
        }.bind(this));

        this.task.querySelector(".task-list-title").addEventListener("mousedown", function (evt) {

            this.board.startDrag.x = evt.clientX;
            this.board.startDrag.y = evt.clientY;

            this.board.dragObject = this;
            this.board.dragCallBack = this.moveTaskPosition;
            this.board.dragEndCallBack = this.unsetDraggable;

        }.bind(this));

        this.task.querySelector(".task-list-title").addEventListener("click", function(evt) {
            this.taskTitleInput.value = this.taskListTitle.innerHTML.trim();
            this.taskTitleInput.style.display = "block";
            this.taskTitleInput.focus();

            // set dragObject to true in order to prevent reloading
            this.board.dragObject = true;

        }.bind(this));

        this.taskTitleInput.addEventListener("blur", function(evt) {
            this.taskTitleInput.style.display = "none";

            // make the board updatable again
            if(this.board.dragObject === true) {
                this.board.unsetDraggable();
            }
        }.bind(this));

        this.taskTitleInput.addEventListener("keypress", function(evt) {
            if(detectEnter(evt)) {
                evt.preventDefault();
                const newTitle = evt.currentTarget.value.trim();
                this.taskObject.title = newTitle;
                this.renameTask(this.taskObject);
                evt.currentTarget.blur();

                // make the board updatable again
                if(this.board.dragObject === true) {
                    this.board.unsetDraggable();
                }
            }
        }.bind(this));

        this.taskListOptionButton.addEventListener("click", function(evt) {
            evt.preventDefault();
            evt.stopPropagation();
            document.querySelector("body").click();
            this.taskListOptionHolder.style.display = "block";

            this.taskListOptionHolder.style.left = evt.clientX + this.board.scrollContainer.scrollLeft + "px";
            this.taskListOptionHolder.style.top = evt.clientY - 80 + "px";

            // set dragObject to true in order to prevent reloading
            this.board.dragObject = true;

        }.bind(this));

        this.taskListDeleteButton.addEventListener("click", function(evt) {
            evt.preventDefault();
            evt.stopPropagation();
            document.querySelector("body").click();

            // make the board updatable again
            if(this.board.dragObject === true) {
                this.board.unsetDraggable();
            }

            this.deleteTask(this.taskObject);
        }.bind(this));

        window.addEventListener("resize", function (evt) {
            const rect = getBoundingRect(this.taskContainer);
            const titleRect = getBoundingRect(this.taskListTitle);
            const addButtonRect = getBoundingRect(this.addCardButton);
            this.cardListContainer.style.maxHeight = rect.height - titleRect.height - addButtonRect.height + "px";
        }.bind(this));

    }

    incrementContainerWidth() {
        const boundRect = getBoundingRect(this.board.container);
        const boundWidth = boundRect.width;
        const rect = this.getBoundingRect(this.board.selector(".add-list-button"));
        this.board.container.style.width = boundWidth + 280 + "px";

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
        if (!this.moving) {
            this.moving = true;
            this.setDraggable.call(this);
        }

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

    renameTask(obj) {
        this.board.stompClient.send(`/app/message/rename/${this.boardIndex}/${this.taskObject.id}`, {}, JSON.stringify(obj));
    }

    deleteTask(obj) {
        this.board.stompClient.send(`/app/message/delete/${this.boardIndex}/${this.taskObject.id}`, {}, JSON.stringify(obj));
    }

    addCard(obj) {
        this.board.stompClient.send(`/app/message/add/${this.boardIndex}/${this.taskObject.id}/card`, {}, JSON.stringify(obj));
    }

    reorderCard(originId, destIndex) {
        const obj = {
            originId: originId,
            destinationIndex: destIndex
        };
        this.board.stompClient.send(`/app/message/reorder/${this.boardIndex}/${this.taskObject.id}/card`, {}, JSON.stringify(obj));
    }
}
