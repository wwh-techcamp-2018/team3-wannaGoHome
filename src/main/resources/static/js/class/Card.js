class Card {
    constructor(card) {
        this.id = card.id;
        // this.authorId = card.author.id;
        //TODO this.boardId = 가져오기?
        this.title = card.title;
        this.description = card.description;
        this.endDate = card.endDate;
        this.startDate = card.createDate;
    }

}