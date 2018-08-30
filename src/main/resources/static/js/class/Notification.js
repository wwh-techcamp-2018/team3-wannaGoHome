class Notification {
    constructor(header) {
        this.connection = new SockJS("/websocket");
        this.stompClient = Stomp.over(this.connection);
        this.stompClient.debug = false;

        this.notificationButton = header.querySelector(".header-notification-button");
        this.showMoreButton = header.querySelector(".header-notification-more");

        this.bell = this.notificationButton.firstChild;
        this.holder = header.querySelector(".header-notification-holder");
        this.ul = header.querySelector(".header-notification-ul");
        this.scrollable = header.querySelector(".header-notification-scrollable");

        this.template = Handlebars.templates["precompile/header_notification_template"];

        this.stompClient.connect({}, (frame) => {
            this.initSubscribeId = this.stompClient.subscribe("/topic/activity/init", (frame) => {
                this.initSubscribeId.unsubscribe();
                const {topic, messages} = JSON.parse(frame.body);
                this.initSubscription(topic);
                this.handleNotification(messages);
                this.bell.classList.remove("notification-swing");

            });

            this.stompClient.send("/app/activity/init");
        });

        document.addEventListener("click", (evt) => {
            this.hideNotification();
        });

        this.showMoreButton.addEventListener("click", (evt) => {
            evt.stopPropagation();
            this.onClickShowMoreButton();
        });

        this.notificationButton.addEventListener("click", (evt) => {
            evt.stopPropagation();
            this.showNotification();
            $_(".header-button-boardlist").style.display = 'none';
            const calendar = $_("#calendar");
            if(calendar) {
                calendar.style.display = 'none';
            }
        });

        this.holder.addEventListener("click", (evt) => {
            evt.stopPropagation();
        });

    }

    initSubscription(topic) {
        this.subscribeTopic("/topic/user/" + topic, this.handleNotification.bind(this));
    }

    subscribeTopic(topic, callback) {
        this.stompClient.subscribe(topic, (frame) => {
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
        const elem = createElementFromHTML(this.template(notification));
        this.ul.appendChild(elem);
        elem.addEventListener("mouseover", function(evt) {
            $_(".header-notification-extra").innerHTML = elem.querySelector("a").text;
            $_(".header-notification-extra").style.display = "inline-block";
            const holderRect = getBoundingRect($_(".header-notification-holder"));
            const thisRect = getBoundingRect(this);
            console.log(holderRect.right, evt.pageX);
            $_(".header-notification-extra").style.right = (holderRect.right - thisRect.left) + "px";
            $_(".header-notification-extra").style.top = (thisRect.bottom - holderRect.top) - $_("body").scrollTop - 10 + "px";
        });
        elem.addEventListener("mouseleave", function(evt) {
            $_(".header-notification-extra").style.display = "none";
        });
        this.scrollable.scrollTop = this.scrollable.scrollHeight;
    }

    prependNotification(notification) {
        const elem = createElementFromHTML(this.template(notification));
        this.ul.prepend(elem);
        elem.addEventListener("mouseover", function(evt) {
            $_(".header-notification-extra").innerHTML = elem.querySelector("a").text;
            $_(".header-notification-extra").style.display = "inline-block";
            const holderRect = getBoundingRect($_(".header-notification-holder"));
            const thisRect = getBoundingRect(this);
            console.log(holderRect.right, evt.pageX);
            $_(".header-notification-extra").style.right = (holderRect.right - thisRect.left) + "px";
            $_(".header-notification-extra").style.top = (thisRect.bottom - holderRect.top) - $_("body").scrollTop - 10 + "px";
        });
        elem.addEventListener("mouseleave", function(evt) {
            $_(".header-notification-extra").style.display = "none";
        });
        this.scrollable.scrollTop = this.scrollable.scrollHeight;
    }

    showNotification() {
        this.holder.classList.toggle("header-notification-hide");
        this.bell.classList.remove("notification-swing");
    }

    hideNotification() {
        if(!this.holder.classList.contains("header-notification-hide")) {
            this.holder.classList.toggle("header-notification-hide");
        }
    }

    onClickShowMoreButton() {
        this.stompClient.send(
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