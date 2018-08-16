class SignForm {
    constructor(form, url) {
        this.form = form;
        this.url = url;
        this.inputForms = {};
        this.initInputForms();

        console.log(this.inputForms);
    }

    initInputForms() {
        const inputForms = this.form.querySelectorAll("input");
        inputForms.forEach((input) => {
            this.inputForms[input.type] = input;
        });
    }
}