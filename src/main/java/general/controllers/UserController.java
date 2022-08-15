package general.controllers;

import general.controllers.api.dtos.UserDTO;
import general.controllers.forms.ClassForm;
import general.controllers.forms.SchoolForm;
import general.controllers.forms.UserForm;
import general.entities.Role;
import general.services.SchoolClassService;
import general.services.UserService;
import general.utils.Result;
import general.utils.UserDetailsExtended;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    private final UserService userService;
    private final SchoolClassService schoolClassService;
    private final String addUserPage = "/userControllerPages/AddUser";
    private final String setClassForStudentPage = "/userControllerPages/SetClassForStudent";

    @Autowired
    public UserController(UserService userService, SchoolClassService schoolClassService) {
        this.userService = userService;
        this.schoolClassService = schoolClassService;
    }

    @GetMapping(value = "/adduser")
    public String addUserGet(@AuthenticationPrincipal UserDetailsExtended userDetailsExtended, Model model) {
        if (userDetailsExtended.getUser().getRole() == Role.Operator)
            model.addAttribute("classes", schoolClassService.getAllClassesBySchool(userDetailsExtended.getUser().getSchool()));
        return addUserPage;
    }

    @PostMapping(value = "/adduser")
    public String addUserPost(
            @ModelAttribute("userForm") UserForm userForm,
            @ModelAttribute("schoolForm") SchoolForm schoolForm,
            @ModelAttribute("classForm") ClassForm classForm,
            @AuthenticationPrincipal UserDetailsExtended userDetailsExtended,
            Model model) {
        if (userDetailsExtended.getUser().getRole() == Role.Operator)
            model.addAttribute("classes", schoolClassService.getAllClassesBySchool(userDetailsExtended.getUser().getSchool()));
        Result result = userService.createUser(new UserDTO(userForm, schoolForm, classForm), userDetailsExtended);
        if (result == Result.Ok)
            model.addAttribute("completed", "Пользователь с именем: " + userForm.getUserName() + " был успешно добавлен");
        else
            model.addAttribute("error", result.getError());
        return addUserPage;
    }

    @GetMapping(value = "/setClassForStudent")
    public String setClassForStudentGet(@AuthenticationPrincipal UserDetailsExtended userDetailsExtended, Model model) {
        if (userDetailsExtended.getUser().getRole() == Role.Operator)
            model.addAttribute("classes", schoolClassService.getAllClassesBySchool(userDetailsExtended.getUser().getSchool()));
        return setClassForStudentPage;
    }

    @PostMapping(value = "/setClassForStudent")
    public String setClassForStudentPost(
            @ModelAttribute("userForm") UserForm userForm,
            @ModelAttribute("classForm") ClassForm classForm,
            @AuthenticationPrincipal UserDetailsExtended userDetailsExtended,
            Model model
    ) {
        Result result = userService.setClassForStudent(new UserDTO(userForm, classForm), userDetailsExtended);
        if (result == Result.Ok)
            model.addAttribute("completed", "Ученик привязан к классу");
        else model.addAttribute("error", result.getError());
        return setClassForStudentPage;
    }
}
