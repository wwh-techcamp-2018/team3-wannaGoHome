const BLANK = '&nbsp;&nbsp;#';
class Calendar {
    constructor() {
        this.board;
        this.calendar = new tui.Calendar(document.getElementById('calendar'), {
            disableDblClick: true,
            defaultView: 'month',
            taskView: true,
            scheduleView: true,
            template: {
                task: function (schedule) {
                    return BLANK + schedule.title;
                },
            },
            month: {
                daynames: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
                startDayOfWeek: 0,
            },
            week: {
                daynames: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
                startDayOfWeek: 0,
            }
        });
        this.clickSchedule();
        this.clickCalendar();
        this.dragSchedule();
        this.cardList= [];
        // this.calendar.createSchedules(this.cardList);
     }


    constructCard(cards) {
        this.cards = cards;
        this.cards.forEach((card)=>{
            this.constructCardForm(card);
        });
        this.calendar.clear();
        this.calendar.createSchedules(this.cardList);
    }

    clearCalendar() {
        this.cardList = [];
        this.calendar.clear();
    }

    constructCardCallBack(status, card) {
        if(status === 200) {
            this.constructCardForm(card);
        }
    }

    constructCardForm(card) {
        this.cardList.push({
            id: card.id,
            calendarId: this.board.boardIndex,
            title: card.title,
            category: 'time',
            dueDateClass: '',
            start: card.createDate,
            end: card.endDate,
            isAllDay: true,
            bgColor: this.getRandomColor()
        });
        this.calendar.clear();
        this.calendar.createSchedules(this.cardList);

    }

    getRandomColor() {
        const colorList = ['#ffe5d4', '#D9C4DE', '#d1e1bf', '#f3ff99','#ffd5b8', '#e5e5d8', '#f9a287', '#ff6a6a', '#dddfff', '#70ae9b', '#ffe8ab' , '#7e98b0'];
        return colorList[Math.floor(Math.random() * colorList.length)];
    }

    clickSchedule() {
        this.calendar.on({
            'clickSchedule': function (e) {
                console.log(e.schedule);
                this.board.cardDetailForm.show(e.schedule.id);
            }.bind(this)
        })
    }
    clickCalendar() {
        this.calendar.on({
            'beforeCreateSchedule': function(e) {
                e.guide.clearGuideElement();
            }

        });
    }
    dragSchedule() {
        this.calendar.on('beforeUpdateSchedule', function(event) {
            var schedule = event.schedule;
            var startTime = event.start;
            var endTime = event.end;
            this.calendar.updateSchedule(schedule.id, schedule.calendarId, {
                start: startTime,
                end: endTime
            });

            let mm = (startTime.getMonth()+1 > 9 ? '' : '0') +(startTime.getMonth()+1);
            const startDate = startTime.getFullYear() +"-" + mm + "-" + startTime.getDate();
            mm = (endTime.getMonth()+1 > 9 ? '' : '0') +(endTime.getMonth()+1);
            const endDate = endTime.getFullYear() +"-" + mm + "-" + endTime.getDate();

            const cardDetailDto = {
                endDate: endDate,
                createDate: startDate
            }
            fetchManager({
                url: "/api/cards/"+schedule.id+"/date",
                method: "PUT",
                body: JSON.stringify(cardDetailDto),
                callback: this.handleUpdateSchedule.bind(this)

            })
        }.bind(this));
    }

    handleUpdateSchedule(status, card) {
        if(status !== 200) {
            return;
        }
    }


}