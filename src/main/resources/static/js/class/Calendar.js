const BLANK = '&nbsp;&nbsp;#';
class Calendar {
    constructor() {
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
     }


    constructCard(cards) {
        this.cards = cards;
        this.cardList = [];
        this.cards.forEach((card)=>{
            this.constructCardForm(card);
        });
        this.calendar.createSchedules(this.cardList);
    }

    clearCalendar() {
        this.calendar.clear();
    }

    constructCardForm(card) {
        this.cardList.push({
            id: card.id,
            calendarId: '1', //TODO boardId로
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

    clickSchedule() {
        this.calendar.on({
            'clickSchedule': function (e) {
                console.log(e.schedule);
                //TODO e.schedule로 카드 상세정보 페이지 띄울수 있음(카드상세페이지 생기면 연결)
            }
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
            //TODO schedule에서 업데이트 된 정보를 카드에도 업데이트 시켜줘야함
        }.bind(this));
    }


}