/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general.services;

import general.entities.School;
import general.entities.SchoolClass;
import general.reposes.SchoolClassRepos;
import general.reposes.SchoolRepos;
import general.reposes.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String addClass(String name, String schoolName, Model model){
    if (name == null){
            model.addAttribute("error", "Введите имя!");
            return "addclass";
        }
        if (schoolClassRepos.findSchoolClassByName(name) != null){
            model.addAttribute("error", "Такой класс уже существует");
            return "addclass";
        }
        if (name.length() < 6){
            model.addAttribute("error", "Введите полное название");
            return "addclass";
        }
        SchoolClass schoolClass = new SchoolClass();
        School school = schoolRepos.findSchoolByName(schoolName);
        schoolClass.setName(name);
        schoolClass.setSchool(school);
        school.addClass(schoolClass);
        schoolClassRepos.save(schoolClass);
        schoolRepos.save(school);
        model.addAttribute("completed",  "Класс "+ name + " был добавлен");
        return "addclass";}
    
}
