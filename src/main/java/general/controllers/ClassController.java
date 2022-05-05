package general.controllers;

import general.controllers.forms.ClassForm;
import general.controllers.forms.UserForm;
import general.entities.*;
import general.services.SchoolClassService;
import general.services.SchoolService;
import general.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final String adcftPage = "/classControllerPages/AddClassForTeacher";

    @Autowired
    public ClassController(SchoolClassService schoolClassService, UserService userService, SchoolService schoolService) {
        this.schoolClassService = schoolClassService;
        this.userService = userService;
        this.schoolService = schoolService;
    }

    @GetMapping(value = "/addclass")
    public String addClassGet() {
        return addClassPage;
    }

    @PostMapping(value = "/addclass")
    public String addClassPost(
            @ModelAttribute("schoolClass") SchoolClass schoolClass,
            @RequestParam(name = "schoolName", required = false) String schoolName,
            @RequestParam(name = "classNumber", required = false) int classNumber,
            Model model) {
        School school = null;
        User user = userService.getUserByName(userService.getCurrentUserName());
        if (user.getRole() == Role.Admin) {
            school = schoolService.getSchoolByName(schoolName);
            if (school == null) {
                model.addAttribute("error", "Вы неверно указали школу");
                return addClassPage;
            }
        }
        if (school == null)
            school = user.getSchool();
        if (!schoolClassService.checkClassName(schoolClass.getName())) {
            model.addAttribute("error", "Введите полное название");
            return addClassPage;
        }
        if (schoolClassService.isClassExistsInSchool(schoolClass.getName(), classNumber, school)) {
            model.addAttribute("error", "Такой класс уже есть");
            return addClassPage;
        }
        schoolClass.setSchool(school);
        schoolClassService.createNewSchoolClass(schoolClass);
        model.addAttribute("completed", schoolClass.getNameWithNumber() + " класс был добавлен");
        return addClassPage;
    }

    @GetMapping(value = "/adcft")
    public String addClassForTeacherGet(Model model) {
        User user = userService.getUserByName(userService.getCurrentUserName());
        if (user.getRole() == Role.Operator)
            model.addAttribute("classes", schoolClassService.getAllClassesBySchool(user.getSchool()));
        return adcftPage;
    }

    @PostMapping(value = "/adcft")
    public String addClassForTeacherPost(
            @ModelAttribute("userForm") UserForm userForm,
            @ModelAttribute("classForm") ClassForm classForm,
            Model model) {
        User operator = userService.getUserByName(userService.getCurrentUserName());
        model.addAttribute("classes", schoolClassService.getAllClassesBySchool(operator.getSchool()));
        User foundedUser = userService.getUserByName(userForm.getUserName());
        if (foundedUser == null || foundedUser.getRole() != Role.Teacher ){
            model.addAttribute("error", "Введите корректные данные");
            return adcftPage;
        }
        SchoolClass schoolClass = schoolClassService.getClassById(classForm.getClassId());
        if (schoolClass==null) {
            model.addAttribute("error", "Введите корректные данные");
            return adcftPage;
        }
        Teacher teacher = (Teacher) foundedUser;
        teacher.addSchoolClass(schoolClass);
        userService.saveUser(teacher);
        model.addAttribute("completed", "Учитель: " + teacher.getName() + " привязан к классу!");
        return adcftPage;
    }
}
