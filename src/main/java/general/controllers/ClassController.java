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
            model.addAttribute("error", "Введите название класса короче 20 символов");
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

    @GetMapping(value = "/addClassForTeacher")
    public String addClassForTeacherGet(Model model) {
        User user = userService.getUserByName(userService.getCurrentUserName());
        if (user.getRole() == Role.Operator)
            model.addAttribute("classes", schoolClassService.getAllClassesBySchool(user.getSchool()));
        return addClassForTeacherPage;
    }

    @PostMapping(value = "/addClassForTeacher")
    public String addClassForTeacherPost(
            @ModelAttribute("userForm") UserForm userForm,
            @ModelAttribute("classForm") ClassForm classForm,
            Model model) {
        User user = userService.getUserByName(userService.getCurrentUserName());
        if (user.getRole() == Role.Operator)
            model.addAttribute("classes", schoolClassService.getAllClassesBySchool(user.getSchool()));
        User foundedUser = userService.getUserByName(userForm.getUserName());
        if (foundedUser == null || foundedUser.getRole() != Role.Teacher) {
            model.addAttribute("error", "Введите корректные данные (пользователь не найден)");
            return addClassForTeacherPage;
        }
        SchoolClass schoolClass;
        if (user.getRole() == Role.Operator)
            schoolClass = schoolClassService.getClassById(classForm.getClassId());
        else
            schoolClass = schoolClassService.getClassByNameAndNumberAndSchool(classForm.getClassName(), classForm.getClassNumber(), foundedUser.getSchool());
        if (schoolClass == null) {
            model.addAttribute("error", "Введите корректные данные (класс не найден)");
            return addClassForTeacherPage;
        }
        Teacher teacher = (Teacher) foundedUser;
        teacher.addSchoolClass(schoolClass);
        userService.saveUser(teacher);
        model.addAttribute("completed", "Учитель: " + teacher.getName() + " привязан к классу!");
        return addClassForTeacherPage;
    }
}
