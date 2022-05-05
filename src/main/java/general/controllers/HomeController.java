package general.controllers;

import general.entities.*;
import general.services.AnswerService;
import general.services.TaskService;
import general.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    private final UserService userService;
    private final AnswerService answerService;
    private final TaskService taskService;

    @Autowired
    public HomeController(UserService userService, AnswerService answerService, TaskService taskService) {
        this.userService = userService;
        this.answerService = answerService;
        this.taskService = taskService;
    }

    @GetMapping(value = "/")
    public String home(Model model) {
        User user = userService.getUserByName(userService.getCurrentUserName());
        if (user.getRole() == Role.Student) {
            Student student = (Student) (user);
            List<Task> newTasks = new ArrayList<>();
            List<Task> tasks = taskService.getTasksByClass(student.getSchoolClass());
            List<Answer> answers = answerService.getAnswersByStudent(student);
            Timestamp now = new Timestamp(System.currentTimeMillis());
            for (Task task : tasks) {
                if (now.before(task.getTimeStamp())) {
                    newTasks.add(task);
                }
            }
            for (Answer answer : answers) {
                newTasks.remove(answer.getTask());
            }
            model.addAttribute("answers", answers);
            model.addAttribute("newtasks", newTasks);
            return "StudentHome";
        }
        if (user.getRole() == Role.Teacher) {
            Teacher teacher = (Teacher) (user);
            model.addAttribute("schoolClasses", teacher.getSchoolClassSet());
            return "TeacherHome";
        }
        return "Home";
    }
}