package general.controller;

import general.controller.dto.StudentsTasksAndAnswers;
import general.entity.Role;
import general.entity.Student;
import general.entity.Teacher;
import general.entity.User;
import general.service.TaskService;
import general.service.UserService;
import general.util.UserDetailsExtended;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final UserService userService;
    private final TaskService taskService;

    @Autowired
    public HomeController(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    @GetMapping(value = "/")
    public String home(@RequestParam(required = false) String teacherName,
                       @AuthenticationPrincipal UserDetailsExtended userDetailsExtended,
                       Model model) {
        User authenticatedUser = userDetailsExtended.getUser();
        if (authenticatedUser.getRole() == Role.Student) {
            Student student = (Student) authenticatedUser;
            StudentsTasksAndAnswers tasksAndAnswersByStudent;
            if (teacherName != null) {
                Teacher teacher = userService.getTeacherByName(teacherName);
                tasksAndAnswersByStudent = taskService.getTasksAndAnswersByStudentAndTeacher(student, teacher);
                model.addAttribute("teacherName", teacherName);
            } else tasksAndAnswersByStudent = taskService.getTasksAndAnswersByStudent(student);
            model.addAttribute("answers", tasksAndAnswersByStudent.getAnswerDTOS());
            model.addAttribute("actualTasks", tasksAndAnswersByStudent.getActualTaskDTOS());
            model.addAttribute("oldTasks", tasksAndAnswersByStudent.getOldTaskDTOS());
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