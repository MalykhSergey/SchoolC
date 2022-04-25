package general.services;

import general.entities.SchoolClass;
import general.entities.Student;
import general.reposes.UserRepos;
import general.utils.CheckDataBoolAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepos userRepos;

    @Autowired
    public UserService(UserRepos userRepos) {
        this.userRepos = userRepos;
    }

    public CheckDataBoolAnswer checkInputData(String name, String password) {
        if (password == null) {
            return new CheckDataBoolAnswer(false, "Введите пароль");
        }
        if (password.length() < 4) {
            return new CheckDataBoolAnswer(false, "Введите пароль длинее 5 символов");
        }
        if (password.length() > 20) {
            return new CheckDataBoolAnswer(false, "Ваш пароль слишком длинный!");
        }
        if (name == null) {
            return new CheckDataBoolAnswer(false, "Введите имя");
        }
        if (name.length() > 25) {
            return new CheckDataBoolAnswer(false, "Введите имя короче 15 символов");
        }
        if (userRepos.findUserByName(name) != null) {
            return new CheckDataBoolAnswer(false, "Введите другое имя");
        }
        return new CheckDataBoolAnswer(true, null);
    }

    @Transactional
    public void updateClassForStudent(SchoolClass schoolClass, Student student) {
        userRepos.updateClassForUser(schoolClass.getId(), student.getId());
        userRepos.updateClassForStudentInJoinTable(schoolClass.getId(), student.getId());
    }

    public String getUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
