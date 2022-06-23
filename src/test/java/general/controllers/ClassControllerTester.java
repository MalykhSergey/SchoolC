package general.controllers;

import general.controllers.forms.ClassForm;
import general.controllers.forms.UserForm;
import general.entities.*;
import general.services.SchoolClassService;
import general.services.SchoolService;
import general.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;

public class ClassControllerTester {

    UserService userService = Mockito.mock(UserService.class);
    SchoolService schoolService = Mockito.mock(SchoolService.class);
    SchoolClassService classService = Mockito.mock(SchoolClassService.class);
    ClassController classController = new ClassController(classService, userService, schoolService);
    SchoolClass schoolClass = Mockito.mock(SchoolClass.class);
    School school = Mockito.mock(School.class);
    User admin = new User("Admin", "password", school, Role.Admin);
    Teacher teacher = new Teacher("Teacher", "password", school, Role.Teacher);
    Model model = new ConcurrentModel();

    public void testAddClassPost(User user, School school, boolean bool1, boolean bool2, String status, String message) {
        Mockito.when(userService.getUserByName(any())).thenReturn(user);
        Mockito.when(schoolService.getSchoolByName(any())).thenReturn(school);
        Mockito.when(classService.checkClassName(any())).thenReturn(bool1);
        Mockito.when(classService.isClassExistsInSchool(any(), anyInt(), any())).thenReturn(bool2);
        classController.addClassPost(schoolClass, "SchoolName", 11, model);
        assertEquals(model.getAttribute(status), message);
    }

    @Test
    public void testAddClassPostOnCompleted() {
        testAddClassPost(this.admin, this.school, true, false, "completed", "null класс был добавлен");
    }

    @Test
    public void testAddClassPostOnSchoolError() {
        testAddClassPost(this.admin, null, true, false, "error", "Вы неверно указали школу");
    }

    @Test
    public void testAddClassPostOnClassNameError() {
        testAddClassPost(this.admin, this.school, false, false, "error", "Введите название класса короче 20 символов");
    }

    @Test
    public void testAddClassPostOnClassError() {
        testAddClassPost(this.admin, this.school, true, true, "error", "Такой класс уже есть");
    }

    public void testAddClassForTeacherPost(User teacher, SchoolClass schoolClass, String status, String message) {
        Mockito.when(userService.getCurrentUserName()).thenReturn("Admin");
        Mockito.when(userService.getUserByName("Admin")).thenReturn(admin);
        User operator = new User();
        operator.setRole(Role.Operator);
        Mockito.when(userService.getUserByName("Operator")).thenReturn(operator);
        Mockito.when(userService.getUserByName(any())).thenReturn(teacher);
        Mockito.when(classService.getClassByNameAndNumberAndSchool(any(),anyInt(),any())).thenReturn(schoolClass);
        classController.addClassForTeacherPost(Mockito.mock(UserForm.class), Mockito.mock(ClassForm.class),model);
        Mockito.when(userService.getCurrentUserName()).thenReturn("Operator");
        classController.addClassForTeacherPost(Mockito.mock(UserForm.class), Mockito.mock(ClassForm.class),model);
        assertEquals(model.getAttribute(status), message);
    }

    @Test
    public void testAddClassForTeacherPostOnCompleted() {
        testAddClassForTeacherPost(teacher,schoolClass,"completed","Учитель: " + teacher.getName() + " привязан к классу!");
    }

    @Test
    public void testAddClassForTeacherPostOnErrors() {
        testAddClassForTeacherPost(admin,schoolClass,"error","Введите корректные данные (пользователь не найден)");
        testAddClassForTeacherPost(teacher,null,"error","Введите корректные данные (класс не найден)");
    }
}
