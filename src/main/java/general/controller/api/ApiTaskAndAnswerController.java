package general.controller.api;

import general.controller.dto.StudentsTasksAndAnswers;
import general.controller.dto.TaskDTO;
import general.entity.Student;
import general.entity.Task;
import general.entity.Teacher;
import general.service.AnswerService;
import general.service.SchoolClassService;
import general.service.TaskService;
import general.service.UserService;
import general.util.Result;
import general.util.UserDetailsExtended;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiTaskAndAnswerController {
    private final TaskService taskService;
    private final AnswerService answerService;
    private final UserService userService;

    private final SchoolClassService schoolClassService;

    @Autowired
    public ApiTaskAndAnswerController(TaskService taskService, AnswerService answerService, UserService userService, SchoolClassService schoolClassService) {
        this.taskService = taskService;
        this.answerService = answerService;
        this.userService = userService;
        this.schoolClassService = schoolClassService;
    }

    @GetMapping("/student/tasksAndAnswers")
    public StudentsTasksAndAnswers getStudentsTasksAndAnswers(@RequestParam(name = "teacherName", required = false) String teacherName, @AuthenticationPrincipal UserDetailsExtended userDetailsExtended) {
        if (teacherName == null)
            return taskService.getTasksAndAnswersByStudent((Student) userDetailsExtended.getUser());
        else {
            Teacher teacher = userService.getTeacherByName(teacherName);
            return taskService.getTasksAndAnswersByStudentAndTeacher((Student) userDetailsExtended.getUser(), teacher);
        }
    }

    @PostMapping("/student/addAnswer")
    public Result addAnswer(@AuthenticationPrincipal UserDetailsExtended userDetailsExtended, @RequestParam("taskId") Long taskId, @RequestBody String body) {
        Task task = taskService.getTaskById(taskId);
        return answerService.createAnswer(body, task, (Student) userDetailsExtended.getUser());
    }

    @GetMapping("/teacher/tasks")
    public List<TaskDTO> getTasks(@AuthenticationPrincipal UserDetailsExtended userDetailsExtended, @RequestParam("classId") Long classId) {
        Teacher teacher = (Teacher) userDetailsExtended.getUser();
        return taskService.getTasksByClassAndTeacher(schoolClassService.getClassById(classId), teacher).stream().map(TaskDTO::new).toList();
    }

}
