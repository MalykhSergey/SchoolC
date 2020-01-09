/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general.services;

import general.entities.*;
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
    
    public String addUser(String name, String type, String nameOfSchool,String nameOfSchoolClass,  String password, Model model){
        School school = null;
        User user = userRepos.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
        if (nameOfSchool != null){
            for (Role role:user.getRoles()){
                if ("ROLE_ADMIN".equals(role.getName())){
                    school = schoolRepos.findSchoolByName(nameOfSchool);
                    if (school == null){
                        model.addAttribute("error", "Неверно указана школа");
                        return "adduser";
                    }
                    break;
                }
            }
            school = schoolRepos.findSchoolByName(nameOfSchool);
        }
        if (type.equals("student")){

        }
        if (school == null) {
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
        if (name == null){
            model.addAttribute("error", "Введите имя");
            return "adduser";
        }
        if (name.length() > 15){
            model.addAttribute("error", "Введите имя короче 15 символов");
            return "adduser";
        }
        System.out.println(userRepos.findUserByName(name));
        if (userRepos.findUserByName(name) != null) {
            model.addAttribute("error", "Введите другое имя");
            return "adduser";
        }
            switch (type){
                case "student":
                    SchoolClass schoolClass = schoolClassRepos.findSchoolClassByName(nameOfSchoolClass);
                    if(nameOfSchoolClass == null | schoolClass == null){
                        model.addAttribute("error", "Неверно указан класс");
                        return "adduser";
                    }
                    Collection<Role> roles = Arrays.asList(
                            new Role("ROLE_STUDENT"));
                    Student student = new Student(name, passwordEncoder.encode(password), roles, school, schoolClass);
                    schoolClass.addStudent(student);
                    schoolClassRepos.save(schoolClass);
                    break;
                case "teacher":
                    roles = Arrays.asList(
                            new Role("ROLE_TEACHER"));
                    Teacher teacher = new Teacher(name, passwordEncoder.encode(password), roles, school);
                    school.addTeacher(teacher);
                    schoolRepos.save(school);

                    break;
                case "operator":
                    roles = Arrays.asList(
                            new Role("ROLE_OPERATOR")
                    );
                    Operator operator = new Operator(name, passwordEncoder.encode(password), roles, school);
                    school.addOperator(operator);
                    schoolRepos.save(school);
                    break;
                default:{
                    model.addAttribute("error", "Не жульничай!");
                    return "adduser";
                }
            }


        model.addAttribute("completed", "Пользователь с именем: "+ name +" был успешно добавлен");
        return "adduser";
    }
    public String setClassForStudent(String name, String className, Model model){
        if (name == null){
            model.addAttribute("error", "Введите имя ученика");
            return "setClassForStudent";
        }
        if (className == null){
            model.addAttribute("error", "Введите класс");
            return "setClassForStudent";
        }
        Student student = (Student) (userRepos.findUserByName(name));
        SchoolClass schoolClass = schoolClassRepos.findSchoolClassByName(className);
        if (student != null | schoolClass != null){
            student.setSchoolClass(schoolClass);
            userRepos.save(student);
            schoolClass.addStudent(student);
            schoolClassRepos.save(schoolClass);
        }
        else{
            model.addAttribute("error", "Введите корректные данные");
            return "setClassForStudent";
        }
        model.addAttribute("completed", "Ученик привязан к классу");
        return "setClassForStudent";
    }
    
}
