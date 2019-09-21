package general.controllers;

import general.entities.Role;
import general.entities.School;
import general.entities.Student;
import general.entities.Teacher;
import general.reposes.SchoolClassRepos;
import general.reposes.SchoolRepos;
import general.reposes.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.Collection;

@Controller
public class AuthController {
    @Autowired
    UserRepos userRepos;
    @Autowired
    SchoolRepos schoolRepos;
    @Autowired
    SchoolClassRepos schoolClassRepos;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @RequestMapping(value = "/adduser", method = RequestMethod.GET)
    public String addUserGet(){
        return "adduser";
    }
    @RequestMapping(value = "/adduser", method = RequestMethod.POST)
    public String addUserPost(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "typeOfUser") String type,
            @RequestParam(name = "school") String nameOfSchool,
            @RequestParam(name = "password") String password, Model model){
        School school = schoolRepos.findSchoolByName(nameOfSchool);
        if (school == null){
            model.addAttribute("error", "Ошибка: Неверно указана школа!");
            return "adduser";
        }
        if (password == null){
            model.addAttribute("error", "Введите пароль");
            return "adduser";
        }
        if (password.length() < 4){
            model.addAttribute("error", "Введите пароль длинее 5 символов");
            return "adduser";
        }
        if (password.length() > 12){
            model.addAttribute("error", "Ваш пароль слишком длинный!");
            return "adduser";
        }
        if (userRepos.findUserByName(name) != null){
            model.addAttribute("error", "Введите другое имя");
            return "adduser";
        }
        if (name == null){
            model.addAttribute("error", "Введите имя");
            return "adduser";
        }
        if (name.length() > 15){
            model.addAttribute("error", "Введите имя короче 15 символов");
            return "adduser";
        }
        switch (type){
            case "student":
                Collection<Role> roles = Arrays.asList(
                        new Role("ROLE_Student"));
                Student student = new Student(name, passwordEncoder.encode(password), roles, school);
                userRepos.save(student);
                break;
            case "teacher":
                roles = Arrays.asList(
                        new Role("ROLE_Teacher"));
                Teacher teacher = new Teacher(name, passwordEncoder.encode(password), roles, school);
                school.addTeacher(teacher);
                userRepos.save(teacher);
                schoolRepos.save(school);
        }
        model.addAttribute("completed", "Пользователь с именем: "+ name +" был успешно добавлен");
        return "adduser";
    }
}
