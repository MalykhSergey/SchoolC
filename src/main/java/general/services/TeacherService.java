/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general.services;

import general.entities.Role;
import general.entities.School;
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

    public String addClassForTeacher(String teacherName, String className, String schoolName, Model model) {
        User user = userRepos.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
        School school = null;
        if (schoolName != null) {
            for (Role role : user.getRoles()) {
                System.out.println(role.getName());
                if ("ROLE_ADMIN".equals(role.getName())) {
                    school = schoolRepos.findSchoolByName(schoolName);
                    if (school == null) {
                        model.addAttribute("error", "Вы неверно указали школу");
                        return "addclassforteacher";
                    }
                    break;
                }
            }
        }
        if (school == null){
            school = user.getSchool();
        }
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
        Teacher teacher = (Teacher) userRepos.findUserByNameAndSchool(teacherName, school);
        SchoolClass schoolClass = schoolClassRepos.findSchoolClassByName(className);
        teacher.addSchoolClass(schoolClass);
        userRepos.save(teacher);
        model.addAttribute("completed", "Учитель: " + teacherName + " привязан к классу!");
        return "addclassforteacher";
    }
}
