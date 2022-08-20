package general.controller.api;

import general.controller.dto.StudentsTasksAndAnswers;
import general.entity.Student;
import general.entity.Task;
import general.entity.Teacher;
import general.service.AnswerService;
import general.service.TaskService;
import general.service.UserService;
import general.util.Result;
import general.util.UserDetailsExtended;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiTaskAndAnswerController {
    private final TaskService taskService;
    private final AnswerService answerService;
    private final UserService userService;

    @Autowired
    public ApiTaskAndAnswerController(TaskService taskService, AnswerService answerService, UserService userService) {
        this.taskService = taskService;
        this.answerService = answerService;
        this.userService = userService;
    }

    @GetMapping("/studentsTasksAndAnswers")
    public StudentsTasksAndAnswers getStudentsTasksAndAnswers(@RequestParam(name = "teacherName", required = false) String teacherName, @AuthenticationPrincipal UserDetailsExtended userDetailsExtended) {
        if (teacherName == null)
            return taskService.getTasksAndAnswersByStudent((Student) userDetailsExtended.getUser());
        else {
            Teacher teacher = userService.getTeacherByName(teacherName);
            return taskService.getTasksAndAnswersByStudentAndTeacher((Student) userDetailsExtended.getUser(), teacher);
        }
    }

    @PostMapping("/addAnswer/")
    public Result addAnswer(@AuthenticationPrincipal UserDetailsExtended userDetailsExtended, @RequestParam("taskId") Long taskId, @RequestBody String body) {
        Task task = taskService.getTaskById(taskId);
        return answerService.createAnswer(body, task, (Student) userDetailsExtended.getUser());
    }

}
