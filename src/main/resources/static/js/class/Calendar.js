class Calendar {
    constructor() {
        this.calendar = new tui.Calendar(document.getElementById('calendar'), {
            disableDblClick: true,
            defaultView: 'month',
            taskView: true,    // can be also ['milestone', 'task']
            scheduleView: true,  // can be also ['allday', 'time']
            template: {
                task: function (schedule) {
                    return '&nbsp;&nbsp;#' + schedule.title;
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

        this.dragSchedule();
     }

    constructCard(cards) {
        this.cards = cards;
        this.cardList = [];
        this.cards.forEach((card)=>{
            this.constructCardForm(this.cardList, card);
        });
        this.calendar.createSchedules(this.cardList);
    }

    clearCalendar() {
        this.calendar.clear();
    }

    constructCardForm(cardList, card) {
        cardList.push({
            id: card.id,
            calendarId: '1',
            title: card.title,
            category: 'time',
            dueDateClass: '',
            start: card.startDate,
            end: card.endDate,
            isAllDay: true,
            bgColor: this.getRandomColor()
        });
    }

    getRandomColor() {
        const colorList = ['#ffe5d4', '#D9C4DE', '#d1e1bf', '#f3ff99','#ffd5b8', '#e5e5d8', '#f9a287', '#ff6a6a', '#dddfff', '#70ae9b', '#ffe8ab' , '#7e98b0'];
        return colorList[Math.floor(Math.random() * colorList.length)];
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
        }.bind(this));
    }


}