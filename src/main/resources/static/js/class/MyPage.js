class MyPage {
    constructor(profileNode, profileAvatarNode, teamNode, activitiesNode) {
        this.profileHolder = profileNode;
        this.teamHolder = teamNode;
        this.activitiyHolder = activitiesNode;
        this.profileImegeHolder = new Profile(this.profileHolder, profileAvatarNode);
        limitInputSize(this.profileHolderSelector(".content-update-input"), 10);
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
        this.profileHolderSelector(".profile-image").addEventListener("click", (evt) => {
            evt.stopPropagation();
            this.profileImegeHolder.showProfileImageHolder();
        });
    }

    addEditProfileClickEvent() {
        this.profileHolderSelector(".profile-edit-button").addEventListener("click", (evt) => {
            evt.stopPropagation();
            this.profileHolderSelector(".content-update-input").value
                = this.profileHolderSelector(".profile-name").innerHTML;
            this.showProfileContentUpdateHolder();
        })
    }

    addContentUpdateSubmitClickEvent() {
        this.profileHolderSelector(".content-update-submit").addEventListener("click", (evt) => {
            evt.stopPropagation();
            const name = this.profileHolderSelector(".content-update-input").value;
            const obj = {
                "name" : name
            }
            fetchManager({
                url : "/api/users/profile",
                body : JSON.stringify(obj),
                method : "PUT",
                callback : this.handleProfileContentUpdate.bind(this)
            })
        })
    }
    addContentUpdateCancelClickEvent() {
        this.profileHolderSelector(".content-update-cancel").addEventListener("click", (evt) => {
            evt.stopPropagation();
            this.hideProfileContentUpdateHolder();
        })
    }

    addProfileImageLoadedEvent() {
        this.profileHolderSelector(".profile-image-section").addEventListener("load", (evt) => {
            if(imageDimensions(evt.currentTarget)) {
                evt.currentTarget.classList.toggle("profile-image-wide")
            } else {
                evt.currentTarget.classList.toggle("profile-image-long")
            }
            evt.currentTarget.style.display = "inline-block";
        });
    }

    showProfileContentUpdateHolder() {
        this.profileHolderSelector(".profile-content-update-holder").style.display = "block";
        this.profileHolderSelector(".profile-content-description").style.display = "none";
        this.profileHolderSelector(".profile-edit-button").style.display = "none";
    }

    hideProfileContentUpdateHolder() {
        this.profileHolderSelector(".profile-content-update-holder").style.display = "none";
        this.profileHolderSelector(".profile-content-description").style.display = "block";
        this.profileHolderSelector(".profile-edit-button").style.display = "block";
        this.profileHolderSelector(".content-update-input").placeholder = "";

    }


    drawMyPage(status, response) {
        this.drawProfile(response.user);
        this.drawTeam(response.teams);
        this.drawActivity(response.activities);
        this.addProfileImgClickEvent();
        this.addEditProfileClickEvent();
        this.addContentUpdateSubmitClickEvent();
        this.addContentUpdateCancelClickEvent();

    }

    drawProfile(user) {
        const template = Handlebars.templates["precompile/mypage/mypage_profile_template"];
        this.profileHolder.appendChild(createElementFromHTML(template(user)));
        this.addProfileImageLoadedEvent();
        limitInputSize(this.profileHolderSelector(".content-update-input"), 10);
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

    handleProfileContentUpdate(status, response) {
        if(status === 200) {
            this.profileHolderSelector(".profile-name").innerHTML = response.name;
            this.hideProfileContentUpdateHolder();
            return;
        }

        const contentUpdateInputNode = this.profileHolderSelector(".content-update-input");
        contentUpdateInputNode.value = "";
        contentUpdateInputNode.placeholder = response[0].message;
    }

    getActivityRegisteredDate(activity) {
        return activity.querySelector(".activity-time").innerText;
    }

    profileHolderSelector(selector) {
        return this.profileHolder.querySelector(selector);
    }

}