package general.utils;

public enum Result {
    NameIsNull("Введите имя!"),
    PasswordIsNull("Введите пароль!"),
    SchoolIsExists("Такая школа уже существует!"),
    RequiredFullName("Введите полное название!"),
    AllValuesRequired("Введите все значения!"),
    InvalidName("Неверное имя!"),
    InvalidClassName("Неверный класс!"),
    InvalidSchoolName("Неверная школа!"),
    TooShortPassword("Пароль слишком короткий!"),
    TooLongPassword("Пароль слишком длинный!"),
    TooShortName("Имя слишком короткое"),
    TooLongName("Имя слишком длинное"),
    TooShortDescription("Введите более полное описание или название задания"),
    TooLongDescription("Введите более короткое описание или название задания"),
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
