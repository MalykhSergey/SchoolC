package general.controllers;

import general.utils.CheckDataBoolAnswer;
import general.services.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SchoolController {
    private SchoolService schoolService;

    @Autowired
    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @RequestMapping(value = "/addschool", method = RequestMethod.GET)
    public String addSchoolGet() {
        return "AddSchool";
    }

    @RequestMapping(value = "/addschool", method = RequestMethod.POST)
    public String addSchoolPost(
            @RequestParam(name = "name") String name,
            Model model) {
        CheckDataBoolAnswer checkDataBoolAnswer = schoolService.checkSchoolName(name, model);
        if (checkDataBoolAnswer.isTrue()) {
            schoolService.createSchool(name);
            model.addAttribute("completed", "Школа успешно добавлена");
        } else
            model.addAttribute("error", checkDataBoolAnswer.getAnswer());
        return "AddSchool";
    }

}