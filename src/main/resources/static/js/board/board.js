document.addEventListener("DOMContentLoaded", function(evt) {
   const board = new Board();

   const chat = new Chat();

    window.addEventListener("resize", function(evt) {
        let chattingBarWidth = getBoundingRect($_(".chatting-bar")).width;
        $_(".board-scroll-container").style.width = document.documentElement.clientWidth - chattingBarWidth + "px";

        let navbarHeight = getBoundingRect($_("header")).height;
        $_(".chatting-bar").style.height = document.documentElement.clientHeight - navbarHeight + "px";
        $_(".chatting-bar").style.top = navbarHeight + "px";

        let chattingBarHeight = getBoundingRect($_(".chatting-bar")).height;
        let chattingBarTitleHeight = getBoundingRect($_(".chatting-bar-title")).height;
        let chattingInputHolderHeight = getBoundingRect($_(".chatting-input-holder")).height;
        $_(".chatting-bar-message-holder").style.height = chattingBarHeight - chattingBarTitleHeight - chattingInputHolderHeight - 36 + "px";
    });

    window.dispatchEvent(new Event("resize"));

});

