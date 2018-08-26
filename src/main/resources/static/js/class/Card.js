class Card {
    constructor(card, task, board) {
        this.card = card;
        this.task = task;
        this.board = board;
        this.id = card.id;
        this.taskId = card.taskId;
        this.boardId = card.boardId;
        this.title = card.title;
        this.description = card.description;
        this.endDate = card.endDate;
        this.startDate = card.createDate;
        this.moving = false;
        this.init();

    }

    init() {
        this.cardListTemplate = Handlebars.templates["precompile/board/card_list_template"];
        const newCard = createElementFromHTML(this.cardListTemplate(this.card));
        // replacing innerHTML susceptible to html injection
        // newCard.querySelector(".card-list-title").innerHTML = this.card.title.replaceAll("\n", "<br />");
        this.task.taskWrapper.querySelector(".card-list-wrapper").appendChild(newCard);
        this.card = newCard.querySelector(".card-wrapper");
        this.cardHolder = newCard;
        this.cardListContainer = this.task.taskWrapper.querySelector(".card-list-wrapper");

        this.card.addEventListener("mousedown", function (evt) {
            this.moving = true;

            this.board.startDrag.x = evt.clientX;
            this.board.startDrag.y = evt.clientY;

            this.setDraggable.call(this, evt);
        }.bind(this));

    }

    setDraggable(evt) {
        // parent is the shadow
        this.cardHolder.style.width = this.card.offsetWidth + "px";
        this.cardHolder.style.height = this.card.offsetHeight + "px";

        // this.cardHolder.removeChild(this.card);
        this.board.container.appendChild(this.card);

        this.board.startDrag.x = evt.clientX;
        this.board.startDrag.y = evt.clientY;


        // this card is the moving object
        this.card.style.width = this.card.offsetWidth + "px";
        this.card.style.height = this.card.offsetHeight + "px";

        this.card.style.zIndex = '10';
        this.card.style.position = "fixed";

        this.card.style.boxShadow = "2px 2px 2px 2px rgba(51,51,51,0.3)";

        this.board.dragObject = this;
        this.board.dragCallBack = this.moveTaskPosition;
        this.board.dragEndCallBack = this.unsetDraggable;

        this.card.classList.toggle("card-list-dragging");

        for (let i = 0; i < this.task.cardList.length; ++i) {
            if (this.task.cardList[i] === this) {
                this.destinationIndex = i;
                break;
            }
        }

        const cardRect = getBoundingRect(this.cardHolder);

        const gapX = evt.clientX - cardRect.left;
        const gapY = evt.clientY - cardRect.top;


        this.board.startDrag.x = gapX;
        this.board.startDrag.y = gapY;

        const coords = this.getCurrentCoords(evt);
        this.card.style.left = coords.x + "px";
        this.card.style.top = coords.y + "px";
    }

    moveTaskPosition(evt) {
        if (!this.moving) return;

        const rect = getBoundingRect(this.card);
        const centerX = (rect.left + rect.right) / 2;
        const centerY = (rect.top + rect.bottom) / 2;

        let thisTaskIndex = -1;
        let destTaskIndex = -1;
        let thisCardIndex = -1;
        let destCardIndex = -1;

        let i = 0;
        for(const task of this.board.taskList) {
            let insideBound = task.isInsideBound.call(task, centerX, this);
            if(insideBound) {
                destTaskIndex = i;
                let j = 0;
                for(const card of task.cardList) {
                    insideBound = card.isInsideBoundY.call(card, centerY, this);
                    if(insideBound) {
                        destCardIndex = j;
                    }
                    j++;
                }
                if(task.cardList == 0) {
                    destCardIndex = 0;
                }

            }
            if(this.task == task) {
                thisTaskIndex = i;
                let j = 0;
                for(const card of task.cardList) {
                    if(this == card) {
                        thisCardIndex = j;
                    }
                    j++;
                }
            }
            i++;
        }

        if(destTaskIndex != -1 && destCardIndex != -1) {
            // same Task Index
            if(destTaskIndex == thisTaskIndex) {
                if (thisCardIndex > destCardIndex) {
                    this.task.cardList[destCardIndex].handleInsideBound.call(this.task.cardList[destCardIndex], centerX, centerY, this, true);
                    this.task.cardList.splice(thisCardIndex, 1);
                    this.task.cardList.splice(destCardIndex, 0, this);
                } else {
                    this.task.cardList[destCardIndex].handleInsideBound.call(this.task.cardList[destCardIndex], centerX, centerY, this, false);
                    this.task.cardList.splice(thisCardIndex, 1);
                    this.task.cardList.splice(destCardIndex, 0, this);
                }
            }
            else { // different task Index

                const destinationTask = this.board.taskList[destTaskIndex];
                if(destinationTask.cardList.length == 0) { // empty list
                    destinationTask.insertCardNode.call(destinationTask, this);
                    this.task.cardList.splice(thisCardIndex, 1);
                    destinationTask.cardList.splice(destCardIndex, 0, this);
                } else {
                    destinationTask.cardList[destCardIndex].handleInsideBound.call(destinationTask.cardList[destCardIndex], centerX, centerY, this, false);
                    this.task.cardList.splice(thisCardIndex, 1);
                    destinationTask.cardList.splice(destCardIndex, 0, this);
                }
                this.task = destinationTask;
                this.cardListContainer = this.cardHolder.parentNode;
            }
            this.destinationIndex = destCardIndex;
        }

        const coords = this.getCurrentCoords(evt);
        this.card.style.left = coords.x + "px";
        this.card.style.top = coords.y + "px";

    }

    getCurrentCoords(evt) {
        const coordObj = {x: evt.clientX - this.board.startDrag.x, y: evt.clientY - this.board.startDrag.y};
        return coordObj;
    }

    handleInsideBound(x, y, card, prev) {
        const rect = getBoundingRect(this.cardHolder);

        if (rect.top < y && rect.bottom > y && (this !== card)) {
            const container = this.cardHolder.parentNode;
            if (prev) {
                container.insertBefore(card.cardHolder, this.cardHolder);
            } else if(!this.cardHolder.nextSibling) {
                container.appendChild(card.cardHolder);
            } else {
                container.insertBefore(card.cardHolder, this.cardHolder.nextSibling);
            }
        }
    }


    unsetDraggable() {
        if (!this.moving) return;

        this.cardHolder.appendChild(this.card);

        this.card.style.position = "static";
        this.card.style.left = null;
        this.card.style.top = null;

        this.card.style.zIndex = null;

        this.card.style.boxShadow = null;

        const parent = this.card.parentNode;
        parent.style.height = null;

        this.card.classList.toggle("card-list-dragging");

        this.moving = false;

        this.task.reorderCard(this.id, this.destinationIndex);
        // reset drag object
        this.board.unsetDraggable();

    }

    remove() {
        this.cardHolder.remove();
    }


    isInsideBoundY(y, card) {
        const rect = getBoundingRect(this.cardHolder);
        return (rect.top < y && rect.bottom > y); // && (this != card));
    }

    setDueDate(date) {
        const cardDetailDto = {
            id: this.id,
            endDate: date
        };
        fetchManager({
            url: "/api/cards/details/date/" + this.id,
            method: "POST",
            body: JSON.stringify(cardDetailDto),
            callback: this.board.calendar.constructCardCallBack.bind(this.board.calendar)
        });
    }

    setLabels(labels) {
        const cardDetailDto = {
            id: this.id,
            labels: labels
        };
        fetchManager({
            url: "/api/cards/details/label/" + this.id,
            method: "POST",
            body: JSON.stringify(cardDetailDto),
            callback: this.drawLabels.bind(this)
        });
    }

    drawLabels() {

    }



}