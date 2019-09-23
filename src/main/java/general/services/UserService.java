/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general.services;

import general.entities.Role;
import general.entities.School;
import general.entities.Student;
import general.entities.Teacher;
import general.entities.User;
import general.reposes.SchoolClassRepos;
import general.reposes.SchoolRepos;
import general.reposes.UserRepos;
import java.util.Arrays;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 *
 * @author dmali
 */
@Service
public class UserService{
    @Autowired
    UserRepos userRepos;
    @Autowired
    SchoolRepos schoolRepos;
    @Autowired
    SchoolClassRepos schoolClassRepos;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    public String addUser(String name, String type, String nameOfSchool, String password, Model model){
        School school;
        if (nameOfSchool != null){
            school = schoolRepos.findSchoolByName(nameOfSchool);
        }
        else {
            User user = userRepos.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
            school = user.getSchool();
        }
        
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
                        new Role("ROLE_STUDENT"));
                Student student = new Student(name, passwordEncoder.encode(password), roles, school);
                userRepos.save(student);
                break;
            case "teacher":
                roles = Arrays.asList(
                        new Role("ROLE_TEACHER"));
                Teacher teacher = new Teacher(name, passwordEncoder.encode(password), roles, school);
                school.addTeacher(teacher);
                userRepos.save(teacher);
                schoolRepos.save(school);
            case "operator":
                roles = Arrays.asList(
                        new Role("ROLE_OPERATOR")
                );
        }
        model.addAttribute("completed", "Пользователь с именем: "+ name +" был успешно добавлен");
        return "adduser";
    }
    
    
}
