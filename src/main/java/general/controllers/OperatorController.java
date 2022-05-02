package general.controllers;

import general.entities.User;
import general.reposes.SchoolClassRepos;
import general.reposes.UserRepos;
import general.services.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OperatorController {
    private final SchoolClassRepos schoolClassRepos;
    private final UserRepos userRepos;
    private final SchoolService schoolService;

    @Autowired
    public OperatorController(SchoolClassRepos schoolClassRepos, UserRepos userRepos, SchoolService schoolService) {
        this.userRepos = userRepos;
        this.schoolClassRepos = schoolClassRepos;
        this.schoolService = schoolService;
    }

    @GetMapping("/aboutSchool")
    public String seeClasses(Model model) {
        User user = this.userRepos.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("classes", schoolClassRepos.findSchoolClassBySchool(user.getSchool()));
        model.addAttribute("schoolName",user.getSchool().getName());
        model.addAttribute("teachers",schoolService.getTeachersBySchool(user.getSchool()));
        return "AboutSchool";
    }
}
