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

@Service
public class TeacherService {

    @Autowired
    UserRepos userRepos;
    @Autowired
    SchoolRepos schoolRepos;
    @Autowired
    SchoolClassRepos schoolClassRepos;

    public String addClassForTeacher(String teacherName, String className, Model model) {
        if (checkInputData(teacherName, className, model)) return "addclassforteacher";
        Teacher teacher = (Teacher) userRepos.findUserByName(teacherName);
        SchoolClass schoolClass = schoolClassRepos.findSchoolClassByName(className);
        teacher.addSchoolClass(schoolClass);
        userRepos.save(teacher);
        model.addAttribute("completed", "Учитель: " + teacherName + " привязан к классу!");
        return "addclassforteacher";
    }

    private boolean checkInputData(String teacherName, String className, Model model) {
        if (className == null | teacherName == null) {
            model.addAttribute("error", "Введите имя!");
            return true;
        }
        if (schoolClassRepos.findSchoolClassByName(className) == null) {
            model.addAttribute("error", "Такого класса не существует");
            return true;
        }
        if (userRepos.findUserByName(teacherName) == null) {
            model.addAttribute("error", "Такого учителя не существует");
            return true;
        }
        return false;
    }

    public String checkAnswer() {
    	return "";
    }
}
