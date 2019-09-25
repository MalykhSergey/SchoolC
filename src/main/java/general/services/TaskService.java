/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package general.services;

import general.entities.SchoolClass;
import general.entities.Task;
import general.reposes.SchoolClassRepos;
import general.reposes.TaskRepos;
import general.reposes.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 *
 * @author dmali
 */
@Service
public class TaskService {
    
    @Autowired
            TaskRepos taskRepos;
    @Autowired
            SchoolClassRepos schoolClassRepos;
    @Autowired
            UserRepos userRepos;
    
    public String addTask(String name, String body, String nameOfSchoolClass, Model model) {
        if (name == null | body == null | nameOfSchoolClass == null){
            model.addAttribute("error","Введите все значения");
            return "addtask";
        }
        SchoolClass schoolClass = schoolClassRepos.findSchoolClassByName(nameOfSchoolClass);
        if (schoolClass == null){
            model.addAttribute("error", "Неверный класс");
            return "addtask";
        }
        if (name.length() > 25 | name.length() < 5 | body.length() > 150 | body.length() < 25){
            model.addAttribute("error", "Введите более полное описание задания");
            return "addtask";
        }
        Task task = new Task(name, body, schoolClass);
        schoolClass.addTask(task);
        taskRepos.save(task);
        schoolClassRepos.save(schoolClass);
        model.addAttribute("completed","Задача для "+schoolClass.getName()+"класса добавлена");
        return "addtask";
    }
}
