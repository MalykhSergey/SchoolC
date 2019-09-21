package general.controllers;

import general.entities.School;
import general.entities.SchoolClass;
import general.entities.Teacher;
import general.reposes.SchoolClassRepos;
import general.reposes.SchoolRepos;
import general.reposes.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AddSchoolController {
    @Autowired
    UserRepos userRepos;
    @Autowired
    SchoolRepos schoolRepos;
    @Autowired
    SchoolClassRepos schoolClassRepos;
        @RequestMapping(value = "/addschool", method = RequestMethod.GET)
        public String addSchoolGet(){
            return "addschool";
        }
        @RequestMapping(value = "/addschool", method = RequestMethod.POST)
        public String addSchoolPost(
                @RequestParam(name = "name") String name,
                Model model){
            if (name == null){
                model.addAttribute("error", "Введите имя!");
                return "addschool";
            }
            if (schoolRepos.findSchoolByName(name) != null){
                model.addAttribute("error", "Такая школа уже существует");
                return "addschool";
            }
            if (name.length() < 6){
                model.addAttribute("error", "Введите полное название");
                return "addschool";
            }
            School school = new School(name);
            school.setName(name);
            model.addAttribute("completed", "Школа успешно добавлена");
            schoolRepos.save(school);
            return "addschool";
        }

    }