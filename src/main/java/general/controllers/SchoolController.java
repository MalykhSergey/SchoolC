package general.controllers;

import general.controllers.forms.SchoolForm;
import general.utils.CheckDataBoolAnswer;
import general.services.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SchoolController {
    private final SchoolService schoolService;
    private final String addSchoolPage = "/schoolControllerPages/AddSchool";

    @Autowired
    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @GetMapping(value = "/addschool")
    public String addSchoolGet() {
        return addSchoolPage;
    }

    @PostMapping(value = "/addschool")
    public String addSchoolPost(
            @ModelAttribute("schoolForm") SchoolForm schoolForm,
            Model model) {
        CheckDataBoolAnswer checkDataBoolAnswer = schoolService.checkSchoolName(schoolForm.getSchoolName());
        if (checkDataBoolAnswer.isTrue()) {
            schoolService.createSchool(schoolForm.getSchoolName());
            model.addAttribute("completed", "Школа успешно добавлена");
        } else
            model.addAttribute("error", checkDataBoolAnswer.getAnswer());
        return addSchoolPage;
    }

}