package general.controllers;

import general.entities.*;
import general.reposes.TaskStatusOfStudentRepos;
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
    @Autowired
    TaskStatusOfStudentRepos taskStatusOfStudentRepos;
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model) {
        User user = userRepos.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
        for (Role role : user.getRoles()){
            if ("ROLE_STUDENT".equals(role.getName())){
                Student student = (Student) (user);
                Iterable<TaskStatusOfStudent> taskStatusOfStudents = taskStatusOfStudentRepos.findAllByStudent(student);
                List<TaskStatusOfStudent> oldtasks = new ArrayList<>();
                List<TaskStatusOfStudent> newtasks = new ArrayList<>();
                Calendar now = new GregorianCalendar();
                for (TaskStatusOfStudent taskStatusOfStudent : taskStatusOfStudents){
                    if (now.get(Calendar.DAY_OF_YEAR) <= taskStatusOfStudent.getTask().getDate().get(Calendar.DAY_OF_YEAR) && taskStatusOfStudent.getStatus().equals("Не решено!")){
                        newtasks.add(taskStatusOfStudent);
                    }
                    else{
                        oldtasks.add(taskStatusOfStudent);
                    }
                }
                model.addAttribute("oldtasks", oldtasks);
                model.addAttribute("newtasks", newtasks);
            }
            if ("ROLE_TEACHER".equals(role.getName())){
                Teacher teacher = (Teacher) (user);
                model.addAttribute("schoolClasses",teacher.getSchoolClassSet());
            }
        }
        return "home";
    }

}