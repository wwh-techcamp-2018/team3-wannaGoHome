class Notification {
    constructor(header) {
        this.connection = new SockJS("/websocket");
        this.client = Stomp.over(this.connection);

        this.notificationButton = header.querySelector(".header-notification-button");
        this.showMoreButton = header.querySelector(".header-notification-more");

        this.bell = this.notificationButton.firstChild;
        this.holder = header.querySelector(".header-notification-holder");
        this.ul = header.querySelector(".header-notification-ul");
        this.scrollable = header.querySelector(".header-notification-scrollable");

        this.template = Handlebars.templates["precompile/header_notification_template"];

        this.client.connect({}, (frame) => {
            this.initSubscribeId = this.client.subscribe("/topic/activity/init", (frame) => {
                this.initSubscribeId.unsubscribe();
                const {topic, messages} = JSON.parse(frame.body);
                this.initSubscription(topic);
                this.handleNotification(messages);

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
        this.subscribeTopic("/topic/user/" + topic, this.handleNotification.bind(this));
    }

    subscribeTopic(topic, callback) {
        this.client.subscribe(topic, (frame) => {
            callback(JSON.parse(frame.body));
        });
    }

    handleNotification(body) {
        body.forEach((notification) => {
            if (this.oldestActivityDate < notification.registeredDate)
                this.prependNotification(notification);
            else
                this.appendNotification(notification);
        });
        this.swingNotification();
    }

    swingNotification() {
        if (this.holder.classList.contains("header-notification-hide")) {
            this.bell.classList.add("notification-swing");
        }
    }

    appendNotification(notification) {
        this.ul.appendChild(createElementFromHTML(this.template(notification)));
        this.scrollable.scrollTop = this.scrollable.scrollHeight;
    }

    prependNotification(notification) {
        this.ul.prepend(createElementFromHTML(this.template(notification)));
        this.scrollable.scrollTop = this.scrollable.scrollHeight;
    }

    onClickNotificationButton() {
        this.holder.classList.toggle("header-notification-hide");
        if (!this.holder.classList.contains("header-notification-hide")) {
            this.bell.classList.remove("notification-swing");
        }
    }

    onClickShowMoreButton() {
        this.client.send(
            "/app/activity/fetch",
            {},
            JSON.stringify(
                {registeredDate: this.oldestActivityDate}
            )
        );
    }

    get oldestActivityDate() {
        if (this.ul.lastElementChild)
            return this.ul.lastElementChild.querySelector("div > p").innerText;
        else
            return "";
    }
}