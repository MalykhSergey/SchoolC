package general.controllers;

import general.utils.CheckDataBoolAnswer;
import general.entities.*;
import general.reposes.*;
import general.services.AnswerService;
import general.services.TaskService;
import general.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class TaskAndAnswerController {
    private TaskService taskService;
    private AnswerService answerService;
    private TaskRepos taskRepos;
    private UserRepos userRepos;
    private UserRatingDTORepository userRatingDTORepository;
    private AnswerRepos answerRepos;
    private SchoolClassRepos schoolClassRepos;
    private UserService userService;

    @Autowired
    public TaskAndAnswerController(TaskService taskService, AnswerService answerService, TaskRepos taskRepos, UserRepos userRepos, UserRatingDTORepository userRatingDTORepository, AnswerRepos answerRepos, SchoolClassRepos schoolClassRepos, UserService userService) {
        this.taskService = taskService;
        this.answerService = answerService;
        this.taskRepos = taskRepos;
        this.userRepos = userRepos;
        this.userRatingDTORepository = userRatingDTORepository;
        this.answerRepos = answerRepos;
        this.schoolClassRepos = schoolClassRepos;
        this.userService = userService;
    }

    @RequestMapping(value = "/addtask", method = RequestMethod.GET)
    public String addTaskGet(Model model) {
        Teacher teacher = (Teacher) (userRepos
                .findUserByName(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.addAttribute("schoolClasses", teacher.getSchoolClassSet());
        return "AddTask";
    }

    @RequestMapping(value = "/addtask", method = RequestMethod.POST)
    public String addTaskPost(@RequestParam(name = "name") String name, @RequestParam(name = "body") String body,
                              @RequestParam(name = "schoolClassName") String nameOfSchoolClass, @RequestParam(name = "date") String date,
                              Model model) throws IOException {
        Teacher teacher = (Teacher) (userRepos.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName()));
        SchoolClass schoolClass = schoolClassRepos.findSchoolClassByName(nameOfSchoolClass);
        CheckDataBoolAnswer checkDataBoolAnswer = taskService.checkInputData(name, body, nameOfSchoolClass, date, teacher, schoolClass);
        model.addAttribute("schoolClasses", teacher.getSchoolClassSet());
        if (checkDataBoolAnswer.isTrue()){
            taskService.createTask(name, body, date, schoolClass);
            model.addAttribute("completed", "Задача для " + schoolClass.getName() + "класса добавлена");
        }
        else {
            model.addAttribute("error",checkDataBoolAnswer.getAnswer());
        }
        return "AddTask";
    }

    @RequestMapping(value = "/addanswer", method = RequestMethod.GET)
    public String addAnswerGet(@RequestParam(name = "id") String id, Model model) {
        Task task = taskRepos.findTaskById(Long.parseLong(id));
        Student student = (Student) (userRepos
                .findUserByName(userService.getUserName()));
        model.addAttribute("task", taskRepos.findTaskById(Long.parseLong(id)));
        return "AddAnswer";
    }

    @RequestMapping(value = "/addanswer", method = RequestMethod.POST)
    public String addAnswerPost(@RequestParam(name = "id") String id, @RequestParam(name = "body") String body,
                                Model model) {
        Task task = taskRepos.findTaskById(Long.parseLong(id));
        Student student = (Student) (userRepos.findUserByName(userService.getUserName()));
        model.addAttribute("task", task);
        if (answerRepos.findByStudentAndTask(student, task) == null) {
            answerService.createAnswer(body, task, student);
            model.addAttribute("completed", "Ответ успешно добавлен");
        }
        return "AddAnswer";
    }

    @RequestMapping(value = "/tasksOfClass", method = RequestMethod.GET)
    public String taskOfClass(@RequestParam(name = "id") String id, Model model) {
        Long schoolClassId = Long.parseLong(id);
        model.addAttribute("students", userRepos.findAllStudentsBySchoolClass(schoolClassId));
        String schoolClassName = schoolClassRepos.findSchoolClassNameById(schoolClassId);
        model.addAttribute("tasks", taskService.getTaskByClass(schoolClassId));
        model.addAttribute("usersRatings", userRatingDTORepository.findAllUsersRatingByClass(schoolClassId));
        model.addAttribute("schoolClassName", schoolClassName);
        model.addAttribute("schoolClassId", schoolClassId);
        return "TasksOfClass";
    }

    @RequestMapping(value = "/answersOfTask", method = RequestMethod.GET)
    public String answerOfTask(@RequestParam(name = "taskId") String taskId,
                               @RequestParam(name = "classId") String classId, Model model) {
        Task task = taskRepos.findTaskById(Long.parseLong(taskId));
        model.addAttribute("answers", task.getAnswers());
        model.addAttribute("task", task);
        model.addAttribute("schoolClass", task.getSchoolClass());
        return "AnswersOfTasks";
    }

    @RequestMapping(value = "/checkAnswer", method = RequestMethod.GET)
    public String checkAnswerGet(@RequestParam(name = "answerId") String answerId, Model model) {
        Teacher teacher = (Teacher) (userRepos
                .findUserByName(SecurityContextHolder.getContext().getAuthentication().getName()));
        Answer answer = answerRepos.findAnswerById(Long.parseLong(answerId));
        Task task = answer.getTask();
        model.addAttribute("taskName", task.getName());
        model.addAttribute("taskBody", task.getName());
        model.addAttribute("answerBody", answer.getBody());
        model.addAttribute("studentName", answer.getStudent().getName());
        return "CheckAnswer";
    }

    @RequestMapping(value = "/checkAnswer", method = RequestMethod.POST)
    public String checkAnswerPost(@RequestParam(name = "answerId") String answerId, @RequestParam(name = "rating") String rating,
                                  @RequestParam(name = "comment") String comment) {
        Long schoolClassId = null;
        Long taskId = null;
        if (Byte.parseByte(rating) < 6 && Byte.parseByte(rating) > 1) {
            Teacher teacher = (Teacher) (userRepos.findUserByName(userService.getUserName()));
            Answer answer = answerRepos.findAnswerById(Long.parseLong(answerId));
            if (answerService.isStudentInClassSetOfTeacher(teacher, answer)) {
                answerService.updateAnswer(rating, comment, answer);
                schoolClassId = answer.getTask().getSchoolClass().getId();
                taskId = answer.getTask().getId();
            }
        }
        return "redirect:/answersOfTask/?taskId=" + taskId + "&classId=" + schoolClassId;
    }
}
