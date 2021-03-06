document.addEventListener("DOMContentLoaded", function (evt) {

    const calendar = new Calendar();
    const board = new Board();
    initEvent(calendar, board);
    board.calendar = calendar;
    calendar.board = board;

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
        $_(".chatting-bar-message-container").style.height = chattingBarHeight - chattingBarTitleHeight - chattingInputHolderHeight - 36 + "px";
    });

    $_(".chatting-bar .chatting-bar-closer").addEventListener("click", function (evt) {
        $_(".board-header .chatting-bar-closer").style.display = "inline-block";
        $_(".chatting-bar").style.width = "0px";
        window.dispatchEvent(new Event("resize"));
    });

    $_(".board-header .chatting-bar-closer").addEventListener("click", function (evt) {
        $_(".chatting-bar").style.width = "320px";
        $_(".board-header .chatting-bar-closer").style.display = "none";
        window.dispatchEvent(new Event("resize"));
    });

    window.dispatchEvent(new Event("resize"));

    $_(".board-header .fa-calendar").parentNode.addEventListener("click", (evt) => {
        evt.stopPropagation();
        showCalender(calendar);
    });

    $_("#calendar").addEventListener("click", (evt) => {
        evt.stopPropagation()
    });

});

function initEvent(calendar, board) {
    document.addEventListener("click", (evt) => {
        $_("#calendar").style.display = 'none';
        calendar.clearCalendar();
        board.hideCardDetailForm();

    });

    document.addEventListener("keyup", (evt) => {
        if (evt.key === "Escape") {
            document.querySelector("body").click();
        }

    })

    $_(".fa-calendar").parentNode.addEventListener("click", (evt) => {
        evt.stopPropagation();
        $_(".header-button-boardlist").style.display = 'none';
    });

    $_(".card-detail-container").addEventListener("click", (evt) => {
        evt.stopPropagation();
        $_(".card-detail-assignee-container").classList.add("card-detail-assignee-container-hide");
        $_(".card-detail-label-container").style.display = 'none';
        $_(".card-detail-date-container").style.display = 'none';
        $_(".card-detail-summary-attachment-list").style.display = 'none';
    })

}

function getCalendarCardList(calendar, boardId) {
    fetchManager({
        url: "/api/boards/" + boardId + "/cards",
        method: "GET",
        callback: drawCalendarCardList.bind(calendar)
    });
}

function drawCalendarCardList(status, cards) {
    if (status === 200) {
        this.constructCard(cards);
    }
}

function showCalender(calendar) {
    if ($_("#calendar").style.display == 'block') {
        $_("#calendar").style.display = 'none';
        calendar.clearCalendar();
    } else {
        //카드 날짜 정하기 전까지 임시 카드리스트
        getCalendarCardList(calendar, calendar.board.boardIndex);
        $_("#calendar").style.display = 'block';
    }
}


