/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package general.services;

import general.entities.School;
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
public class SchoolService{
    @Autowired
    UserRepos userRepos;
    @Autowired
    SchoolRepos schoolRepos;
    @Autowired
    SchoolClassRepos schoolClassRepos;
    public String addSchool(String name, Model model){
        if (checkSchoolName(name, model)) return "addschool";
        return createSchool(name, model);
    }

    private String createSchool(String name, Model model) {
        School school = new School(name);
        school.setName(name);
        model.addAttribute("completed", "Школа успешно добавлена");
        schoolRepos.save(school);
        return "addschool";
    }

    private boolean checkSchoolName(String name, Model model) {
        if (name == null){
                model.addAttribute("error", "Введите имя!");
            return true;
            }
        if (schoolRepos.findSchoolByName(name) != null){
            model.addAttribute("error", "Такая школа уже существует");
            return true;
        }
        if (name.length() < 6){
            model.addAttribute("error", "Введите полное название");
            return true;
        }
        return false;
    }
}
