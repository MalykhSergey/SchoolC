package general.controller;

import general.controller.dto.ClassDTO;
import general.controller.dto.UserDTO;
import general.entity.Role;
import general.entity.School;
import general.entity.SchoolClass;
import general.entity.Teacher;
import general.service.SchoolClassService;
import general.service.SchoolService;
import general.service.UserService;
import general.util.Result;
import general.util.UserDetailsExtended;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ClassController {
    private final SchoolClassService schoolClassService;
    private final UserService userService;
    private final SchoolService schoolService;
    private final String addClassPage = "/classControllerPages/AddClass";
    private final String addClassForTeacherPage = "/classControllerPages/AddClassForTeacher";

    @Autowired
    public ClassController(SchoolClassService schoolClassService, UserService userService, SchoolService schoolService) {
        this.schoolClassService = schoolClassService;
        this.userService = userService;
        this.schoolService = schoolService;
    }

    @GetMapping(value = "/addClass")
    public String addClassGet() {
        return addClassPage;
    }

    @PostMapping(value = "/addClass")
    public String addClassPost(
            @RequestParam(name = "className") String className,
            @RequestParam(name = "classNumber") int classNumber,
            @RequestParam(name = "schoolName", required = false) String schoolName,
            @AuthenticationPrincipal UserDetailsExtended userDetailsExtended,
            Model model) {
        School school;
        if (userDetailsExtended.getUser().getRole() == Role.Operator)
            school = userDetailsExtended.getUser().getSchool();
        else school = schoolService.getSchoolByName(schoolName);
        Result result = schoolClassService.createSchoolClass(className, classNumber, school);
        if (result == Result.Ok) {
            model.addAttribute("completed", classNumber + "-" + className + " класс был добавлен");
        } else model.addAttribute("error", result.getError());
        return addClassPage;
    }

    @GetMapping(value = "/addClassForTeacher")
    public String addClassForTeacherGet(@AuthenticationPrincipal UserDetailsExtended userDetailsExtended, Model model) {
        if (userDetailsExtended.getUser().getRole() == Role.Operator)
            model.addAttribute("classes", schoolClassService.getAllClassesBySchool(userDetailsExtended.getUser().getSchool()));
        return addClassForTeacherPage;
    }

    @PostMapping(value = "/addClassForTeacher")
    public String addClassForTeacherPost(
            @ModelAttribute("userDTO") UserDTO userDTO,
            @ModelAttribute("classDTO") ClassDTO classDTO,
            @AuthenticationPrincipal UserDetailsExtended userDetailsExtended,
            Model model) {
        Teacher teacher = userService.getTeacherByName(userDTO.getUserName());
        SchoolClass schoolClass;
        if (userDetailsExtended.getUser().getRole() == Role.Operator) {
            model.addAttribute("classes", schoolClassService.getAllClassesBySchool(userDetailsExtended.getUser().getSchool()));
            schoolClass = schoolClassService.getClassById(classDTO.getClassId());
        } else
            schoolClass = schoolClassService.getClassByNameAndNumberAndSchool(classDTO.getClassName(), classDTO.getClassNumber(), teacher.getSchool());
        Result result = schoolClassService.addClassForTeacher(teacher, schoolClass, userDetailsExtended);
        if (result == Result.Ok)
            model.addAttribute("completed", "Учитель: " + teacher.getName() + " привязан к классу!");
        else model.addAttribute("error", result.getError());
        return addClassForTeacherPage;
    }
}
