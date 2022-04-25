package general.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import general.entities.SchoolClass;
import general.entities.Teacher;
import general.reposes.SchoolClassRepos;
import general.reposes.SchoolRepos;
import general.reposes.UserRepos;

@Service
public class TeacherService {

    private UserRepos userRepos;
    private SchoolClassRepos schoolClassRepos;

    @Autowired
    public TeacherService(UserRepos userRepos, SchoolRepos schoolRepos, SchoolClassRepos schoolClassRepos) {
        this.userRepos = userRepos;
        this.schoolClassRepos = schoolClassRepos;
    }

    public boolean checkInputDataForAddingClassToTeacher(String teacherName, String className, Model model) {
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
}
