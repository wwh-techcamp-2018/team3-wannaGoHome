class Message {
    constructor(chat, message, reference) {
        this.chat = chat;
        this.message = message;
        this.reference = reference;

        // message node placeholder
        this.messageElem = undefined;

        this.init();
    }

    init() {
        if (this.reference) {
            this.handleNewerMessage.call(this, this.message, this.reference);
        } else {
            this.handleMessage.call(this, this.message);
        }

        if (this.selector(".profile-image")) {
            this.profileImage = this.selector(".profile-image");
            this.profileImage.addEventListener("load", function (evt) {
                if (imageDimensions(evt.currentTarget)) {
                    evt.currentTarget.classList.toggle("profile-image-wide")
                } else {
                    evt.currentTarget.classList.toggle("profile-image-long")
                }
                evt.currentTarget.style.display = "inline-block";
            }.bind(this));
        }
    }

    createMessageElement(message) {
        let messageTemplate;
        message.time = getFormattedTime(new Date(message.messageCreated));
        // author of message is current user
        if (message["author"]["id"] == this.chat.userId) {
            messageTemplate = Handlebars.templates["precompile/board/chat_message_right_template"];
        } else {
            messageTemplate = Handlebars.templates["precompile/board/chat_message_left_template"];
        }
        message.text = message.text.trim();
        const newMessage = createElementFromHTML(messageTemplate(message));
        // replacing innerHTML susceptible to html injection
        // newMessage.querySelector(".speech-bubble-text").innerHTML = message.text.replaceAll("\n", "<br />");

        // creating and assigning messageElem
        this.messageElem = newMessage;
        this.messageElem.querySelector(".speech-bubble-text").addEventListener("mouseup", function (evt) {
            evt.preventDefault();
            evt.stopPropagation();
        });
    }

    handleMessage(message) {
        this.createMessageElement(message);
        this.chat.messageHolder.appendChild(this.messageElem);
        this.chat.messageContainer.scrollTop = this.chat.messageContainer.scrollHeight;
        this.chat.newestMessageOrder = message.messageOrder;
        if (this.chat.oldestMessageOrder == -1) {
            this.chat.oldestMessageOrder = message.messageOrder;
        }
        this.chat.oldestMessageOrder = Math.min(this.chat.oldestMessageOrder, message.messageOrder);
    }

    handleNewerMessage(message, reference) {
        this.createMessageElement(message);
        this.chat.messageHolder.insertBefore(this.messageElem, reference);
        if (this.chat.oldestMessageOrder == -1) {
            this.chat.oldestMessageOrder = message.messageOrder;
        }
        this.chat.oldestMessageOrder = Math.min(this.chat.oldestMessageOrder, message.messageOrder);
    }

    selector(nodeSelector) {
        return this.messageElem.querySelector(nodeSelector);
    }

    equalsTime(messageInstance) {
        return getFormattedTime(new Date(this.message.messageCreated)) == getFormattedTime(new Date(messageInstance.message.messageCreated));
    }

    hideTime() {
        this.selector(".time-holder").style.display = "none";
    }

    equalsDate(messageInstance) {
        return getFormattedDate(new Date(this.message.messageCreated)) == getFormattedDate(new Date(messageInstance.message.messageCreated));
    }

    prependDateDivider() {
        const dateDividerTemplate = Handlebars.templates["precompile/board/date_divider_template"];
        const dateObject = {
            date: getFormattedDate(new Date(this.message.messageCreated))
        };
        const dateElem = createElementFromHTML(dateDividerTemplate(dateObject));
        this.messageElem.parentNode.insertBefore(dateElem, this.messageElem);
    }

    equalsAuthor(messageInstance) {
        return this.message.author.id == messageInstance.message.author.id;
    }

    hideAuthor() {
        if (this.messageElem.classList.contains("message-holder-left")) {
            this.selector(".profile-header").style.display = "none";
        }
        if (!this.messageElem.classList.contains("no-author")) {
            this.messageElem.classList.toggle("no-author");
        }
    }

    showAuthor() {
        if (this.messageElem.classList.contains("message-holder-left")) {
            this.selector(".profile-header").style.display = "block";
        }
        if (this.messageElem.classList.contains("no-author")) {
            this.messageElem.classList.toggle("no-author");
        }
    }

}