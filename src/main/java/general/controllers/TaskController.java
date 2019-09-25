/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package general.controllers;

import general.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TaskController {

    @Autowired
    TaskService taskService;

    @RequestMapping(value = "/addtask", method = RequestMethod.GET)
    public String addTaskGet() {
        return "addtask";
    }

    @RequestMapping(value = "/addtask", method = RequestMethod.POST)
    public String addTaskPost(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "body") String body,
            @RequestParam(name = "schoolClassName") String nameOfSchoolClass, Model model
    ) {
        return taskService.addTask(name, body, nameOfSchoolClass, model);
    }
}
