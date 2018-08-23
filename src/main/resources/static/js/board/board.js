document.addEventListener("DOMContentLoaded", function (evt) {
    const board = new Board();
    const calendar = new Calendar();
    initEvent(calendar);


    const chat = new Chat();

    window.addEventListener("resize", function (evt) {
        let chattingBarWidth = getBoundingRect($_(".chatting-bar")).width;
        $_(".board-scroll-container").style.width = document.documentElement.clientWidth - chattingBarWidth + "px";
        $_(".board-header").style.width = document.documentElement.clientWidth - chattingBarWidth + "px";

        let navbarHeight = getBoundingRect($_("header")).height;
        $_(".chatting-bar").style.height = document.documentElement.clientHeight - navbarHeight + "px";
        $_(".chatting-bar").style.top = navbarHeight + "px";

        let chattingBarHeight = getBoundingRect($_(".chatting-bar")).height;
        let chattingBarTitleHeight = getBoundingRect($_(".chatting-bar-title")).height;
        let chattingInputHolderHeight = getBoundingRect($_(".chatting-input-holder")).height;
        $_(".chatting-bar-message-holder").style.height = chattingBarHeight - chattingBarTitleHeight - chattingInputHolderHeight - 36 + "px";
    });

    $_(".chatting-bar .chatting-bar-closer").addEventListener("click", function (evt) {
        $_(".board-header-side-menu .chatting-bar-closer").style.display = "inline-block";
        $_(".chatting-bar").style.width = "0px";
        window.dispatchEvent(new Event("resize"));
    });

    $_(".board-header-side-menu .chatting-bar-closer").addEventListener("click", function (evt) {
        $_(".chatting-bar").style.width = "320px";
        $_(".board-header-side-menu .chatting-bar-closer").style.display = "none";
        window.dispatchEvent(new Event("resize"));
    });

    window.dispatchEvent(new Event("resize"));

    $_(".board-header-side-menu .fa-calendar").addEventListener("click", (evt) => {
        evt.stopPropagation();
        if ($_("#calendar").style.display == 'block') {
            $_("#calendar").style.display = 'none';
            calendar.clearCalendar();
        } else {
            //카드 날짜 정하기 전까지 임시 카드리스트
            const cards = [];
            calendar.constructCard(cards);
            $_("#calendar").style.display = 'block';
        }
    });

});

function initEvent(calendar) {
    document.addEventListener("click", (evt) => {
        $_("#calendar").style.display = 'none';
        calendar.clearCalendar();
    });

    $_("#calendar").addEventListener("click", (evt) => {
        evt.stopPropagation();
    })

}

