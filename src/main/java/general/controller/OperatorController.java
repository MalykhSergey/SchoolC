package general.controller;

import general.entity.User;
import general.service.SchoolClassService;
import general.service.SchoolService;
import general.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OperatorController {
    private final UserService userService;
    private final SchoolService schoolService;
    private final SchoolClassService schoolClassService;
    private final String aboutSchoolPage = "/schoolControllerPages/AboutSchool";
    private final String aboutClassPage = "/classControllerPages/AboutClass";

    @Autowired
    public OperatorController(UserService userService, SchoolService schoolService, SchoolClassService schoolClassService) {
        this.userService = userService;
        this.schoolClassService = schoolClassService;
        this.schoolService = schoolService;
    }

    @GetMapping("/aboutSchool")
    public String aboutSchool(Model model) {
        User user = userService.getUserByName(userService.getCurrentUserName());
        model.addAttribute("school", user.getSchool());
        model.addAttribute("teachers", schoolService.getTeachersBySchool(user.getSchool()));
        return aboutSchoolPage;
    }

    @GetMapping("/aboutClass/{classId}")
    public String aboutClass(@PathVariable("classId") Long classId, Model model) {
        model.addAttribute("class", schoolClassService.getClassById(classId));
        return aboutClassPage;
    }

    @GetMapping("/startNewYear")
    public String startNewYearGet() {
        return "StartNewYear";
    }

    @PostMapping("/startNewYear")
    public String startNewYearPost() {
        User user = userService.getUserByName(userService.getCurrentUserName());
        schoolService.startNewYearForSchoolId(user.getSchool().getId());
        return "redirect:/aboutSchool";
    }
}
