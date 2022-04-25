package general.controllers;

import general.entities.*;
import general.reposes.SchoolClassRepos;
import general.reposes.SchoolRepos;
import general.reposes.UserRepos;
import general.services.UserService;
import general.utils.CheckDataBoolAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    private final UserService userService;
    private final UserRepos userRepos;
    private final PasswordEncoder passwordEncoder;
    private final SchoolRepos schoolRepos;
    private final SchoolClassRepos schoolClassRepos;

    @Autowired
    public UserController(UserService userService, UserRepos userRepos, PasswordEncoder passwordEncoder, SchoolRepos schoolRepos, SchoolClassRepos schoolClassRepos) {
        this.userService = userService;
        this.userRepos = userRepos;
        this.passwordEncoder = passwordEncoder;
        this.schoolRepos = schoolRepos;
        this.schoolClassRepos = schoolClassRepos;
    }

    @RequestMapping(value = "/adduser", method = RequestMethod.GET)
    public String addUserGet() {
        return "adduser";
    }

    @RequestMapping(value = "/adduser", method = RequestMethod.POST)
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
                school = schoolRepos.findSchoolByName(nameOfSchool);
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
                    SchoolClass schoolClass = schoolClassRepos.findSchoolClassByName(nameOfSchoolClass);
                    if (nameOfSchoolClass == null | schoolClass == null) {
                        model.addAttribute("error", "Неверно указан класс");
                        return "adduser";
                    }
                    Student student = new Student(name, passwordEncoder.encode(password), Role.Student, school, schoolClass);
                    schoolClass.addStudent(student);
                    schoolClassRepos.save(schoolClass);
                    break;
                case "teacher":
                    Teacher teacher = new Teacher(name, passwordEncoder.encode(password), school, Role.Teacher);
                    school.addTeacher(teacher);
                    schoolRepos.save(school);
                    break;
                case "operator":
                    User operator = new User(name, passwordEncoder.encode(password), school, Role.Operator);
                    school.addOperator(operator);
                    schoolRepos.save(school);
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

    @RequestMapping(value = "/scfs", method = RequestMethod.GET)
    public String setClassForStudentGet() {
        return "setClassForStudent";
    }

    @RequestMapping(value = "/scfs", method = RequestMethod.POST)
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
        SchoolClass schoolClass = schoolClassRepos.findSchoolClassByName(className);
        if ((student != null || schoolClass != null) && student.getSchool().getId() == schoolClass.getSchool().getId()) {
            userService.updateClassForStudent(schoolClass,student);
        } else {
            model.addAttribute("error", "Введите корректные данные");
            return "SetClassForStudent";
        }
        model.addAttribute("completed", "Ученик привязан к классу");
        return "SetClassForStudent";
    }
}
