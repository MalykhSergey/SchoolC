package general.controller;

import general.controller.dto.SchoolDTO;
import general.service.SchoolService;
import general.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
            @ModelAttribute("schoolDTO") SchoolDTO schoolDTO,
            Model model) {
        Result result = schoolService.createSchool(schoolDTO.getSchoolName());
        if (result == Result.Ok) {
            model.addAttribute("completed", "Школа успешно добавлена");
        } else
            model.addAttribute("error", result.getError());
        return addSchoolPage;
    }

}