let cards = [];
document.addEventListener("DOMContentLoaded", function(evt) {
   const board = new Board();
   const calendar = new Calendar();
   fetchCards();
    checkClickCalendar(calendar);
    initEvent(calendar);
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
function fetchCards() {
    fetchManager({
        url: "/api/cards/calendar",
        method: "GET",
        callback: this.drawCards.bind(this)
    })
}

function drawCards(status, result) {
    if(status === 200) {
        result.map(card => cards.push(new Card(card)));
    }
}

function checkClickCalendar(calendar) {
    $_(".board-header-side-menu .fa-calendar").addEventListener("click", (evt)=>{
        evt.stopPropagation();
        if($_("#calendar").style.display == 'block') {
            $_("#calendar").style.display = 'none';
            calendar.clearCalendar();
        } else {
            calendar.constructCard(cards);
            $_("#calendar").style.display = 'block';
        }
    });

}


