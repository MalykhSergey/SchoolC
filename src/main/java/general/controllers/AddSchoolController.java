package general.controllers;

import general.services.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AddSchoolController {
    @Autowired
    SchoolService schoolService;
        @RequestMapping(value = "/addschool", method = RequestMethod.GET)
        public String addSchoolGet(){
            return "addschool";
        }
        @RequestMapping(value = "/addschool", method = RequestMethod.POST)
        public String addSchoolPost(
                @RequestParam(name = "name") String name,
                Model model){
            return schoolService.addSchool(name, model);
        }

    }