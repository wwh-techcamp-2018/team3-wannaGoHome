let cardList = [];
document.addEventListener("DOMContentLoaded", function(evt) {
   const board = new Board();
   cardList.push(new Card(1, 'card_title', 'card_desc', -5, -3));
   cardList.push(new Card(2, 'card_title2', 'card_desc2',-10,  -3));
   cardList.push( new Card(3, 'card_title3', 'card_desc3', -2, 2));
   cardList.push(new Card(4, 'card_title4', 'card_desc4', -2, 4));
   cardList.push(new Card(5, 'card_title5', 'card_desc5', -7, -4));
   const calendar = new Calendar();
   checkClickCalendar(calendar);

});

function checkClickCalendar(calendar) {
    $_(".board-header-side-menu .fa-calendar").addEventListener("click", (evt)=>{
        if($_("#calendar").style.display == 'block') {
            $_("#calendar").style.display = 'none';
            calendar.clearCalendar();
        } else {
            calendar.constructCard(cardList);
            $_("#calendar").style.display = 'block';
        }
    });

}


