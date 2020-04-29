/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package general.services;

import general.entities.Role;
import general.entities.School;
import general.entities.SchoolClass;
import general.entities.User;
import general.reposes.SchoolClassRepos;
import general.reposes.SchoolRepos;
import general.reposes.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 *
 * @author dmali
 */
@Service
public class SchoolClassService {

    @Autowired
    UserRepos userRepos;
    @Autowired
    SchoolRepos schoolRepos;
    @Autowired
    SchoolClassRepos schoolClassRepos;

    public String addClass(String name, String schoolName, Model model) {
        School school = null;
        User user = userRepos.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
        if (schoolName != null) {
            for (Role role : user.getRoles()) {
                if ("ROLE_ADMIN".equals(role.getName())) {
                    school = schoolRepos.findSchoolByName(schoolName);
                    if (school == null) {
                        model.addAttribute("error", "Вы неверно указали школу");
                        return "addclass";
                    }
                    break;
                }
            }
        }
        school = getSchoolIfUserIsOperator(school, user);
        if (checkName(name, model)) return "addclass";
        if (checkClass(name, model)) return "addclass";
        createSchoolClass(name, school);
        model.addAttribute("completed", "Класс " + name + " был добавлен");
        return "addclass";
    }

    private void createSchoolClass(String name, School school) {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName(name);
        schoolClass.setSchool(school);
        school.addClass(schoolClass);
        schoolClassRepos.save(schoolClass);
        schoolRepos.save(school);
    }

    private boolean checkClass(String name, Model model) {
        if (schoolClassRepos.findSchoolClassByName(name) != null){

            model.addAttribute("error", "Такой класс уже есть");
            return true;

        }
        return false;
    }

    private boolean checkName(String name, Model model) {
        if (name == null) {
            model.addAttribute("error", "Введите имя!");
            return true;
        }
        if (name.length() < 6) {
            model.addAttribute("error", "Введите полное название");
            return true;
        }
        return false;
    }

    private School getSchoolIfUserIsOperator(School school, User user) {
        if (school == null){
            school = user.getSchool();
        }
        return school;
    }

}
