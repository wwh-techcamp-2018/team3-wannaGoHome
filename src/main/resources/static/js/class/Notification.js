class Notification {
    constructor(header) {
        console.log(header);
        this.connection = new SockJS("/websocket");
        this.client = Stomp.over(this.connection);

        this.button = header.querySelector(".header-notification-button");
        this.clock = this.button.firstChild;
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

        this.button.addEventListener("click", (evt) => {
            this.onClickButton();
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
        this.appendNotification(body);
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

    onClickButton() {
        this.holder.classList.toggle("header-notification-hide");
        if(!this.holder.classList.contains("header-notification-hide")) {
            this.clock.classList.remove("notification-swing");
        }
    }
}