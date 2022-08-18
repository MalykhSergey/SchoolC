package general.controller;

import general.entity.*;
import general.service.AnswerService;
import general.service.TaskService;
import general.service.UserService;
import general.util.UserDetailsExtended;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String home(@RequestParam(required = false) String teacherName,
                       @AuthenticationPrincipal UserDetailsExtended userDetailsExtended,
                       Model model) {
        User authenticatedUser = userDetailsExtended.getUser();
        if (authenticatedUser.getRole() == Role.Student) {
            Student student = (Student) authenticatedUser;
            List<Task> newTasks = new ArrayList<>();
            List<Task> tasks;
            List<Answer> answers;
            if (teacherName != null) {
                tasks = taskService.getTasksByClassAndTeacher(student.getSchoolClass(), (Teacher) userService.getUserByName(teacherName));
                answers = answerService.getAnswersByStudentAndTeacher(student, (Teacher) userService.getUserByName(teacherName));
                model.addAttribute("teacherName", teacherName);
            } else {
                tasks = taskService.getTasksByClass(student.getSchoolClass());
                answers = answerService.getAnswersByStudent(student);
            }
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
            model.addAttribute("teacherNames", userService.getNamesOfTeachersByClassId(student.getSchoolClass().getId()));
            return "StudentHome";
        }
        if (authenticatedUser.getRole() == Role.Teacher) {
            Teacher teacher = (Teacher) (authenticatedUser);
            model.addAttribute("schoolClasses", teacher.getSchoolClassSet());
            return "TeacherHome";
        }
        return "Home";
    }
}