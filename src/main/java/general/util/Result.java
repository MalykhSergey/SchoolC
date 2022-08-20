package general.util;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT )
public enum Result {
    AnswerIsExists("Ответ уже есть!"),
    AnswerIsNotExists("Ответ не существует!"),
    UserIsExists("Это имя уже занято!"),
    TaskIsExists("Задача для этого класса с таким именем уже есть!"),
    TeacherIsLinked("Учитель уже привязан!"),
    ClassIsExists("Такой класс уже есть!"),
    SchoolIsExists("Такая школа уже существует!"),
    NameIsNull("Введите имя!"),
    PasswordIsNull("Введите пароль!"),
    InvalidDate("Неправильная дата"),
    InvalidName("Неверное имя!"),
    InvalidRating("Неверная оценка!"),
    InvalidTeacher("Вы не можете оценить этот ответ!"),
    InvalidClassName("Неверный класс!"),
    InvalidClassNumber("Неверный номер класса!"),
    InvalidSchoolName("Неверная школа!"),
    TooShortClassName("Имя класса слишком короткое! Имя должно быть " + StringLengthConstants.ClassName.getLengthDescription()),
    TooLongClassName("Имя класса слишком длинное! Имя должно быть " + StringLengthConstants.ClassName.getLengthDescription()),
    TooShortSchoolName("Имя школы слишком короткое! Имя должно быть " + StringLengthConstants.ClassName.getLengthDescription()),
    TooLongSchoolName("Имя школы слишком длинное! Имя должно быть " + StringLengthConstants.ClassName.getLengthDescription()),
    TooShortPassword("Пароль слишком короткий! Пароль должен быть " + StringLengthConstants.Password.getLengthDescription()),
    TooLongPassword("Пароль слишком длинный! Пароль должен быть " + StringLengthConstants.Password.getLengthDescription()),
    TooShortName("Имя слишком короткое! Имя должно быть " + StringLengthConstants.Name.getLengthDescription()),
    TooLongName("Имя слишком длинное! Имя должно быть" + StringLengthConstants.Name.getLengthDescription()),
    TooShortTaskBody("Введите более полное описание задания! Описание задания должно быть " + StringLengthConstants.TaskBody.getLengthDescription()),
    TooLongTaskBody("Введите более короткое описание задания! Описание задания должно быть " + StringLengthConstants.TaskBody.getLengthDescription()),
    TooShortTaskName("Введите более полное название задания! Название задания должно быть " + StringLengthConstants.TaskName.getLengthDescription()),
    TooLongTaskName("Введите более короткое название задания! Название задания должно быть " + StringLengthConstants.TaskName.getLengthDescription()),
    TooLongAnswerBody("Введите более короткий ответ! Ответ должен быть " + StringLengthConstants.AnswerBody.getLengthDescription()),
    TooShortAnswerBody("Введите более длинный ответ! Ответ должен быть " + StringLengthConstants.AnswerBody.getLengthDescription()),
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
