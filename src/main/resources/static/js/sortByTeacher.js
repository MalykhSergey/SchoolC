let teacherName = document.getElementById('teacherNames');
function redirectForSort() {
    if (teacherName.value !== '')
        window.location.href = '?teacherName=' + teacherName.value
    else window.location.href = '/'
}