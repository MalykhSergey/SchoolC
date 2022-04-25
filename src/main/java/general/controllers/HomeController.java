package general.controllers;

import general.entities.*;
import general.reposes.AnswerRepos;
import general.reposes.TaskRepos;
import general.reposes.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    private UserRepos userRepos;
    private AnswerRepos answerRepos;
    private TaskRepos taskRepos;

    @Autowired
    public HomeController(UserRepos userRepos, AnswerRepos answerRepos, TaskRepos taskRepos) {
        this.userRepos = userRepos;
        this.answerRepos = answerRepos;
        this.taskRepos = taskRepos;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model) {
        User user = userRepos.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
        if ("ROLE_STUDENT".equals(user.getRole().getName())) {
            Student student = (Student) (user);
            List<Task> newTasks = new ArrayList<>();
            Iterable<Task> tasks = taskRepos.findTasksBySchoolClassOrderByTimeStamp(student.getSchoolClass());
            Iterable<Answer> answers = answerRepos.findAllByStudent(student);
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
        if ("ROLE_TEACHER".equals(user.getRole().getName())) {
            Teacher teacher = (Teacher) (user);
            model.addAttribute("schoolClasses", teacher.getSchoolClassSet());
            return "TeacherHome";
        }
        return "Home";
    }
}