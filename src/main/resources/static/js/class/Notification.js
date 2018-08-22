class Notification {
    constructor() {
        this.connection = new SockJS("/websocket");
        this.client = Stomp.over(this.connection);

        this.client.connect({}, (frame) => {
            this.client.subscribe("/topic/activity/init", (frame) => {
                const {topic, messages} = JSON.parse(frame.body);
                this.initSubscription(topic);
            });

            this.client.send("/app/activity/init");
        })
    }

    initSubscription(topic) {
        this.subscribeTopic("/topic/user/" + topic, (body)=> {console.log(body)});
    }

    subscribeTopic(topic, callback) {
        this.client.subscribe(topic, (frame) => {
            callback(JSON.parse(frame.body));
        });
    }
}