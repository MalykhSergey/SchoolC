package general.utils;

public enum Result {
    AnswerIsExists("Ответ уже есть!"),
    AnswerIsNotExists("Ответ не существует!"),
    NameIsNull("Введите имя!"),
    PasswordIsNull("Введите пароль!"),
    SchoolIsExists("Такая школа уже существует!"),
    RequiredFullName("Введите полное название!"),
    AllValuesRequired("Введите все значения!"),
    InvalidDate("Неправильная дата"),
    InvalidName("Неверное имя!"),
    InvalidRating("Неверная оценка!"),
    InvalidTeacher("Вы не можете оценить этот ответ!"),
    InvalidClassName("Неверный класс!"),
    InvalidSchoolName("Неверная школа!"),
    TooShortPassword("Пароль слишком короткий! Пароль должен быть " + StringLengthConstants.Password.getLengthDescription()),
    TooLongPassword("Пароль слишком длинный! Пароль должен быть " + StringLengthConstants.Password.getLengthDescription()),
    TooShortName("Имя слишком короткое! Имя должно быть " + StringLengthConstants.Name.getLengthDescription()),
    TooLongName("Имя слишком длинное! Имя должно быть" + StringLengthConstants.Name.getLengthDescription()),
    TooShortTaskBody("Введите более полное описание задания! Описание задания должно быть "+ StringLengthConstants.TaskBody.getLengthDescription()),
    TooLongTaskBody("Введите более короткое описание задания! Описание задания должно быть "+ StringLengthConstants.TaskBody.getLengthDescription()),
    TooShortTaskName("Введите более полное название задания! Название задания должно быть "+ StringLengthConstants.TaskName.getLengthDescription()),
    TooLongTaskName("Введите более короткое название задания! Название задания должно быть "+ StringLengthConstants.TaskName.getLengthDescription()),
    TooLongAnswerBody("Введите более короткий ответ! Ответ должен быть "+ StringLengthConstants.AnswerBody.getLengthDescription()),
    TooShortAnswerBody("Введите более длинный ответ! Ответ должен быть "+ StringLengthConstants.AnswerBody.getLengthDescription()),
    Ok();
    private final String error;

    Result(String error) {

        this.error = error;
    }

    Result() {
        error = null;
    }

    public String getError() {
        return error;
    }
}
