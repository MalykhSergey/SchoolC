package general.controllers;

import general.controllers.forms.ClassForm;
import general.controllers.forms.SchoolForm;
import general.controllers.forms.UserForm;
import general.entities.*;
import general.services.SchoolClassService;
import general.services.SchoolService;
import general.services.UserService;
import general.utils.ResultOfInputDataChecking;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;

class UserControllerTest {
    UserService userService = Mockito.mock(UserService.class);
    PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
    SchoolService schoolService = Mockito.mock(SchoolService.class);
    SchoolClassService schoolClassService = Mockito.mock(SchoolClassService.class);
    Model model = new ConcurrentModel();
    SchoolForm schoolForm = new SchoolForm();
    School school = Mockito.mock(School.class);
    User admin = new User("Admin", null, null, Role.Admin);
    User operator = new User("Operator", null, null, Role.Operator);
    SchoolClass schoolClass = new SchoolClass("Class",11,school);
    Student student = new Student("Student", null, Role.Student, school, schoolClass);
    {
        schoolForm.setSchoolName("School");
    }
    UserController userController = new UserController(userService, passwordEncoder, schoolService, schoolClassService);

    void testAddUserPost(String status, String message, School school, SchoolForm schoolForm,
                         ResultOfInputDataChecking resultOfInputDataChecking, UserForm userForm, ClassForm classForm, SchoolClass schoolClass) {
        Mockito.when(userService.getUserByName(any())).thenReturn(admin);
        Mockito.when(schoolService.getSchoolByName(any())).thenReturn(school);
        Mockito.when(userService.checkUserFormForCreate(any())).thenReturn(resultOfInputDataChecking);
        Mockito.when(schoolClassService.getClassById(anyLong())).thenReturn(schoolClass);
        Mockito.when(schoolClassService.getClassByNameAndNumberAndSchool(any(), anyInt(), any())).thenReturn(schoolClass);
        userController.addUserPost(userForm, schoolForm, classForm, model);
        assertEquals(model.getAttribute(status), message);
    }

    @Test
    void testAddUserPostOnSchoolError() {
        testAddUserPost("error", "Неверно указана школа", null,
                schoolForm, null, null, null, null);
    }

    @Test
    void testAddUserPostOnClassError() {
        UserForm userForm = new UserForm();
        userForm.setRole(Role.Student);
        ClassForm classForm = new ClassForm();
        classForm.setClassId(100L);
        testAddUserPost("error", "Неверно указан класс", school, schoolForm,
                new ResultOfInputDataChecking(true, null), userForm, classForm, null);
        classForm.setClassId(null);
        classForm.setClassName("");
        classForm.setClassNumber(11);
        testAddUserPost("error", "Неверно указан класс", school, schoolForm,
                new ResultOfInputDataChecking(true, null), userForm, classForm, null);
    }

    @Test
    void testAddUserPostOnUserFormError() {
        testAddUserPost("error", "user form error", school, schoolForm,
                new ResultOfInputDataChecking(false, "user form error"), null, null, null);
    }

    @Test
    void testAddUserPostOnCompleted() {
        UserForm userForm = new UserForm();
        userForm.setRole(Role.Student);
        ClassForm classForm = new ClassForm();
        classForm.setClassId(100L);
        testAddUserPost("completed", "Пользователь с именем: " + userForm.getUserName() +
                        " был успешно добавлен", school, schoolForm,
                new ResultOfInputDataChecking(true, null), userForm, classForm, schoolClass);
    }

    void testSetClassForStudentPost(String status, String message, Student student, SchoolClass schoolClass) {
        Mockito.when(userService.getUserByName(any())).thenReturn(student);
        Mockito.when(userService.getCurrentUserName()).thenReturn("Operator");
        Mockito.when(userService.getUserByName("Operator")).thenReturn(operator);
        Mockito.when(userService.getUserByName("Admin")).thenReturn(admin);
        Mockito.when(schoolClassService.getClassById(anyLong())).thenReturn(schoolClass);
        userController.setClassForStudentPost(Mockito.mock(UserForm.class), Mockito.mock(ClassForm.class), model);
        Mockito.when(userService.getCurrentUserName()).thenReturn("Admin");
        assertEquals(model.getAttribute(status), message);
        userController.setClassForStudentPost(Mockito.mock(UserForm.class), Mockito.mock(ClassForm.class), model);
        assertEquals(model.getAttribute(status), message);
    }

    @Test
    void testSetClassForStudentPostOnUserError() {
        testSetClassForStudentPost("error", "Введите корректные данные (пользователь не найден)",
                null, schoolClass);
    }

    @Test
    void testSetClassForStudentPostOnClassError() {
        testSetClassForStudentPost("error", "Введите корректные данные (класс не найден)",
                student, null);
    }

    @Test
    void testSetClassForStudentPostOnCompleted() {
        testSetClassForStudentPost("completed", "Ученик привязан к классу",
                student, schoolClass);
    }
}