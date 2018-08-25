class Chat {
    constructor(chattingBar) {

        this.handledIndexes = {};

        this.messageList = [];

        this.oldestMessageOrder = -1;
        this.newestMessageOrder = null;

        this.scrollTopThreshold = 10;

        this.init();
        this.addListeners();
        this.connectSocket();
        this.fetchUser();
    }

    init() {
        this.chattingBar = $_(".chatting-bar");
        this.messageContainer = $_(".chatting-bar-message-container");
        this.messageHolder = $_(".chatting-bar-message-holder");
        this.boardIndex = window.location.href.trim().split("/").pop();

    }

    addListeners() {
        this.selector(".chatting-input-holder button").addEventListener("click", function (evt) {
            evt.preventDefault();
            const obj = {};
            obj.text = this.selector(".chatting-input-holder textarea").value.trim();
            this.selector(".chatting-input-holder textarea").value = "";
            this.sendMessage(obj);
        }.bind(this));

        this.selector(".chatting-input-holder textarea").addEventListener("keypress", function (evt) {
            if (detectShiftEnter(evt)) {
                evt.preventDefault();
                pasteIntoInput(evt.currentTarget, "\n");
            } else if (detectEnter(evt)) {
                evt.preventDefault();
                this.selector(".chatting-input-holder button").click();
            }
        }.bind(this));

        this.selector(".chatting-bar-message-container").addEventListener("scroll", function (evt) {
            const holderScrollTop = evt.currentTarget.scrollTop;
            // 10 is a simple threshold
            if (holderScrollTop < this.scrollTopThreshold) {

                this.fetchOlderMessages();
            }
        }.bind(this));

        this.chattingBar.addEventListener("click", function (evt) {
            this.selector(".chatting-input-holder textarea").focus();
        }.bind(this));

    }

    fetchUser() {
        fetchManager({
            url: "/api/users/userId",
            method: "GET",
            headers: {"content-type": "application/json"},
            callback: this.setUserInfo.bind(this)
        });
    }

    fetchRecentMessages() {
        fetchManager({
            url: "/api/chat/getRecent/" + this.boardIndex,
            method: "GET",
            headers: {"content-type": "application/json"},
            callback: this.setFirstMessages.bind(this)
        });
    }

    fetchOlderMessages() {
        fetchManager({
            url: `/api/chat/getRecent/${this.boardIndex}/before/${this.oldestMessageOrder}`,
            method: "GET",
            headers: {"content-type": "application/json"},
            callback: this.setOlderMessages.bind(this)
        });
    }

    setUserInfo(status, result) {
        this.userId = result.id;
        this.fetchRecentMessages();
    }

    setFirstMessages(status, unsorted) {
        const result = unsorted.sort((a, b) => {
            return a.messageOrder - b.messageOrder
        });
        for (const message of result) {
            this.handleMessage(message);
        }
    }

    setOlderMessages(status, unsorted) {
        const result = unsorted.sort((a, b) => {
            return b.messageOrder - a.messageOrder;
        });
        let reference;
        for (const message of result) {
            this.saveHeight();
            reference = this.selector(".message-holder:first-child");
            this.handleNewerMessage(message, reference);
            this.adjustScrollTop();
        }

    }

    handleMessage(message) {
        if (this.handledIndexes[message.messageOrder]) {
            return;
        }
        this.handledIndexes[message.messageOrder] = true;
        const nextMessage = new Message(this, message);
        if (this.messageList.length > 0) {
            const beforeMessage = this.messageList[this.messageList.length - 1];
            this.handleSiblingMessage(beforeMessage, nextMessage);
        }
        this.messageList.push(nextMessage);
    }

    handleNewerMessage(message, reference) {
        if (this.handledIndexes[message.messageOrder]) {
            return;
        }
        this.handledIndexes[message.messageOrder] = true;
        const beforeMessage = new Message(this, message, reference);
        if (this.messageList.length > 0) {
            const nextMessage = this.messageList[0];
            this.handleSiblingMessage(beforeMessage, nextMessage);
        }
        this.messageList.splice(0, 1, beforeMessage);
    }

    handleSiblingMessage(beforeMessage, nextMessage) {
        if(!nextMessage.equalsDate(beforeMessage)) {
            nextMessage.prependDateDivider();
            return;
        }
        if (nextMessage.equalsAuthor(beforeMessage)) {
            nextMessage.hideAuthor();
            if (nextMessage.equalsTime(beforeMessage)) {
                beforeMessage.hideTime();
            } else {
                nextMessage.showAuthor();
            }
        }
    }

    saveHeight() {
        const rect = getBoundingRect(this.messageHolder);
        this.scrollHeight = rect.height;
    }

    adjustScrollTop() {
        const rect = getBoundingRect(this.messageHolder);
        const deltaScroll = rect.height - this.scrollHeight;
        const currentScrollTop = this.messageContainer.scrollTop;
        // this.messageContainer.scrollTop = currentScrollTop - deltaScroll;
    }

    connectSocket() {
        const socket = new SockJS('/websocket');
        this.stompClient = Stomp.over(socket);
        this.stompClient.debug = null;
        this.stompClient.connect({}, function (frame) {
            this.stompClient.subscribe(`/topic/board/${this.boardIndex}/chat`, function (message) {
                this.handleMessage(JSON.parse(message.body));
            }.bind(this));
        }.bind(this));
    }

    sendMessage(obj) {
        if (obj.text) {
            this.stompClient.send(`/app/message/board/${this.boardIndex}/chat`, {}, JSON.stringify(obj));
        }
    }

    selector(nodeSelector) {
        return this.chattingBar.querySelector(nodeSelector);
    }
}