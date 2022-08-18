package general.controller;

import general.controller.dto.ClassDTO;
import general.controller.dto.SchoolDTO;
import general.controller.dto.UserDTO;
import general.entity.Role;
import general.service.SchoolClassService;
import general.service.UserService;
import general.util.Result;
import general.util.UserDetailsExtended;
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
            @ModelAttribute("userDTO") UserDTO userDTO,
            @ModelAttribute("schoolDTO") SchoolDTO schoolDTO,
            @ModelAttribute("classDTO") ClassDTO classDTO,
            @AuthenticationPrincipal UserDetailsExtended userDetailsExtended,
            Model model) {
        if (userDetailsExtended.getUser().getRole() == Role.Operator)
            model.addAttribute("classes", schoolClassService.getAllClassesBySchool(userDetailsExtended.getUser().getSchool()));
        Result result = userService.createUser(userDTO, classDTO, schoolDTO, userDetailsExtended);
        if (result == Result.Ok)
            model.addAttribute("completed", "Пользователь с именем: " + userDTO.getUserName() + " был успешно добавлен");
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
            @ModelAttribute("userDTO") UserDTO userDTO,
            @ModelAttribute("classDTO") ClassDTO classDTO,
            @AuthenticationPrincipal UserDetailsExtended userDetailsExtended,
            Model model
    ) {
        if (userDetailsExtended.getUser().getRole() == Role.Operator)
            model.addAttribute("classes", schoolClassService.getAllClassesBySchool(userDetailsExtended.getUser().getSchool()));
        Result result = userService.setClassForStudent(userDTO, classDTO, userDetailsExtended);
        if (result == Result.Ok)
            model.addAttribute("completed", "Ученик привязан к классу");
        else model.addAttribute("error", result.getError());
        return setClassForStudentPage;
    }
}
