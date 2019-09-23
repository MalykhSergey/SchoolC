package general.controllers;

import general.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    @Autowired
        UserService userService;
    @RequestMapping(value = "/adduser", method = RequestMethod.GET)
    public String addUserGet(){
        return "adduser";
    }
    @RequestMapping(value = "/adduser", method = RequestMethod.POST)
    public String addUserPost(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "typeOfUser") String type,
            @RequestParam(name = "school", required = false) String nameOfSchool,
            @RequestParam(name = "password") String password, Model model){
        return userService.addUser(name, type, nameOfSchool, password, model);
    }
}
