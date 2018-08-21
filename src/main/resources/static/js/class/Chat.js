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

    }

    addListeners() {
        this.selector(".chatting-input-holder button").addEventListener("click", function(evt) {
            evt.preventDefault();
            const obj = {};
            obj.text = this.selector(".chatting-input-holder textarea").value.trim();
            this.sendMessage(obj);
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

    setUserInfo(status, result) {
        this.userId = result.id;
    }


    connectSocket() {
        const socket = new SockJS('/websocket');
        this.stompClient = Stomp.over(socket);
        // this.stompClient.debug = null;
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

    handleMessage(message) {
        // author of message is current user
        let messageTemplate;
        if(message["author"]["id"] == this.userId) {
            messageTemplate = Handlebars.templates["precompile/board/chat_message_right_template"];
        } else {
            messageTemplate = Handlebars.templates["precompile/board/chat_message_left_template"];
        }
        const newMessage = createElementFromHTML(messageTemplate(message));
        this.messageHolder.appendChild(newMessage);
    }

    selector(nodeSelector) {
        return this.chattingBar.querySelector(nodeSelector);
    }
}