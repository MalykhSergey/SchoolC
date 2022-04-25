package general.controllers;

import general.entities.*;
import general.reposes.SchoolClassRepos;
import general.reposes.SchoolRepos;
import general.reposes.UserRepos;
import general.services.SchoolClassService;
import general.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ClassController {
    private TeacherService teacherService;
    private SchoolClassService schoolClassService;
    private UserRepos userRepos;
    private SchoolRepos schoolRepos;
    private SchoolClassRepos schoolClassRepos;

    @Autowired
    public ClassController(TeacherService teacherService, SchoolClassService schoolClassService, UserRepos userRepos, SchoolRepos schoolRepos, SchoolClassRepos schoolClassRepos) {
        this.teacherService = teacherService;
        this.schoolClassService = schoolClassService;
        this.userRepos = userRepos;
        this.schoolRepos = schoolRepos;
        this.schoolClassRepos = schoolClassRepos;
    }

    @RequestMapping(value = "/addclass", method = RequestMethod.GET)
    public String addClassGet() {
        return "AddClass";
    }

    @RequestMapping(value = "/addclass", method = RequestMethod.POST)
    public String addClassPost(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "school", required = false) String schoolName, Model model) {
        School school = null;
        User user = userRepos.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user.getRole() == Role.Admin) {
            school = schoolRepos.findSchoolByName(schoolName);
            if (school == null) {
                model.addAttribute("error", "Вы неверно указали школу");
                return "addclass";
            }
        }
        if (school == null)
            school = user.getSchool();
        if (schoolClassService.checkClassName(name)) {
            model.addAttribute("error", "Введите полное название");
            return "addclass";
        }
        if (schoolClassService.checkClass(name)) {
            model.addAttribute("error", "Такой класс уже есть");
            return "addclass";
        }
        schoolClassService.createNewSchoolClass(name, school);
        model.addAttribute("completed", "Класс " + name + " был добавлен");
        return "addclass";
    }

    @RequestMapping(value = "/adcft", method = RequestMethod.GET)
    public String addClassForTeacherGet() {
        return "AddClassForTeacher";
    }

    @RequestMapping(value = "/adcft", method = RequestMethod.POST)
    public String addClassForTeacherPost(
            @RequestParam(name = "className") String className,
            @RequestParam(name = "teacherName") String teacherName, Model model) {
        if (teacherService.checkInputDataForAddingClassToTeacher(teacherName, className, model))
            return "AddClassForTeacher";
        Teacher teacher = (Teacher) userRepos.findUserByName(teacherName);
        SchoolClass schoolClass = schoolClassRepos.findSchoolClassByName(className);
        teacher.addSchoolClass(schoolClass);
        userRepos.save(teacher);
        model.addAttribute("completed", "Учитель: " + teacherName + " привязан к классу!");
        return "AddClassForTeacher";
    }
}
