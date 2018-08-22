class Notification {
    constructor() {
        this.connection = new SockJS("/websocket");
        this.client = Stomp.over(this.connection);

        this.client.connect({}, (frame) => {
            this.client.subscribe("/topic/activity/init", (frame) => {
                const {topics, messages} = JSON.parse(frame.body);
                this.initSubscription(topics);
            });

            this.client.send("/app/activity/init");
        })
    }

    initSubscription(topics) {
        topics.forEach((topic) => {
            this.client.subscribe(topic, (message) => {
                console.log(message);
            })
        });
    }
}