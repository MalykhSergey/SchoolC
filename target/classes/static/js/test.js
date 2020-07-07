let select = document.getElementById("select");
let form = document.getElementById("form");
let newinput = document.createElement("input");
newinput.type = "text";
newinput.name = "class";
newinput.placeholder = "Введите класс";
let active;
function checkselect(){
    
if (select.value === "student"){
    form.insertBefore(newinput, form.getElementsByTagName("input")[1]);
}
else if (form[1].name === "class"){
    form.removeChild(form[1]);
}
}