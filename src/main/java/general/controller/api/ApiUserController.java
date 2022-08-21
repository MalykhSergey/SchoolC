package general.controller.api;

import general.controller.dto.ClassDTO;
import general.entity.Role;
import general.entity.Student;
import general.entity.Teacher;
import general.service.UserService;
import general.util.UserDetailsExtended;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiUserController {
    private final UserService userService;

    @Autowired
    public ApiUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public Role login(@AuthenticationPrincipal UserDetailsExtended userDetailsExtended) {
        return userDetailsExtended.getUser().getRole();
    }

    @GetMapping("/student/teacherNames")
    public List<String> getTeacherNames(@AuthenticationPrincipal UserDetailsExtended userDetailsExtended) {
        Student student = (Student) userDetailsExtended.getUser();
        return userService.getNamesOfTeachersByClassId(student.getSchoolClass().getId());
    }

    @GetMapping("/teacher/classes")
    public List<ClassDTO> getTeacherClasses(@AuthenticationPrincipal UserDetailsExtended userDetailsExtended) {
        Teacher teacher = (Teacher) userDetailsExtended.getUser();
        return teacher.getSchoolClassSet().stream()
                .map(schoolClass -> new ClassDTO(schoolClass.getName(), schoolClass.getClassNumber(), schoolClass.getId())).toList();
    }
}
