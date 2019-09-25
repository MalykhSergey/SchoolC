/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general.controllers;

import general.entities.Student;
import general.entities.User;
import general.reposes.TaskRepos;
import general.reposes.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author dmali
 */
@Controller
public class HomeController {
@Autowired
UserRepos userRepos;
@Autowired
TaskRepos taskRepos;
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model) {
        Student user = (Student) (userRepos.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName()));
        System.out.println(taskRepos.findAllBySchoolClass(user.getSchoolClass()));
        model.addAttribute("tasks",taskRepos.findAllBySchoolClass(user.getSchoolClass()));
        return "home";
    }

}
