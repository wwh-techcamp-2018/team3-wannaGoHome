class Chat {
    constructor(chattingBar) {

        this.init();
        this.addListeners();
        this.connectSocket();
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



    connectSocket() {
        const socket = new SockJS('/websocket');
        this.stompClient = Stomp.over(socket);
        // this.stompClient.debug = null;
        this.stompClient.connect({}, function(frame) {
            this.stompClient.subscribe(`/topic/board/${this.boardIndex}/chat`, function(message) {
                console.log("Received Message");
            }.bind(this));
        }.bind(this));
    }

    sendMessage(obj) {
        if(obj.text) {
            this.stompClient.send(`/app/message/board/${this.boardIndex}/chat`, {}, JSON.stringify(obj));
        }
    }

    selector(nodeSelector) {
        return this.chattingBar.querySelector(nodeSelector);
    }
}