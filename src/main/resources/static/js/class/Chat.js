class Chat {
    constructor(chattingBar) {

        this.init();
        this.addListeners();
        this.connectSocket();
        this.fetchUser();
    }

    init() {
        this.chattingBar = $_(".chatting-bar");
        this.messageHolder = $_(".chatting-bar-message-holder");
        this.boardIndex = window.location.href.trim().split("/").pop();

        this.oldestMessageOrder = -1;
        this.newestMessageOrder = null;
    }

    addListeners() {
        this.selector(".chatting-input-holder button").addEventListener("click", function(evt) {
            evt.preventDefault();
            const obj = {};
            obj.text = this.selector(".chatting-input-holder textarea").value.trim();
            this.selector(".chatting-input-holder textarea").value = "";
            console.log(this.selector(".chatting-input-holder textarea").value.length);
            this.sendMessage(obj);
        }.bind(this));

        this.selector(".chatting-input-holder textarea").addEventListener("keypress", function(evt) {
            if(detectShiftEnter(evt)) {
                evt.preventDefault();
                pasteIntoInput(evt.currentTarget, "\n");
            } else if(detectEnter(evt)) {
                evt.preventDefault();
                this.selector(".chatting-input-holder button").click();
            }
        }.bind(this));

        this.selector(".chatting-bar-message-holder").addEventListener("scroll", function(evt) {
            const holderScrollTop = evt.currentTarget.scrollTop;
            // 10 is a simple threshold
            if(holderScrollTop < 10) {

                this.fetchOlderMessages();
            }
        }.bind(this));

        this.chattingBar.addEventListener("click", function(evt) {
            this.selector(".chatting-input-holder textarea").focus();
        }.bind(this));

    }

    fetchUser() {
        fetchManager({
            url: "/api/users/userId",
            method: "GET",
            headers: {"content-type": "application/json"},
            callback : this.setUserInfo.bind(this)
        });
    }

    fetchRecentMessages() {
        fetchManager({
            url: "/api/chat/getRecent/" + this.boardIndex,
            method: "GET",
            headers: {"content-type": "application/json"},
            callback : this.setFirstMessages.bind(this)
        });
    }

    fetchOlderMessages() {
        fetchManager({
            url: `/api/chat/getRecent/${this.boardIndex}/before/${this.oldestMessageOrder}`,
            method: "GET",
            headers: {"content-type": "application/json"},
            callback : this.setOlderMessages.bind(this)
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
        for(const message of result) {
            this.handleMessage(message);
        }
    }

    setOlderMessages(status, unsorted) {
        const result = unsorted.sort((a, b) => {
            return a.messageOrder - b.messageOrder;
        });
        const reference = this.selector(".message-holder:first-child");
        for(const message of result) {
            this.handleNewerMessage(message, reference);
        }
    }

    connectSocket() {
        const socket = new SockJS('/websocket');
        this.stompClient = Stomp.over(socket);
        this.stompClient.debug = null;
        this.stompClient.connect({}, function(frame) {
            this.stompClient.subscribe(`/topic/board/${this.boardIndex}/chat`, function(message) {
                this.handleMessage(JSON.parse(message.body));
            }.bind(this));
        }.bind(this));
    }

    sendMessage(obj) {
        if(obj.text) {
            this.stompClient.send(`/app/message/board/${this.boardIndex}/chat`, {}, JSON.stringify(obj));
        }
    }

    createMessageElement(message) {
        // author of message is current user
        let messageTemplate;
        if(message["author"]["id"] == this.userId) {
            messageTemplate = Handlebars.templates["precompile/board/chat_message_right_template"];
        } else {
            messageTemplate = Handlebars.templates["precompile/board/chat_message_left_template"];
        }
        const newMessage = createElementFromHTML(messageTemplate(message));
        return newMessage;
    }

    handleMessage(message) {
        const newMessage = this.createMessageElement(message);
        this.messageHolder.appendChild(newMessage);
        this.messageHolder.scrollTop = this.messageHolder.scrollHeight;
        this.newestMessageOrder = message.messageOrder;
        if(this.oldestMessageOrder == -1) {
            this.oldestMessageOrder = message.messageOrder;
        }
        this.oldestMessageOrder = Math.min(this.oldestMessageOrder, message.messageOrder);
    }

    handleNewerMessage(message, reference) {
        const newMessage = this.createMessageElement(message);
        this.messageHolder.insertBefore(newMessage, reference);
        if(this.oldestMessageOrder == -1) {
            this.oldestMessageOrder = message.messageOrder;
        }
        this.oldestMessageOrder = Math.min(this.oldestMessageOrder, message.messageOrder);
    }

    selector(nodeSelector) {
        return this.chattingBar.querySelector(nodeSelector);
    }
}