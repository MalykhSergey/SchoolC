/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general.services;

import general.entities.*;
import general.reposes.SchoolClassRepos;
import general.reposes.TaskRepos;
import general.reposes.TaskStatusOfStudentRepos;
import general.reposes.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
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
    @Autowired
    TaskStatusOfStudentRepos taskStatusOfStudentRepos;

    public String addTask(String name, String body, String nameOfSchoolClass, Model model, String dateString) {
        Teacher teacher = (Teacher) (userRepos.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName()));
        SchoolClass schoolClass = schoolClassRepos.findSchoolClassByName(nameOfSchoolClass);
        boolean bool = false;
        if (name == null | body == null | nameOfSchoolClass == null | dateString == null) {
            model.addAttribute("error", "Введите все значения");
            return "addtask";
        }
        for (SchoolClass checkSchoolClass : teacher.getSchoolClassSet()) {
            if (checkSchoolClass.getName().equals(schoolClass.getName())) {
                bool = true;
            }
        }
        if (bool = false) {
            model.addAttribute("error", "Неверный класс");
            return "addtask";
        }
        if (name.length() > 25 | name.length() < 5 | body.length() < 25) {
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
        taskRepos.save(task);
        for(Student student:schoolClass.getStudents()){
            TaskStatusOfStudent taskStatusOfStudent = new TaskStatusOfStudent(student, task, "Не решено!");
            taskStatusOfStudentRepos.save(taskStatusOfStudent);
        }
        schoolClass.addTask(task);
        schoolClassRepos.save(schoolClass);
        model.addAttribute("completed", "Задача для " + schoolClass.getName() + "класса добавлена");
        model.addAttribute("schoolClasses", teacher.getSchoolClassSet());
        return "addtask";
    }

    public String getTaskByClass(String id, Model model) {
        Teacher teacher = (Teacher) userRepos.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
        SchoolClass schoolClass = schoolClassRepos.findSchoolClassById(Long.parseLong(id));
        for (SchoolClass sc : teacher.getSchoolClassSet()) {
            if (sc.getId().equals(schoolClass.getId())) {
                List<Task> tasks = schoolClass.getTasks();
                Collections.reverse(tasks);
                model.addAttribute("tasks", tasks);
                model.addAttribute("schoolClass", schoolClass);
                break;
            }
        }
        return "home";
    }

    public String getAnswerByTask(String taskId, String classId, Model model) {
        Teacher teacher = (Teacher) userRepos.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
        Task task = taskRepos.findTaskById(Long.parseLong(taskId));
        SchoolClass schoolClass = schoolClassRepos.findSchoolClassById(Long.parseLong(classId));
        for (SchoolClass sc : teacher.getSchoolClassSet()) {
            if (sc.getId().equals(schoolClass.getId())) {
                for (Task tk : sc.getTasks()) {
                    if (tk.getId().equals(task.getId())) {
                        model.addAttribute("answers", task.getAnswers());
                        model.addAttribute("task", task);
                    }
                }
                break;
            }
        }
        return "home";
    }
}
