package general.controllers;

import general.entities.*;
import general.services.SchoolClassService;
import general.services.SchoolService;
import general.services.TeacherService;
import general.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ClassController {
    private final TeacherService teacherService;
    private final SchoolClassService schoolClassService;
    private final UserService userService;
    private final SchoolService schoolService;

    @Autowired
    public ClassController(TeacherService teacherService, SchoolClassService schoolClassService, UserService userService, SchoolService schoolService) {
        this.teacherService = teacherService;
        this.schoolClassService = schoolClassService;
        this.userService = userService;
        this.schoolService = schoolService;
    }

    @GetMapping(value = "/addclass")
    public String addClassGet() {
        return "AddClass";
    }

    @PostMapping(value = "/addclass")
    public String addClassPost(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "school", required = false) String schoolName, Model model) {
        School school = null;
        User user = userService.getUserByName(userService.getUserName());
        if (user.getRole() == Role.Admin) {
            school = schoolService.getSchoolByName(schoolName);
            if (school == null) {
                model.addAttribute("error", "Вы неверно указали школу");
                return "AddClass";
            }
        }
        if (school == null)
            school = user.getSchool();
        if (schoolClassService.checkClassName(name)) {
            model.addAttribute("error", "Введите полное название");
            return "AddClass";
        }
        if (schoolClassService.checkClass(name)) {
            model.addAttribute("error", "Такой класс уже есть");
            return "AddClass";
        }
        schoolClassService.createNewSchoolClass(name, school);
        model.addAttribute("completed", "Класс " + name + " был добавлен");
        return "AddClass";
    }

    @GetMapping(value = "/adcft")
    public String addClassForTeacherGet() {
        return "AddClassForTeacher";
    }

    @PostMapping(value = "/adcft")
    public String addClassForTeacherPost(
            @RequestParam(name = "className") String className,
            @RequestParam(name = "teacherName") String teacherName, Model model) {
        if (teacherService.checkInputDataForAddingClassToTeacher(teacherName, className, model))
            return "AddClassForTeacher";
        Teacher teacher = (Teacher) userService.getUserByName(teacherName);
        SchoolClass schoolClass = schoolClassService.getClassByName(className);
        teacher.addSchoolClass(schoolClass);
        userService.saveUser(teacher);
        model.addAttribute("completed", "Учитель: " + teacherName + " привязан к классу!");
        return "AddClassForTeacher";
    }
}
