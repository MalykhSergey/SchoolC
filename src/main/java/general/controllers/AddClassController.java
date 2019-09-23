package general.controllers;

import general.entities.School;
import general.entities.SchoolClass;
import general.entities.Teacher;
import general.reposes.SchoolClassRepos;
import general.reposes.SchoolRepos;
import general.reposes.UserRepos;
import general.services.SchoolClassService;
import general.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class AddClassController {
    @Autowired
    TeacherService teacherService;
    @Autowired
    SchoolClassService schoolClassService;
    @RequestMapping(value = "/addclass", method = RequestMethod.GET)
    public String addClassGet(){
        return "addclass";
    }
    @RequestMapping(value = "/addclass", method = RequestMethod.POST)
    public String addClassPost(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "school")String schoolName, Model model){
        return schoolClassService.addClass(name, schoolName, model);
    }
    @RequestMapping(value = "/adft", method = RequestMethod.GET)
    public String addClassForTeacherGet(){
        return "addclassforteacher";
    }
    @RequestMapping(value = "/adft", method = RequestMethod.POST)
    public String addClassForTeacherPost(
            @RequestParam(name = "className") String className,
            @RequestParam(name = "teacherName") String teacherName,
            @RequestParam(name = "schoolName") String schoolName, Model model){
        return teacherService.addClassForTeacher(teacherName, className, schoolName, model);
    }
}
