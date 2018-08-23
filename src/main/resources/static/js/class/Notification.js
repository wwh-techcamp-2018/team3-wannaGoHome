class Notification {
    constructor(header) {
        console.log(header);
        this.connection = new SockJS("/websocket");
        this.client = Stomp.over(this.connection);

        this.notificationButton = header.querySelector(".header-notification-button");
        this.showMoreButton = header.querySelector(".header-notification-more");
        this.clock = this.notificationButton.firstChild;
        this.holder = header.querySelector(".header-notification-holder");
        this.recentActivityLabel = this.holder.firstElementChild;
        this.template = Handlebars.templates["precompile/header_notification_template"];

        this.maxActivityCount = 10;

        this.client.connect({}, (frame) => {
            this.initSubscribeId = this.client.subscribe("/topic/activity/init", (frame) => {
                this.initSubscribeId.unsubscribe();
                const {topic, messages} = JSON.parse(frame.body);
                this.initSubscription(topic);
                messages.forEach(this.appendNotification.bind(this))

            });

            this.client.send("/app/activity/init");
        });

        this.notificationButton.addEventListener("click", (evt) => {
            this.onClickNotificationButton();
        });
        this.showMoreButton.addEventListener("click", (evt) => {
            this.onClickShowMoreButton();
        });
    }

    initSubscription(topic) {
        console.log("initSubscription is called");
        this.subscribeTopic("/topic/user/" + topic, this.handleNotification.bind(this));
    }

    subscribeTopic(topic, callback) {
        this.client.subscribe(topic, (frame) => {
            callback(JSON.parse(frame.body));
        });
    }

    handleNotification(body) {
        console.log("handleNotification is called with body: {}", body);
        body.forEach((notification) => {
            this.appendNotification(notification);
        });
        this.swingNotification();
    }

    swingNotification() {
        if(this.holder.classList.contains("header-notification-hide")) {
            this.clock.classList.add("notification-swing");
        }
    }

    appendNotification(body) {
        if (this.holder.childElementCount - 1 === this.maxActivityCount) {
            this.holder.lastElementChild.remove();
        }
        this.recentActivityLabel.insertAdjacentElement("afterend", createElementFromHTML(this.template(body)));
    }

    onClickNotificationButton() {
        this.holder.classList.toggle("header-notification-hide");
        if(!this.holder.classList.contains("header-notification-hide")) {
            this.clock.classList.remove("notification-swing");
        }
    }

    onClickShowMoreButton() {
        this.client.send("/app/activity/")
    }
}