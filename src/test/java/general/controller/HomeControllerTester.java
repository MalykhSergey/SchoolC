package general.controller;

import general.controller.dto.StudentsTasksAndAnswers;
import general.entity.*;
import general.service.TaskService;
import general.service.UserService;
import general.util.UserDetailsExtended;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

class HomeControllerTester {
    UserService userService = Mockito.mock(UserService.class);
    TaskService taskService = Mockito.mock(TaskService.class);
    Model model = Mockito.mock(Model.class);
    HomeController homeController = new HomeController(userService, taskService);
    User user;
    UserDetailsExtended userDetailsExtended;

    @Test
    public void homeForAdmin() {
        user = new User("Admin", "password", null, Role.Admin);
        userDetailsExtended = new UserDetailsExtended(user);
        assertEquals(homeController.home(null, userDetailsExtended, model), "Home");
    }

    @Test
    public void homeForTeacher() {
        user = new Teacher("Teacher", "password", null);
        userDetailsExtended = new UserDetailsExtended(user);
        assertEquals(homeController.home(null, userDetailsExtended, model), "TeacherHome");
    }

    @Test
    public void homeForStudent() {
        user = new Student("Student", "password", null, Mockito.mock(SchoolClass.class));
        userDetailsExtended = new UserDetailsExtended(user);
        Mockito.when(taskService.getTasksAndAnswersByStudent(any())).thenReturn(Mockito.mock(StudentsTasksAndAnswers.class));
        assertEquals(homeController.home(null, userDetailsExtended, model), "StudentHome");
    }
}