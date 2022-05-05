package general.controllers;

import general.controllers.forms.ClassForm;
import general.controllers.forms.SchoolForm;
import general.controllers.forms.UserForm;
import general.entities.*;
import general.services.SchoolClassService;
import general.services.SchoolService;
import general.services.UserService;
import general.utils.CheckDataBoolAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final SchoolService schoolService;
    private final SchoolClassService schoolClassService;
    private final String addUserPage = "/userControllerPages/AddUser";
    private final String setClassForStudentPage = "/userControllerPages/SetClassForStudent";

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder,
                          SchoolService schoolService, SchoolClassService schoolClassService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.schoolService = schoolService;
        this.schoolClassService = schoolClassService;
    }

    @GetMapping(value = "/adduser")
    public String addUserGet(Model model) {
        User user = userService.getUserByName(userService.getCurrentUserName());
        if (user.getRole() == Role.Operator)
            model.addAttribute("classes", schoolClassService.getAllClassesBySchool(user.getSchool()));
        return addUserPage;
    }

    @PostMapping(value = "/adduser")
    public String addUserPost(
            @ModelAttribute("userForm") UserForm userForm,
            @ModelAttribute("schoolForm") SchoolForm schoolForm,
            @ModelAttribute("classForm") ClassForm classForm,
            Model model) {
        School school = null;
        User user = userService.getUserByName(userService.getCurrentUserName());
        if (user.getRole() == Role.Operator)
            model.addAttribute("classes", schoolClassService.getAllClassesBySchool(user.getSchool()));
        if (schoolForm.getSchoolName() != null) {
            if (user.getRole() == Role.Admin) {
                school = schoolService.getSchoolByName(schoolForm.getSchoolName());
                if (school == null) {
                    model.addAttribute("error", "Неверно указана школа");
                    return addUserPage;
                }
            }
        }
        CheckDataBoolAnswer checkDataBoolAnswer = userService.checkUserFormForCreate(userForm);
        if (checkDataBoolAnswer.isTrue()) {
            if (school == null) {
                school = user.getSchool();
            }
            switch (userForm.getRole()) {
                case Student:
                    SchoolClass schoolClass;
                    if (classForm.getClassId() != null) {
                        schoolClass = schoolClassService.getClassById(classForm.getClassId());
                    } else
                        schoolClass = schoolClassService.getClassByNameAndNumberAndSchool(classForm.getClassName(), classForm.getClassNumber(), school);
                    if (schoolClass == null) {
                        model.addAttribute("error", "Неверно указан класс");
                        return addUserPage;
                    }
                    Student student = new Student(userForm.getUserName(),
                            passwordEncoder.encode(userForm.getPassword()), Role.Student, school, schoolClass);
                    userService.saveUser(student);
                    break;
                case Teacher:
                    Teacher teacher = new Teacher(userForm.getUserName(),
                            passwordEncoder.encode(userForm.getPassword()), school, Role.Teacher);
                    userService.saveUser(teacher);
                    break;
                case Operator:
                    User operator = new User(userForm.getUserName(),
                            passwordEncoder.encode(userForm.getPassword()), school, Role.Operator);
                    userService.saveUser(operator);
                    break;
                default: {
                    model.addAttribute("error", "Не жульничай!");
                }
            }
            model.addAttribute("completed", "Пользователь с именем: " + userForm.getUserName() + " был успешно добавлен");
        } else
            model.addAttribute("error", checkDataBoolAnswer.getAnswer());
        return addUserPage;
    }

    @GetMapping(value = "/scfs")
    public String setClassForStudentGet(Model model) {
        User user = userService.getUserByName(userService.getCurrentUserName());
        if (user.getRole() == Role.Operator)
            model.addAttribute("classes", schoolClassService.getAllClassesBySchool(user.getSchool()));
        return setClassForStudentPage;
    }

    @PostMapping(value = "/scfs")
    public String setClassForStudentPost(
            @ModelAttribute("userForm") UserForm userForm,
            @ModelAttribute("classForm") ClassForm classForm,
            Model model
    ) {
        User user = userService.getUserByName(userService.getCurrentUserName());
        if (user.getRole() == Role.Operator)
            model.addAttribute("classes", schoolClassService.getAllClassesBySchool(user.getSchool()));
        if (userForm.getUserName() == null) {
            model.addAttribute("error", "Введите имя ученика");
            return setClassForStudentPage;
        }
        User foundedUser = userService.getUserByName(userForm.getUserName());
        if (foundedUser == null || foundedUser.getRole() != Role.Student) {
            model.addAttribute("error", "Введите корректные данные");
            return setClassForStudentPage;
        }
        Student student = (Student) foundedUser;
        SchoolClass schoolClass;
        if (classForm.getClassId() != null)
            schoolClass = schoolClassService.getClassById(classForm.getClassId());
        else
            schoolClass = schoolClassService.getClassByNameAndNumberAndSchool(classForm.getClassName(), classForm.getClassNumber(), student.getSchool());
        if (schoolClass != null && student.getSchool().getId() == schoolClass.getSchool().getId()) {
            student.setSchoolClass(schoolClass);
            userService.saveUser(student);
        } else {
            model.addAttribute("error", "Введите корректные данные");
            return setClassForStudentPage;
        }
        model.addAttribute("completed", "Ученик привязан к классу");
        return setClassForStudentPage;
    }
}
