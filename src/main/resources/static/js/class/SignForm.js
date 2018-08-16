class SignForm {
    constructor(form, url) {
        this.form = form;
        this.url = url;
        this.inputForms = {};
        this.cautions = {};
        this.initInputForms();
    }

    initInputForms() {
        const inputForms = this.form.querySelectorAll("input");
        inputForms.forEach((input) => {
            const dataType = input.getAttribute("data-type");
            if (dataType)
                this.inputForms[dataType] = input;
        });

        const cautions = this.form.querySelectorAll(".caution");
        cautions.forEach((caution) => {
            this.cautions[caution.getAttribute("data-type")] = caution;
        });

        this.form.querySelector(".sign-button").addEventListener("click", (evt) => {
            evt.preventDefault();
            this.onClickSubmit();
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
            window.location.href = "/";
            return;
        }

        this.clearCautions();
        result.forEach(({errorType, message}) => {
            this.showCaution(errorType, message);
        });
    }

    showCaution(errorType, message) {
        const caution = this.cautions[errorType];
        caution.style.display = "block";
        caution.innerHTML = message;
    }

    clearCautions() {
        for (let field in this.cautions) {
            this.cautions[field].style.display = "none";
        }
    }
}