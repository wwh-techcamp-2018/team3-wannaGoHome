class SignForm {
    constructor(form, url, redirectUrl) {
        this.form = form;
        this.url = url;
        this.redirectUrl = redirectUrl;
        this.inputForms = {};
        this.defaultPlaceHolder = {};
        this.initInputForms();
    }

    initInputForms() {
        const inputForms = this.form.querySelectorAll("input");
        inputForms.forEach((input) => {
            const dataType = input.getAttribute("data-type");
            if (dataType)
                this.defaultPlaceHolder[dataType] = input.placeholder;
                this.inputForms[dataType] = input;
        });

        this.form.querySelector(".sign-button").addEventListener("click", (evt) => {
            evt.preventDefault();
            this.onClickSubmit();
        });

        this.form.addEventListener("input", (evt) => {
            this.clearCaution(evt.target.getAttribute("data-type"));
        });
    }

    onClickSubmit() {
        const data = {};
        Object.keys(this.inputForms).map((key) => {
            data[key] = this.inputForms[key].value;
        });
        fetchRequest({
            url: this.url,
            method: "POST",
            body: JSON.stringify(data),
            callback: this.handleSign.bind(this)
        });
    }

    handleSign(status, result) {
        if (status === 201 || status === 200) {
            window.location.href = this.redirectUrl;
            return;
        }

        this.clearCautions();
        result.forEach(({errorType, message}) => {
            this.showCaution(errorType, message);
        });
    }

    clearCaution(field) {
        const inputForm = this.inputForms[field];
        if (inputForm.classList.contains("caution-on"))
            inputForm.classList.toggle("caution-on");
        inputForm.placeholder = this.defaultPlaceHolder[field];
    }

    showCaution(field, message) {
        const inputForm = this.inputForms[field];
        if (!inputForm.classList.contains("caution-on"))
            inputForm.classList.toggle("caution-on");
        inputForm.placeholder = message;
        inputForm.value = null;
    }

    clearCautions() {
        for (let field in this.inputForms) {
            this.clearCaution(field);
        }
    }
}