package general.controller;

import general.controller.dto.ClassDTO;
import general.entity.*;
import general.repository.UserRatingDTORepository;
import general.service.AnswerService;
import general.service.SchoolClassService;
import general.service.TaskService;
import general.util.Result;
import general.util.UserDetailsExtended;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TaskAndAnswerController {
    private final TaskService taskService;
    private final AnswerService answerService;
    private final UserRatingDTORepository userRatingDTORepository;
    private final SchoolClassService schoolClassService;
    private final String addTaskPage = "/taskAndAnswerControllerPages/AddTask";
    private final String addAnswerPage = "/taskAndAnswerControllerPages/AddAnswer";
    private final String tasksOfClassPage = "/taskAndAnswerControllerPages/TasksOfClass";
    private final String answersOfTaskPage = "/taskAndAnswerControllerPages/AnswersOfTasks";
    private final String checkAnswerPage = "/taskAndAnswerControllerPages/CheckAnswer";

    @Autowired
    public TaskAndAnswerController(TaskService taskService, AnswerService answerService,
                                   UserRatingDTORepository userRatingDTORepository,
                                   SchoolClassService schoolClassService) {
        this.taskService = taskService;
        this.answerService = answerService;
        this.userRatingDTORepository = userRatingDTORepository;
        this.schoolClassService = schoolClassService;
    }

    @GetMapping(value = "/addTask")
    public String addTaskGet(@AuthenticationPrincipal UserDetailsExtended userDetailsExtended, Model model) {
        Teacher teacher = (Teacher) userDetailsExtended.getUser();
        model.addAttribute("classes", teacher.getSchoolClassSet());
        return addTaskPage;
    }

    @PostMapping(value = "/addTask")
    public String addTaskPost(@ModelAttribute(name = "name") String name,
                              @ModelAttribute(name = "body") String body,
                              @ModelAttribute(name = "classDTO") ClassDTO classDTO,
                              @ModelAttribute(name = "date") String date,
                              @AuthenticationPrincipal UserDetailsExtended userDetailsExtended,
                              Model model) {
        Teacher teacher = (Teacher) userDetailsExtended.getUser();
        model.addAttribute("classes", teacher.getSchoolClassSet());
        SchoolClass schoolClass = schoolClassService.getClassById(classDTO.getClassId());
        Result result = taskService.createTask(name, body, date, schoolClass, teacher);
        if (result == Result.Ok) {
            model.addAttribute("completed", "Задача для " + schoolClass.getNameWithNumber() + " класса добавлена");
        } else {
            model.addAttribute("error", result.getError());
            model.addAttribute("name", name);
            model.addAttribute("body", body);
            model.addAttribute("date", date);
        }
        return addTaskPage;
    }

    @GetMapping(value = "/addAnswer")
    public String addAnswerGet(@RequestParam(name = "id") Long id, Model model) {
        model.addAttribute("task", taskService.getTaskById(id));
        return addAnswerPage;
    }

    @PostMapping(value = "/addAnswer")
    public String addAnswerPost(@RequestParam(name = "id") Long id, @RequestParam(name = "body") String body,
                                @AuthenticationPrincipal UserDetailsExtended userDetailsExtended, Model model) {
        Task task = taskService.getTaskById(id);
        Student student = (Student) userDetailsExtended.getUser();
        model.addAttribute("task", task);
        Result result = answerService.createAnswer(body, task, student);
        if (result == Result.Ok)
            model.addAttribute("completed", "Ответ успешно добавлен");
        else {
            model.addAttribute("error", result.getError());
            model.addAttribute("body", body);
        }
        return addAnswerPage;
    }

    @GetMapping(value = "/tasksOfClass")
    public String taskOfClass(@RequestParam(name = "classId") Long classId,
                              @AuthenticationPrincipal UserDetailsExtended userDetailsExtended,
                              Model model) {
        Teacher teacher = (Teacher) userDetailsExtended.getUser();
        SchoolClass schoolClass = schoolClassService.getClassById(classId);
        model.addAttribute("students", schoolClass.getStudents());
        model.addAttribute("tasks", taskService.getTasksByClassAndTeacher(schoolClass, teacher));
        model.addAttribute("usersRatings", userRatingDTORepository.findAllUsersRatingByTeacherAndClass(teacher, schoolClass));
        model.addAttribute("schoolClassName", schoolClass.getNameWithNumber());
        model.addAttribute("schoolClassId", schoolClass.getId());
        return tasksOfClassPage;
    }

    @GetMapping(value = "/answersOfTask")
    public String answerOfTask(@RequestParam(name = "taskId") Long taskId, Model model) {
        Task task = taskService.getTaskById(taskId);
        model.addAttribute("answers", task.getAnswers());
        model.addAttribute("task", task);
        model.addAttribute("schoolClass", task.getSchoolClass());
        return answersOfTaskPage;
    }

    @GetMapping(value = "/checkAnswer")
    public String checkAnswerGet(@RequestParam(name = "answerId") Long answerId, Model model) {
        Answer answer = answerService.getAnswerById(answerId);
        Task task = answer.getTask();
        model.addAttribute("taskName", task.getName());
        model.addAttribute("taskBody", task.getBody());
        model.addAttribute("answerBody", answer.getBody());
        model.addAttribute("studentName", answer.getStudent().getName());
        return checkAnswerPage;
    }

    @PostMapping(value = "/checkAnswer")
    public String checkAnswerPost(@RequestParam(name = "answerId") Long answerId,
                                  @RequestParam(name = "rating") int rating,
                                  @RequestParam(name = "comment") String comment,
                                  @AuthenticationPrincipal UserDetailsExtended userDetailsExtended,
                                  Model model) {
        Answer answer = answerService.getAnswerById(answerId);
        Result result = answerService.checkAnswer(answer, comment, rating, userDetailsExtended);
        if (result == Result.Ok) {
            model.addAttribute("taskName", answer.getTask().getName());
            model.addAttribute("taskBody", answer.getTask().getBody());
            model.addAttribute("answerBody", answer.getBody());
            model.addAttribute("studentName", answer.getStudent().getName());
            model.addAttribute("completed", "Ответ успешно проверен!");
        }
        else {
            if (answer != null) {
                model.addAttribute("taskName", answer.getTask().getName());
                model.addAttribute("taskBody", answer.getTask().getBody());
                model.addAttribute("answerBody", answer.getBody());
                model.addAttribute("studentName", answer.getStudent().getName());
            }
            model.addAttribute("error", result.getError());
            model.addAttribute("comment", comment);
        }
        return checkAnswerPage;
    }
}
