/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general.controllers;

import general.entities.*;
import general.reposes.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

/**
 *
 * @author dmali
 */
@Controller
public class HomeController {
@Autowired
UserRepos userRepos;
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model) {
        User user = userRepos.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
        for (Role role : user.getRoles()){
            if ("ROLE_STUDENT".equals(role.getName())){
                Student student = (Student) (user);
                List<Task> tasks = student.getSchoolClass().getTasks();
                List<Task> oldtasks = new ArrayList<>();
                List<Task> newtasks = new ArrayList<>();
                Calendar now = new GregorianCalendar();
                for (Task task : tasks){
                    if (now.get(Calendar.DAY_OF_YEAR) <= task.getDate().get(Calendar.DAY_OF_YEAR) && task.getStatus().equals("Не решено!")){
                        newtasks.add(task);
                    }
                    else{
                        oldtasks.add(task);
                    }
                }
                model.addAttribute("oldtasks", oldtasks);
                model.addAttribute("newtasks", newtasks);
            }
            if ("ROLE_TEACHER".equals(role.getName())){
                Teacher teacher = (Teacher) (user);
                model.addAttribute("schoolClasses", teacher.getSchoolClassSet());
            }
        }
        return "home";
    }

}
