/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general.services;

import general.entities.SchoolClass;
import general.entities.Teacher;
import general.entities.User;
import general.reposes.SchoolClassRepos;
import general.reposes.SchoolRepos;
import general.reposes.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 *
 * @author dmali
 */
@Service
public class TeacherService {

    @Autowired
    UserRepos userRepos;
    @Autowired
    SchoolRepos schoolRepos;
    @Autowired
    SchoolClassRepos schoolClassRepos;

    public String addClassForTeacher(String teacherName, String className, Model model) {
        User user = userRepos.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
        if (className == null | teacherName == null) {
            model.addAttribute("error", "Введите имя!");
            return "addclassforteacher";
        }
        if (schoolClassRepos.findSchoolClassByName(className) == null) {
            model.addAttribute("error", "Такого класса не существует");
            return "addclassforteacher";
        }
        if (userRepos.findUserByName(teacherName) == null) {
            model.addAttribute("error", "Такого учителя не существует");
            return "addclassforteacher";
        }
        Teacher teacher = (Teacher) userRepos.findUserByName(teacherName);
        SchoolClass schoolClass = schoolClassRepos.findSchoolClassByName(className);
        teacher.addSchoolClass(schoolClass);
        userRepos.save(teacher);
        model.addAttribute("completed", "Учитель: " + teacherName + " привязан к классу!");
        return "addclassforteacher";
    }
}
