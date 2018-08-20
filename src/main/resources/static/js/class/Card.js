class Card {
    constructor(id, title, description, start, end) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.endDate = new Date();
        this.endDate.setDate(this.endDate.getDate() + end);
        this.startDate = new Date();
        this.startDate.setDate(this.startDate.getDate() + start);
    }

}