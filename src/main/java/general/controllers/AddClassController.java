package general.controllers;

import general.entities.School;
import general.entities.SchoolClass;
import general.entities.Teacher;
import general.reposes.SchoolClassRepos;
import general.reposes.SchoolRepos;
import general.reposes.UserRepos;
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
    UserRepos userRepos;
    @Autowired
    SchoolRepos schoolRepos;
    @Autowired
    SchoolClassRepos schoolClassRepos;
    @RequestMapping(value = "/addclass", method = RequestMethod.GET)
    public String addClassGet(){
        return "addclass";
    }
    @RequestMapping(value = "/addclass", method = RequestMethod.POST)
    public String addClassPost(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "school")String schoolName, Model model){
        if (name == null){
            model.addAttribute("error", "Введите имя!");
            return "addclass";
        }
        if (schoolClassRepos.findSchoolClassByName(name) != null){
            model.addAttribute("error", "Такой класс уже существует");
            return "addclass";
        }
        if (name.length() < 6){
            model.addAttribute("error", "Введите полное название");
            return "addclass";
        }
        SchoolClass schoolClass = new SchoolClass();
        School school = schoolRepos.findSchoolByName(schoolName);
        schoolClass.setName(name);
        schoolClass.setSchool(school);
        school.addClass(schoolClass);
        schoolClassRepos.save(schoolClass);
        schoolRepos.save(school);
        model.addAttribute("completed",  "Класс "+ name + " был добавлен");
        return "addclass";
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
        if (className == null | teacherName == null | schoolName == null){
            model.addAttribute("error", "Введите имя!");
            return "addclassforteacher";
        }
        if (schoolClassRepos.findSchoolClassByName(className) == null){
            model.addAttribute("error", "Такой школы не существует");
            return "addclassforteacher";
        }
        if (schoolRepos.findSchoolByName(schoolName) == null){
            model.addAttribute("error", "Такого класса не существует");
            return "addclassforteacher";
        }
        if (userRepos.findUserByName(teacherName) == null){
            model.addAttribute("error", "Такого учителя не существует");
            return "addclassforteacher";
        }
        School school = schoolRepos.findSchoolByName(schoolName);
        Teacher teacher = (Teacher) userRepos.findUserByNameAndSchool(teacherName, school);
        SchoolClass schoolClass = schoolClassRepos.findSchoolClassByName(className);
        teacher.addSchoolClass(schoolClass);
        userRepos.save(teacher);
        model.addAttribute("completed", "Учитель привязан "+teacherName+" к классу");
        return "addclassforteacher";
    }
}
