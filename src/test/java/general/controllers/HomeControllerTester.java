package general.controllers;

import general.entities.*;
import general.services.AnswerService;
import general.services.TaskService;
import general.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

class HomeControllerTester {
    UserService userService = Mockito.mock(UserService.class);
    AnswerService answerService = Mockito.mock(AnswerService.class);
    TaskService taskService = Mockito.mock(TaskService.class);
    Model model = Mockito.mock(Model.class);
    HomeController homeController = new HomeController(userService, answerService, taskService);
    User user;

    @Test
    public void homeForAdmin() {
        user = new User("Admin", "password", null, null);
        Mockito.when(userService.getUserByName(any())).thenReturn(user);
        assertEquals(homeController.home(null,model), "Home");
    }

    @Test
    public void homeForTeacher() {
        user = new Teacher("Teacher", "password", null);
        Mockito.when(userService.getUserByName(any())).thenReturn(user);
        assertEquals(homeController.home(null,model), "TeacherHome");
    }

    @Test
    public void homeForStudent() {
        user = new Student("Student", "password", null, Mockito.mock(SchoolClass.class));
        Mockito.when(userService.getUserByName(any())).thenReturn(user);
        assertEquals(homeController.home(null,model), "StudentHome");
    }
}