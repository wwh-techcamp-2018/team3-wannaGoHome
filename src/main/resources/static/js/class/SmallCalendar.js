class SmallCalendar {
    constructor() {
        this.cardDetail;
        this.calendar = new tui.Calendar(document.getElementById('smallCalendar'), {
            disableDblClick: true,
            defaultView: 'month',
            taskView: true,
            scheduleView: true,
            template: {
                task: function (schedule) {
                    return BLANK + schedule.title;
                },
                allday: function(schedule) {
                    return schedule.title + '<i class="fa fa-refresh"></i>';
                }
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
        // this.clickSchedule();
        this.clickCalendar();
        // this.dragSchedule();
        this.cardList= [];
    }

    createSchedule() {
        this.calendar.createSchedules();
    }

    clearCalendar() {
        this.calendar.clear();
    }

    drawDueDate(card) {
        console.log(card.endDate);

    }

    // constructCardCallBack(status, card) {
    //     if(status === 200) {
    //         this.constructCardForm(card);
    //     }
    // }
    //
    // constructCardForm(card) {
    //     this.cardList=[];
    //     this.cardList.push({
    //         id: card.id,
    //         calendarId: card.id,
    //         title: card.title,
    //         category: 'time',
    //         dueDateClass: '',
    //         start: card.createDate,
    //         end: card.endDate,
    //         isAllDay: true,
    //         bgColor: this.getRandomColor()
    //     });
    //     this.calendar.clear();
    //     this.calendar.createSchedules(this.cardList);
    //
    // }
    //
    //
    // getRandomColor() {
    //     const colorList = ['#ffe5d4', '#D9C4DE', '#d1e1bf', '#f3ff99','#ffd5b8', '#e5e5d8', '#f9a287', '#ff6a6a', '#dddfff', '#70ae9b', '#ffe8ab' , '#7e98b0'];
    //     return colorList[Math.floor(Math.random() * colorList.length)];
    // }

    // clickSchedule() {
    //     this.calendar.on({
    //         'clickSchedule': function (e) {
    //             console.log(e.schedule);
    //             //TODO e.schedule로 카드 상세정보 페이지 띄울수 있음(카드상세페이지 생기면 연결)
    //         }
    //     })
    // }
    clickCalendar() {
        this.calendar.on({
            'beforeCreateSchedule': function(e) {
                this.cardDetail.setDueDate(e.end._date);
                console.log(e.guide);
                console.log(e);
                // e.guide.clearGuideElement();
            }.bind(this)

        });
    }
    // dragSchedule() {
    //     this.calendar.on('beforeUpdateSchedule', function(event) {
    //         var schedule = event.schedule;
    //         var startTime = event.start;
    //         var endTime = event.end;
    //         this.calendar.updateSchedule(schedule.id, schedule.calendarId, {
    //             start: startTime,
    //             end: endTime
    //         });
    //     }.bind(this));
    // }


}