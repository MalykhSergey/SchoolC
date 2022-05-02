package general.controllers;

import general.entities.*;
import general.reposes.UserRatingDTORepository;
import general.services.AnswerService;
import general.services.SchoolClassService;
import general.services.TaskService;
import general.services.UserService;
import general.utils.CheckDataBoolAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TaskAndAnswerController {
    private final TaskService taskService;
    private final AnswerService answerService;
    private final UserRatingDTORepository userRatingDTORepository;
    private final SchoolClassService schoolClassService;
    private final UserService userService;

    @Autowired
    public TaskAndAnswerController(TaskService taskService, AnswerService answerService,
                                   UserRatingDTORepository userRatingDTORepository,
                                   SchoolClassService schoolClassService,
                                   UserService userService) {
        this.taskService = taskService;
        this.answerService = answerService;
        this.userRatingDTORepository = userRatingDTORepository;
        this.schoolClassService = schoolClassService;
        this.userService = userService;
    }

    @GetMapping(value = "/addtask")
    public String addTaskGet(Model model) {
        Teacher teacher = (Teacher) userService.getUserByName(userService.getUserName());
        model.addAttribute("schoolClasses", teacher.getSchoolClassSet());
        return "AddTask";
    }

    @PostMapping(value = "/addtask")
    public String addTaskPost(@RequestParam(name = "name") String name, @RequestParam(name = "body") String body,
                              @RequestParam(name = "schoolClassName") String nameOfSchoolClass, @RequestParam(name = "date") String date,
                              Model model) {
        Teacher teacher = (Teacher) userService.getUserByName(userService.getUserName());
        SchoolClass schoolClass = schoolClassService.getClassByName(nameOfSchoolClass);
        CheckDataBoolAnswer checkDataBoolAnswer = taskService.checkInputData(name, body, nameOfSchoolClass, date, teacher, schoolClass);
        model.addAttribute("schoolClasses", teacher.getSchoolClassSet());
        if (checkDataBoolAnswer.isTrue()) {
            taskService.createTask(name, body, date, schoolClass, teacher);
            model.addAttribute("completed", "Задача для " + schoolClass.getName() + "класса добавлена");
        } else {
            model.addAttribute("error", checkDataBoolAnswer.getAnswer());
        }
        return "AddTask";
    }

    @GetMapping(value = "/addanswer")
    public String addAnswerGet(@RequestParam(name = "id") String id, Model model) {
        model.addAttribute("task", taskService.getTaskById(Long.parseLong(id)));
        return "AddAnswer";
    }

    @PostMapping(value = "/addanswer")
    public String addAnswerPost(@RequestParam(name = "id") String id, @RequestParam(name = "body") String body,
                                Model model) {
        Task task = taskService.getTaskById(Long.parseLong(id));
        Student student = (Student) userService.getUserByName(userService.getUserName());
        model.addAttribute("task", task);
        if (answerService.getByStudentAndTask(student, task) == null) {
            answerService.createAnswer(body, task, student);
            model.addAttribute("completed", "Ответ успешно добавлен");
        }
        return "AddAnswer";
    }

    @GetMapping(value = "/tasksOfClass")
    public String taskOfClass(@RequestParam(name = "id") String id, Model model) {
        Long schoolClassId = Long.parseLong(id);
        Teacher teacher = (Teacher) userService.getUserByName(userService.getUserName());
        SchoolClass schoolClass = schoolClassService.getClassById(schoolClassId);
        model.addAttribute("students", schoolClass.getStudents());
        model.addAttribute("tasks", taskService.getTaskByTeacherAndClass(teacher, schoolClass));
        model.addAttribute("usersRatings", userRatingDTORepository.findAllUsersRatingByTeacherAndClass(teacher, schoolClass));
        model.addAttribute("schoolClassName", schoolClass.getName());
        model.addAttribute("schoolClassId", schoolClass.getId());
        return "TasksOfClass";
    }

    @GetMapping(value = "/answersOfTask")
    public String answerOfTask(@RequestParam(name = "taskId") String taskId,
                               @RequestParam(name = "classId") String classId, Model model) {
        Task task = taskService.getTaskById(Long.parseLong(taskId));
        model.addAttribute("answers", task.getAnswers());
        model.addAttribute("task", task);
        model.addAttribute("schoolClass", task.getSchoolClass());
        return "AnswersOfTasks";
    }

    @GetMapping(value = "/checkAnswer")
    public String checkAnswerGet(@RequestParam(name = "answerId") String answerId, Model model) {
        Answer answer = answerService.getAnswerById(Long.parseLong(answerId));
        Task task = answer.getTask();
        model.addAttribute("taskName", task.getName());
        model.addAttribute("taskBody", task.getName());
        model.addAttribute("answerBody", answer.getBody());
        model.addAttribute("studentName", answer.getStudent().getName());
        return "CheckAnswer";
    }

    @PostMapping(value = "/checkAnswer")
    public String checkAnswerPost(@RequestParam(name = "answerId") String answerId, @RequestParam(name = "rating") String rating,
                                  @RequestParam(name = "comment") String comment) {
        Long schoolClassId = null;
        Long taskId = null;
        if (Byte.parseByte(rating) < 6 && Byte.parseByte(rating) > 1) {
            Teacher teacher = (Teacher) userService.getUserByName(userService.getUserName());
            Answer answer = answerService.getAnswerById(Long.parseLong(answerId));
            if (answerService.isStudentInClassSetOfTeacher(teacher, answer)) {
                answerService.updateAnswer(rating, comment, answer);
                schoolClassId = answer.getTask().getSchoolClass().getId();
                taskId = answer.getTask().getId();
            }
        }
        if (taskId == null || schoolClassId == null) return "redirect:/";
        return "redirect:/answersOfTask/?taskId=" + taskId + "&classId=" + schoolClassId;
    }
}
