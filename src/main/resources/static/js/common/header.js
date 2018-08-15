document.addEventListener("DOMContentLoaded", function(evt) {
    init();

});

function init() {
    $(".header-button").addEventListener("click", (evt) => {
        console.log("click");
        console.log($(".header-button-boardlist").style.display);
        if($(".header-button-boardlist").style.display === 'none') {
            $(".header-button-boardlist").style.display = 'block';
        } else {
            $(".header-button-boardlist").style.display = 'none';
        }

    });
}