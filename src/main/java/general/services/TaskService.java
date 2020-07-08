/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general.services;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import general.entities.SchoolClass;
import general.entities.Student;
import general.entities.Task;
import general.entities.TaskStatusOfStudent;
import general.entities.Teacher;
import general.reposes.SchoolClassRepos;
import general.reposes.TaskRepos;
import general.reposes.TaskStatusOfStudentRepos;
import general.reposes.UserRepos;

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

    public String addTask(String name, String body, String nameOfSchoolClass, Model model, String dateString) throws IOException {
        Teacher teacher = (Teacher) (userRepos.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName()));
        SchoolClass schoolClass = schoolClassRepos.findSchoolClassByName(nameOfSchoolClass);
        if (checkInputData(name, body, nameOfSchoolClass, model, dateString, teacher, schoolClass, false))
            return "addtask";
        createTask(name, body, dateString, schoolClass);
        model.addAttribute("completed", "Задача для " + schoolClass.getName() + "класса добавлена");
        model.addAttribute("schoolClasses", teacher.getSchoolClassSet());
        return "addtask";
    }

    private void createTask(String name, String body, String dateString, SchoolClass schoolClass) throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar date1 = Calendar.getInstance();
        try {
            date1.setTime(simpleDateFormat.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Task task;
        task = new Task(name, body, schoolClass, date1);
        taskRepos.save(task);
        for(Student student:schoolClass.getStudents()){
            TaskStatusOfStudent taskStatusOfStudent = new TaskStatusOfStudent(student, task, "Не решено!");
            taskStatusOfStudentRepos.save(taskStatusOfStudent);
        }
        schoolClass.addTask(task);
        schoolClassRepos.save(schoolClass);
    }

    private boolean checkInputData(String name, String body, String nameOfSchoolClass, Model model, String dateString, Teacher teacher, SchoolClass schoolClass, boolean bool) {
        if (name == null | body == null | nameOfSchoolClass == null | dateString == null) {
            model.addAttribute("error", "Введите все значения");
            model.addAttribute("schoolClasses", teacher.getSchoolClassSet());
            return true;
        }
        for (SchoolClass checkSchoolClass : teacher.getSchoolClassSet()) {
            if (checkSchoolClass.getName().equals(schoolClass.getName())) {
                bool = true;
            }
        }
        if (bool == false) {
            model.addAttribute("error", "Неверный класс");
            model.addAttribute("schoolClasses", teacher.getSchoolClassSet());
            return true;
        }
        if (name.length() < 5 | body.length() < 25) {
            model.addAttribute("error", "Введите более полное описание или название задания");
            model.addAttribute("schoolClasses", teacher.getSchoolClassSet());
            return true;
        }
        if(name.length() > 40 | body.length()>2000){
            model.addAttribute("error", "Введите более короткое описание или название задания");
            model.addAttribute("schoolClasses", teacher.getSchoolClassSet());
            return true;
        }
        return false;
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
