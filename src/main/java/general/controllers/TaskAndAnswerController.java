/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package general.controllers;

import general.entities.*;
import general.reposes.*;
import general.services.AnswerService;
import general.services.TaskService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
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
    TaskStatusOfStudentRepos taskStatusOfStudentRepos;
    @Autowired
    TaskService taskService;
    @Autowired
    AnswerService answerService;
    @Autowired
    TaskRepos taskRepos;
    @Autowired
    UserRepos userRepos;
    @Autowired
    AnswerRepos answerRepos;
    @Autowired
    SchoolClassRepos schoolClassRepos;
    @RequestMapping(value = "/addtask", method = RequestMethod.GET)
    public String addTaskGet(Model model) {
        Teacher teacher = (Teacher) (userRepos.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.addAttribute("schoolClasses", teacher.getSchoolClassSet());
        return "addtask";
    }

    @RequestMapping(value = "/addtask", method = RequestMethod.POST)
    public String addTaskPost(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "body") String body,
            @RequestParam(name = "schoolClassName") String nameOfSchoolClass,
            @RequestParam(name = "date") String date,
            @RequestParam MultipartFile[] files,
            Model model
    ) throws IOException {
        return taskService.addTask(name, body, nameOfSchoolClass, files, model, date);
    }
    @RequestMapping(value = "/addanswer", method = RequestMethod.GET)
    public String addAnswerGet(@RequestParam(name = "id")String id, Model model){
                Task task = taskRepos.findTaskById(Long.parseLong(id));
                Student student = (Student) (userRepos.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName()));
                Boolean bool = false;
                for (Task studentTask : student.getSchoolClass().getTasks()) {
                    if (studentTask == task) {
                        bool = true;
                        if (taskStatusOfStudentRepos.findTaskStatusOfStudentByStudentAndTask(student, task).getStatus().equals("Решено!")){
                            bool = false;
                            break;
                        }
                        break;
                    }
                }
                if (bool) {
                    model.addAttribute("task", taskRepos.findTaskById(Long.parseLong(id)));
                    return "addanswer";
                }
        return "redirect:/";
    }
    @RequestMapping(value = "/addanswer", method = RequestMethod.POST)
    public String addAnswerPost(@RequestParam (name = "id") String id,
                                @RequestParam (name = "body") String body,
                                @RequestParam MultipartFile[] files,
                                Model model) throws IOException {
        return answerService.addAnswer(id, body, files, model);
    }
    @RequestMapping(value = "/tasksOfClass", method = RequestMethod.GET)
    public String taskOfClass(
            @RequestParam(name = "id") String id,
            Model model
    ){
        model.addAttribute("students", schoolClassRepos.findSchoolClassById(Long.parseLong(id)).getStudents());
        return taskService.getTaskByClass(id, model);
    }
    @RequestMapping(value = "/answersOfTask", method = RequestMethod.GET)
    public String answerOfTask(
            @RequestParam(name = "taskId")String taskId,
            @RequestParam(name = "classId")String classId,
            Model model
    ){
    return taskService.getAnswerByTask(taskId, classId, model);
    }
    @RequestMapping(value = "/checkAnswer", method = RequestMethod.GET)
    public String checkAnswerGet(
            @RequestParam(name = "id") String answerId,
            Model model){
        Teacher teacher = (Teacher) (userRepos.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName()));
        Answer answer = answerRepos.findAnswerById(Long.parseLong(answerId));
        Task task = answer.getTask();
        Boolean isStudentInClassOfTeacher = false;
        for (SchoolClass schoolClass : teacher.getSchoolClassSet()){
            if (schoolClass.getName().equals(answer.getStudent().getSchoolClass().getName())){
                isStudentInClassOfTeacher = true;
                model.addAttribute("taskName", task.getName());
                model.addAttribute("taskBody", task.getName());
                model.addAttribute("answerBody", answer.getBody());
                model.addAttribute("fileNames", answer.getFilename());
                model.addAttribute("studentName", answer.getStudent().getName());
                break;
            }
        }
        return "checkAnswer";
    }
    @RequestMapping(value = "/checkAnswer", method = RequestMethod.POST)
    public String checkAnswerPost(@RequestParam(name = "id")String id,
                                  @RequestParam(name = "mark")String mark,
                                  @RequestParam(name = "comment")String comment){
        return answerService.checkAnswer(id, mark, comment);
    }
    @RequestMapping(value = "/files", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void getFile(@RequestParam("fileName") String fileName, HttpServletResponse response) {
            try {
                response.setHeader("Content-Disposition", "attachment; filename="+fileName);
                InputStream is = new FileInputStream(uploadPath+fileName);
                IOUtils.copy(is, response.getOutputStream());
                response.flushBuffer();
                is.close();
            } catch (IOException ex) {
                throw new RuntimeException("IOError writing file to output stream");
            }
    }
}
