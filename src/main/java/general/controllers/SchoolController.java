package general.controllers;

import general.controllers.forms.SchoolForm;
import general.utils.Result;
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

    @GetMapping(value = "/addSchool")
    public String addSchoolGet() {
        return addSchoolPage;
    }

    @PostMapping(value = "/addSchool")
    public String addSchoolPost(
            @ModelAttribute("schoolForm") SchoolForm schoolForm,
            Model model) {
        Result<String> result = schoolService.checkSchoolName(schoolForm.getSchoolName());
        if (result.isDataValid()) {
            schoolService.createSchool(schoolForm.getSchoolName());
            model.addAttribute("completed", "Школа успешно добавлена");
        } else
            model.addAttribute("error", result.getResult());
        return addSchoolPage;
    }

}