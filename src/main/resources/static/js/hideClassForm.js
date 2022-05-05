let select = document.getElementById("role");
let dinContainer = document.getElementById("dinContainer");
dinContainer.style.display = "none";

function checkSelect() {

    if (select.value === "Student") {
        dinContainer.style.display = "block";
        dinContainer.required = true;
    } else {
        dinContainer.style.display = "none";
        dinContainer.required = false;
    }
}