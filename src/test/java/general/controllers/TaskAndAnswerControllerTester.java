package general.controllers;

import general.controllers.forms.ClassForm;
import general.entities.*;
import general.reposes.UserRatingDTORepository;
import general.services.AnswerService;
import general.services.SchoolClassService;
import general.services.TaskService;
import general.services.UserService;
import general.utils.CheckDataBoolAnswer;
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
    Teacher teacher = Mockito.mock(Teacher.class);
    SchoolClass schoolClass = Mockito.mock(SchoolClass.class);
    ClassForm classForm = Mockito.mock(ClassForm.class);
    TaskAndAnswerController taskAndAnswerController =
            new TaskAndAnswerController(taskService, answerService, userRatingDTORepository, classService, userService);

    @Test
    void testAddTaskPost() {
        Mockito.when(taskService.checkInputData(any(), any(), any(), any(), any())).thenReturn(new CheckDataBoolAnswer(true, null));
        Mockito.when(userService.getUserByName(any())).thenReturn(teacher);
        Mockito.when(classService.getClassById(any())).thenReturn(schoolClass);
        taskAndAnswerController.addTaskPost(null, null, classForm, null, model);
        assertEquals(model.getAttribute("completed"), "Задача для " + schoolClass.getNameWithNumber() + "класса добавлена");
        Mockito.when(taskService.checkInputData(any(), any(), any(), any(), any())).thenReturn(new CheckDataBoolAnswer(true, null));
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
        Long taskId = 100L;
        Long schoolClassId = 100L;
        Mockito.when(answerService.isStudentInClassSetOfTeacher(any(), any())).thenReturn(true);
        Answer answer = Mockito.mock(Answer.class);
        Mockito.when(answerService.getAnswerById(any())).thenReturn(answer);
        Task task = Mockito.mock(Task.class);
        Mockito.when(answer.getTask()).thenReturn(task);
        Mockito.when(task.getSchoolClass()).thenReturn(schoolClass);
        Mockito.when(schoolClass.getId()).thenReturn(schoolClassId);
        Mockito.when(task.getId()).thenReturn(taskId);
        assertEquals(taskAndAnswerController.checkAnswerPost("33", "3", null),
                "redirect:/answersOfTask/?taskId=" + taskId + "&classId=" + schoolClassId);
        Mockito.when(schoolClass.getId()).thenReturn(schoolClassId);
        Mockito.when(task.getId()).thenReturn(null);
        assertEquals(taskAndAnswerController.checkAnswerPost("33", "3", null),
                "redirect:/");
    }
}