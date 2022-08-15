package general.controllers;

import general.controllers.forms.ClassForm;
import general.entities.*;
import general.reposes.UserRatingDTORepository;
import general.services.AnswerService;
import general.services.SchoolClassService;
import general.services.TaskService;
import general.services.UserService;
import general.utils.Result;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;

class TaskAndAnswerControllerTester {
    TaskService taskService = Mockito.mock(TaskService.class);
    AnswerService answerService = Mockito.mock(AnswerService.class);
    UserRatingDTORepository userRatingDTORepository = Mockito.mock(UserRatingDTORepository.class);
    SchoolClassService classService = Mockito.mock(SchoolClassService.class);
    UserService userService = Mockito.mock(UserService.class);
    Model model = new ConcurrentModel();
    Teacher teacher = new Teacher();
    SchoolClass schoolClass = new SchoolClass("Class", 11, null);
    {
        teacher.setId(100L);
        schoolClass.setId(100L);
    }

    ClassForm classForm = Mockito.mock(ClassForm.class);
    TaskAndAnswerController taskAndAnswerController =
            new TaskAndAnswerController(taskService, answerService, userRatingDTORepository, classService, userService);

    @Test
    void testAddTaskPost() {
        Mockito.when(taskService.checkInputData(any(), any(), any(), any(), any())).thenReturn(Result.Ok);
        Mockito.when(userService.getUserByName(any())).thenReturn(teacher);
        Mockito.when(classService.getClassById(any())).thenReturn(schoolClass);
        taskAndAnswerController.addTaskPost(null, null, classForm, null, model);
        assertEquals(model.getAttribute("completed"), "Задача для " + schoolClass.getNameWithNumber() + "класса добавлена");
        Mockito.when(taskService.checkInputData(any(), any(), any(), any(), any())).thenReturn(Result.Ok);
        taskAndAnswerController.addTaskPost(null, null, classForm, null, model);
        assertNull(model.getAttribute("error"));
    }

    @Test
    void testAddAnswerPost() {
        Mockito.when(taskService.getTaskById(any())).thenReturn(Mockito.mock(Task.class));
        Mockito.when(userService.getUserByName(any())).thenReturn(Mockito.mock(Student.class));
        taskAndAnswerController.addAnswerPost("10", null, model);
        assertEquals(model.getAttribute("completed"), "Ответ успешно добавлен");
        Mockito.when(answerService.getByStudentAndTask(any(), any())).thenReturn(Mockito.mock(Answer.class));
        assertEquals(taskAndAnswerController.addAnswerPost("10", null, model), "/taskAndAnswerControllerPages/AddAnswer");
    }

    @Test
    void testCheckAnswerPost() {
        Task task = new Task();
        task.setId(100L);
        task.setSchoolClass(schoolClass);
        Answer answer = new Answer();
        answer.setTeacher(teacher);
        answer.setTask(task);
        Mockito.when(userService.getUserByName(any())).thenReturn(teacher);
        Mockito.when(answerService.getAnswerById(any())).thenReturn(answer);
        assertEquals(taskAndAnswerController.checkAnswerPost("33", "3", null),
                "redirect:/answersOfTask/?taskId=" + task.getId() + "&classId=" + schoolClass.getId());
        task.setId(null);
        assertEquals(taskAndAnswerController.checkAnswerPost("33", "3", null),
                "redirect:/");
    }
}