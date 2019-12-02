package general.services;

import general.entities.*;
import general.reposes.AnswerRepos;
import general.reposes.TaskRepos;
import general.reposes.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class AnswerService {
    @Autowired
    TaskRepos taskRepos;
    @Autowired
    UserRepos userRepos;
    @Autowired
    AnswerRepos answerRepos;
    @Value("${upload.path}")
    private String uploadPath;
    public String addAnswer(String id, String body, MultipartFile[] files, Model model) throws IOException {
                Task task = taskRepos.findTasksById(Long.parseLong(id));
                Student student = (Student) (userRepos.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName()));
                Boolean bool = false;
                for (Task studentTask : student.getSchoolClass().getTasks()){
                    if (studentTask == task){
                        bool = true;
                        break;
                    }
                }
                if (task.getStatus().equals("Решено!"))bool = false;
                if (bool) {
                    Answer answer = new Answer(student, task);
                    answer.setBody(body);
                    for (MultipartFile file : files) {
                        if (!file.isEmpty()) {
                            String filePath =UUID.randomUUID().toString() + "." + file.getOriginalFilename();
                            file.transferTo(new File( uploadPath +  filePath));
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
                    return "addanswer";
                }
        return "redirect:/";
    }
    public String checkAnswer(String answerId, Model model){
        Teacher teacher = (Teacher) (userRepos.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName()));
        Answer answer = answerRepos.findAnswerById(Long.parseLong(answerId));
        Task task = answer.getTask();
        Boolean bool = false;
        for (SchoolClass sc : teacher.getSchoolClassSet()){
            for (Student st : sc.getStudents()){
                if (st.getName().equals(st.getName())){
                    bool = true;
                    break;
                }
            }
            if (bool == true){
                break;
            }
        }
        model.addAttribute("taskName", task.getName());
        model.addAttribute("taskBody", task.getName());
        model.addAttribute("answerBody", answer.getBody());
        model.addAttribute("fileNames", answer.getFilename());
        model.addAttribute("studentNames", answer.getStudent().getName());
        return "checkAnswer";
    }
}
