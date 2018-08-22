class Notification {
    constructor() {
        this.connection = new SockJS("/websocket");
        this.client = Stomp.over(this.connection);

        this.client.connect({}, (frame) => {
            this.client.subscribe("/topic/activity/init", (frame) => {
                const {code, topics, messages} = JSON.parse(frame.body);
                this.initSubscription(code, topics);
            });

            this.client.send("/app/activity/init");
        })
    }

    initSubscription(code, topics) {
        topics.forEach((topic) => {
            this.client.subscribe(topic, (message) => {
                console.log(message);
            })
        });

        this.client.subscribe("/topic/user/" + code, (message) => {
            console.log(message);
        });
    }
}