let form = document.getElementById("form");
let warningDiv = null;
let inpForms = [];
inpForms.push(document.querySelector(".fileForm"));
inpForms[0].addEventListener("change", checkForm);
function checkForm(event) {
    if (warningDiv !=null){
        warningDiv.remove();
    }
    for (let k = 0; k<inpForms.length; k++){
        if (inpForms[k] != event.target){
            if (event.target.value == inpForms[k].value){
                if (warningDiv == null){
                    warningDiv =  document.createElement("div");
                    warningDiv.classList.add("alert", "alert-danger");
                    warningDiv.role = "alert";
                    warningDiv.textContent = "Такой файл уже есть";
                    document.querySelector("#warning").append(warningDiv);}
            }
        }
    }
    let newFileForm = document.createElement("input");
    let newButton = document.createElement("button");
    let newDiv = document.createElement("div");
    newDiv.classList.add("oneForm", "mt-2", "mb-2");
    newButton.classList.add("btn", "btn-outline-danger","btn-sm")
    newButton.textContent = "X"
    newButton.type="button";
    newButton.addEventListener("click",removeForm);
    newFileForm.type = "file";
    newFileForm.classList.add("fileForm");
    newFileForm.name = "files";
    inpForms.push(newFileForm);
    newDiv.append(newButton);
    newDiv.insertBefore(newFileForm, newButton);
    form.insertBefore(newDiv, event.target.parentNode.nextSibling)
    newFileForm.addEventListener("change",checkForm);
    ;
}
function removeForm(event) {
    form.removeChild(event.target.parentNode);
}