package general.controllers;

import general.entities.*;
import general.reposes.UserRepos;
import general.services.SchoolClassService;
import general.services.SchoolService;
import general.services.UserService;
import general.utils.CheckDataBoolAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    private final UserService userService;
    private final UserRepos userRepos;
    private final PasswordEncoder passwordEncoder;
    private final SchoolService schoolService;
    private final SchoolClassService schoolClassService;

    @Autowired
    public UserController(UserService userService, UserRepos userRepos, PasswordEncoder passwordEncoder,
                          SchoolService schoolService, SchoolClassService schoolClassService) {
        this.userService = userService;
        this.userRepos = userRepos;
        this.passwordEncoder = passwordEncoder;
        this.schoolService = schoolService;
        this.schoolClassService = schoolClassService;
    }

    @GetMapping(value = "/adduser")
    public String addUserGet() {
        return "AddUser";
    }

    @PostMapping(value = "/adduser")
    public String addUserPost(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "typeOfUser") String type,
            @RequestParam(name = "school", required = false) String nameOfSchool,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "class", required = false) String nameOfSchoolClass, Model model) {
        School school = null;
        User user = userRepos.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
        if (nameOfSchool != null) {
            if ("ROLE_ADMIN".equals(user.getRole().getName())) {
                school = schoolService.getSchoolByName(nameOfSchool);
                if (school == null) {
                    model.addAttribute("error", "Неверно указана школа");
                    return "AddUser";
                }
            }
        }
        CheckDataBoolAnswer checkDataBoolAnswer = userService.checkInputData(name, password);
        if (checkDataBoolAnswer.isTrue()) {
            if (school == null) {
                school = user.getSchool();
            }
            switch (type) {
                case "student":
                    SchoolClass schoolClass = schoolClassService.getClassByName(nameOfSchoolClass);
                    if (nameOfSchoolClass == null | schoolClass == null) {
                        model.addAttribute("error", "Неверно указан класс");
                        return "AddUser";
                    }
                    Student student = new Student(name, passwordEncoder.encode(password), Role.Student, school, schoolClass);
                    userService.saveUser(student);
                    break;
                case "teacher":
                    Teacher teacher = new Teacher(name, passwordEncoder.encode(password), school, Role.Teacher);
                    userService.saveUser(teacher);
                    break;
                case "operator":
                    User operator = new User(name, passwordEncoder.encode(password), school, Role.Operator);
                    userService.saveUser(operator);
                    break;
                default: {
                    model.addAttribute("error", "Не жульничай!");
                }
            }
            model.addAttribute("completed", "Пользователь с именем: " + name + " был успешно добавлен");
        } else
            model.addAttribute("error", checkDataBoolAnswer.getAnswer());
        return "AddUser";
    }

    @GetMapping(value = "/scfs")
    public String setClassForStudentGet() {
        return "SetClassForStudent";
    }

    @PostMapping(value = "/scfs")
    public String setClassForStudentPost(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "class") String className,
            Model model
    ) {
        if (name == null) {
            model.addAttribute("error", "Введите имя ученика");
            return "SetClassForStudent";
        }
        if (className == null) {
            model.addAttribute("error", "Введите класс");
            return "SetClassForStudent";
        }
        Student student = (Student) (userRepos.findUserByName(name));
        SchoolClass schoolClass = schoolClassService.getClassByName(className);
        if ((student != null || schoolClass != null) && student.getSchool().getId() == schoolClass.getSchool().getId()) {
            student.setSchoolClass(schoolClass);
            userRepos.save(student);
        } else {
            model.addAttribute("error", "Введите корректные данные");
            return "SetClassForStudent";
        }
        model.addAttribute("completed", "Ученик привязан к классу");
        return "SetClassForStudent";
    }
}
