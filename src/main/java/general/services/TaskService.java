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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
    
    public String addTask(String name, String body, String nameOfSchoolClass, Model model, String dateString) {
        if (name == null | body == null | nameOfSchoolClass == null | dateString == null){
            model.addAttribute("error","Введите все значения");
            return "addtask";
        }
        SchoolClass schoolClass = schoolClassRepos.findSchoolClassByName(nameOfSchoolClass);
        if (schoolClass == null){
            model.addAttribute("error", "Неверный класс");
            return "addtask";
        }
        if (name.length() > 25 | name.length() < 5 | body.length() < 25){
            model.addAttribute("error", "Введите более полное описание задания");
            return "addtask";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar date1 = Calendar.getInstance();
        try {
            date1.setTime(simpleDateFormat.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Task task = null;
        task = new Task(name, body, schoolClass, date1);
        schoolClass.addTask(task);
        taskRepos.save(task);
        schoolClassRepos.save(schoolClass);
        model.addAttribute("completed","Задача для "+schoolClass.getName()+"класса добавлена");
        return "addtask";
    }
}
