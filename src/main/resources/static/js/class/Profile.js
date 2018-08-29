class Profile {
    constructor(profileHolder, profileAvatorHolder) {
        this.profileHolder = profileHolder;
        this.profileAvatorHolder = profileAvatorHolder;
        this.init();
    }

    init() {
        this.addCloseButtonClickEvent();
        this.addInitialClickEvent();
        this.addUploadImageClickEvent();
        this.addUploadImageOninputEvent();
    }

    addCloseButtonClickEvent() {
        this.profileAvatorHolder.querySelector(".profile-avatar-holder-icon").addEventListener("click", (evt) => {
            this.hideProfileImageHolder();
        });
        $_(".header-logo").addEventListener("click", (evt) => {
            evt.stopPropagation();
            window.location.href = "/";
        });
        document.addEventListener("click", (evt) => {
            if (this.profileAvatorHolder.style.display === "block") {
                this.hideProfileImageHolder();
            }
        });
    }

    addInitialClickEvent() {
        this.profileAvatorHolder.querySelector(".avatar-option").addEventListener("click", (evt) => {
            evt.stopPropagation();
            fetchManager({
                url : "/api/users/profile/init",
                method : "POST",
                callback : this.handleInitProfile.bind(this)
            })
        });
    }

    addUploadImageClickEvent() {
        this.profileAvatorHolder.querySelector(".profile-upload").addEventListener("click", (evt) =>{
            evt.stopPropagation();
        });
    }

    addUploadImageOninputEvent() {
        this.profileAvatorHolder.querySelector(".profile-upload").oninput = function(evt) {
            const uploadFiles = this.profileAvatorHolder
                .querySelector(".profile-upload-button").files;
            if(uploadFiles.length !== 0) {
                fileFetchManager({
                    url: "/api/users/profile",
                    body: getFileFormData(uploadFiles),
                    callback: this.handleUploadProfileImage.bind(this)
                })
            }
        }.bind(this);
    }

    showProfileImageHolder() {
        this.profileAvatorHolder.style.display = "block";
    }

    hideProfileImageHolder() {
        this.hideProfileImageErrorMessage();
        this.profileAvatorHolder.style.display = "none";
    }

    showProfileImageErrorMessage() {
        this.profileAvatorHolder.querySelector(".profile-upload-error").style.display = "block";
    }

    hideProfileImageErrorMessage() {
        this.profileAvatorHolder.querySelector(".profile-upload-error").style.display = "none";
    }

    handleInitProfile(status, response) {
        this.changeProfileImage(response.profile);
        this.hideProfileImageHolder();
    }

    handleUploadProfileImage(status, response) {
        if(status === 200) {
            // this.profileAvatorHolder.querySelector(".profile-upload").reset();
            this.changeProfileImage(response.profile);
            this.hideProfileImageHolder();
            return;
        }
        this.profileAvatorHolder.querySelector(".profile-upload-error").innerText = response[0].message;
        this.showProfileImageErrorMessage();

    }

    changeProfileImage(imgUrl) {
        const profileImageNode = this.profileHolder.querySelector(".profile-image-section");
        profileImageNode.src = imgUrl;
        this.hideProfileImageHolder();
    }


}