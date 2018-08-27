class MyPage {
    constructor(profileNode, profileAvatarNode, teamNode, activitiesNode) {
        this.profileHolder = profileNode;
        this.teamHolder = teamNode;
        this.activitiyHolder = activitiesNode;
        this.profileImegeHolder = new Profile(this.profileHolder, profileAvatarNode);
        this.init();
    }

    init() {
        this.addLoadMoreActivityClickEvent();
        fetchManager({
            url: "/api/users/profile",
            method: "GET",
            callback: this.drawMyPage.bind(this)
        })
    }

    addLoadMoreActivityClickEvent() {
        this.activitiyHolder.querySelector(".show-more").addEventListener("click", (evt) => {
            evt.stopPropagation();
            if (this.activitiyHolder.querySelector(".activity-list").childElementCount !== 0) {
                this.loadMoreActivity();
            }
        });
    }

    addProfileImgClickEvent() {
        this.profileHolder.querySelector(".profile-image").addEventListener("click", (evt) => {
            evt.stopPropagation();
            this.profileImegeHolder.showProfileImageHolder();
        });
    }


    drawMyPage(status, response) {
        this.drawProfile(response.user);
        this.drawTeam(response.teams);
        this.drawActivity(response.activities);
        this.addProfileImgClickEvent();
    }

    drawProfile(user) {
        const template = Handlebars.templates["precompile/mypage/mypage_profile_template"];
        this.profileHolder.appendChild(createElementFromHTML(template(user)));
    }

    drawTeam(teams) {
        const template = Handlebars.templates["precompile/mypage/mypage_team_template"];
        const teamList = this.teamHolder.querySelector(".team-list");
        teams.forEach((team) => {
            teamList.appendChild(createElementFromHTML(template(team)));
        });
    }

    drawActivity(activities) {
        const template = Handlebars.templates["precompile/mypage/mypage_activity_template"];
        const activityList = this.activitiyHolder.querySelector(".activity-list");
        activities.forEach((activity) => {
            activityList.appendChild(createElementFromHTML(template(activity)));
        });
    }

    loadMoreActivity() {
        const body = {
            registeredDate:
                this.getActivityRegisteredDate(this.activitiyHolder.querySelector(".activity-list").lastChild)
        }
        fetchManager({
            url: "/api/activities/fetch",
            method: "POST",
            body: JSON.stringify(body),
            callback: this.handleLoadMoreActivity.bind(this)
        })
    }

    handleLoadMoreActivity(status, response) {
        this.drawActivity(response);
    }

    getActivityRegisteredDate(activity) {
        return activity.querySelector(".activity-time").innerText;
    }

}