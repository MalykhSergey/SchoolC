package general.controllers;

import general.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
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
            @RequestParam(name = "password") String password,
            @RequestParam(name = "class", required = false)String nameOfSchoolClass, Model model){
        return userService.addUser(name, type, nameOfSchool, nameOfSchoolClass, password, model);
    }
    @RequestMapping(value = "/scfs", method = RequestMethod.GET)
    public String setClassForStudentGet(){
        return "setClassForStudent";
    }
    @RequestMapping(value = "/scfs", method = RequestMethod.POST)
    public String setClassForStudentPost(
            @RequestParam(name = "name")String name,
            @RequestParam(name = "class")String className,
            Model model
    ){
        return userService.setClassForStudent(name, className, model);
    }
}
