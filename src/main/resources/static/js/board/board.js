document.addEventListener("DOMContentLoaded", function(evt) {
   const board = new Board();
   // const calendar = new Calendar();
   //  checkClickCalendar(calendar);
   //  initEvent(calendar);


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

    $_(".chatting-bar-closer").addEventListener("click", function(evt) {
       console.log("Close!");
       const icon = evt.currentTarget.querySelector("i");
        if(icon.classList.contains("fa-comment-dots")) {
            $_(".chatting-bar").style.width = "320px";
        } else {
            $_(".chatting-bar").style.width = "0px";

        }

        icon.classList.toggle("fa-comment-dots");
        icon.classList.toggle("fa-angle-right");
        window.dispatchEvent(new Event("resize"));
    });

    window.dispatchEvent(new Event("resize"));
});

function initEvent(calendar) {
    document.addEventListener("click", (evt)=>{
        $_("#calendar").style.display = 'none';
        calendar.clearCalendar();
    });

    $_("#calendar").addEventListener("click", (evt)=>{
        evt.stopPropagation();
    })

}
//
// function checkClickCalendar(calendar) {
//     $_(".board-header-side-menu .fa-calendar").addEventListener("click", (evt)=>{
//         evt.stopPropagation();
//         if($_("#calendar").style.display == 'block') {
//             $_("#calendar").style.display = 'none';
//             calendar.clearCalendar();
//         } else {
//             calendar.constructCard(cards);
//             $_("#calendar").style.display = 'block';
//         }
//     });
//
// }


