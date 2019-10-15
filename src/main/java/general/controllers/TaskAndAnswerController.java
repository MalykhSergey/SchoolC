/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package general.controllers;

import general.entities.Answer;
import general.entities.Student;
import general.entities.Task;
import general.entities.User;
import general.reposes.AnswerRepos;
import general.reposes.TaskRepos;
import general.reposes.UserRepos;
import general.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author dmali
 */
@Controller
public class TaskAndAnswerController {
    @Value("${upload.path}")
    private String uploadPath;
    @Autowired
    TaskService taskService;
    @Autowired
    TaskRepos taskRepos;
    @Autowired
    UserRepos userRepos;
    @Autowired
    AnswerRepos answerRepos;
    @RequestMapping(value = "/addtask", method = RequestMethod.GET)
    public String addTaskGet() {
        return "addtask";
    }

    @RequestMapping(value = "/addtask", method = RequestMethod.POST)
    public String addTaskPost(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "body") String body,
            @RequestParam(name = "schoolClassName") String nameOfSchoolClass,
            @RequestParam(name = "date") String date,Model model
    ) {
        return taskService.addTask(name, body, nameOfSchoolClass, model, date);
    }
    @RequestMapping(value = "/addanswer", method = RequestMethod.GET)
    public String addAnswerGet(@RequestParam(name = "id")String id, Model model){
        Task task = taskRepos.findTasksById(Long.parseLong(id));
        Student student = (Student) userRepos.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
        Boolean bool = false;
        for (Task studentTask : student.getSchoolClass().getTasks()){
            if (studentTask == task){
                bool = true;
                break;
            }
        }
        if (task.getStatus().equals("Решено!"))bool = false;
        if (bool) {
            model.addAttribute("task", taskRepos.findTasksById(Long.parseLong(id)));
        }
        else return "redirect:/";
        return "addanswer";
    }
    @RequestMapping(value = "/addanswer", method = RequestMethod.POST)
    public String addAnswerPost(@RequestParam (name = "id") String id,
                                @RequestParam (name = "body") String body,
                                @RequestParam MultipartFile[] files,
                                Model model) throws IOException {
        Task task = taskRepos.findTasksById(Long.parseLong(id));
        Student student = (Student) userRepos.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
        Boolean bool = false;
        for (Task studentTask : student.getSchoolClass().getTasks()){
            if (studentTask == task){
                bool = true;
                break;
            }
        }
        if (task.getStatus().equals("Решено!"))bool = false;
        if (bool){
            Answer answer = new Answer(student, task);
            answer.setBody(body);
            for (MultipartFile file : files){
                if (!file.isEmpty()){
                    String filePath = uploadPath +"/"+ UUID.randomUUID().toString() +"."+ file.getOriginalFilename();
                    file.transferTo(new File(filePath));
                    answer.addFileName(filePath);
                }
            }
            student.addAnswer(answer);
            task.addAnswer(answer);
            answerRepos.save(answer);
            task.setStatus("Решено!");
            taskRepos.save(task);
            model.addAttribute("task", task);
            model.addAttribute("completed", "Ответ успешно добавлен");
        }
        else return "redirect:/";
        return "addanswer";
    }
}
