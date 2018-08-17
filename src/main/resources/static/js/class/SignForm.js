class SignForm {
    constructor(form, url, redirectUrl) {
        this.form = form;
        this.url = url;
        this.redirectUrl = redirectUrl;
        this.inputForms = {};
        this.defaultPlaceHolder = {};
        this.signButton;
        this.initInputForms();
    }

    initInputForms() {
        const inputForms = this.form.querySelectorAll("input");
        inputForms.forEach((input) => {
            const dataType = input.getAttribute("data-type");
            if (dataType) {
                this.defaultPlaceHolder[dataType] = input.placeholder;
                this.inputForms[dataType] = input;
            }
        });
        this.inputForms["email"].focus();

        this.signButton = this.form.querySelector(".sign-button");
        this.signButton.addEventListener("click", (evt) => {
            evt.preventDefault();
            this.onClickSubmit();
        });

        this.form.addEventListener("input", (evt) => {
            this.clearCaution(evt.target.getAttribute("data-type"));
            this.handleButtonActive();
        });
    }

    handleButtonActive() {
        const formKeys = Object.keys(this.inputForms);
        const filledFormCount = formKeys.filter((field) => this.inputForms[field].value).length;
        if (filledFormCount === formKeys.length) {
            this.signButton.classList.remove("inactive");
            return;
        }
        this.signButton.classList.add("inactive");
    }

    onClickSubmit() {
        if (this.signButton.classList.contains("inactive")) {
            return;
        }

        const data = {};
        for (let field in this.inputForms) {
            data[field] = this.inputForms[field].value.trim();
        }
        fetchManager({
            url: this.url,
            method: "POST",
            body: JSON.stringify(data),
            callback: this.handleSign.bind(this)
        });
    }

    handleSign(status, result) {
        if (status === 201 || status === 200) {
            window.location.href = this.redirectUrl;
        }

        this.clearCautions();
        result.forEach(({errorType, message}) => {
            this.showCaution(errorType, message);
        });
    }

    clearCaution(field) {
        const inputForm = this.inputForms[field];
        inputForm.classList.remove("caution-on");
        inputForm.placeholder = this.defaultPlaceHolder[field];
    }

    showCaution(field, message) {
        const inputForm = this.inputForms[field];
        inputForm.classList.add("caution-on");
        inputForm.placeholder = message;
        inputForm.value = null;
    }

    clearCautions() {
        for (let field in this.inputForms) {
            this.clearCaution(field);
        }
    }
}