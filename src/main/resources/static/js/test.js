let select = document.getElementById("select");
let dinContainer = document.getElementById("dinContainer");
function checkselect() {

    if (select.value === "student") {
        dinContainer.style.display = "block";
    }
    else {
        dinContainer.style.display = "none";
    }
}