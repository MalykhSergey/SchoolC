package general.controllers;

import general.utils.CheckDataBoolAnswer;
import general.services.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SchoolController {
    private final SchoolService schoolService;

    @Autowired
    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @GetMapping(value = "/addschool")
    public String addSchoolGet() {
        return "AddSchool";
    }

    @PostMapping(value = "/addschool")
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