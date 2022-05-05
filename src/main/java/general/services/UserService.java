package general.services;

import general.controllers.forms.UserForm;
import general.entities.SchoolClass;
import general.entities.Student;
import general.entities.User;
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

    @Transactional
    public void saveUser(User user) {
        userRepos.save(user);
    }

    public CheckDataBoolAnswer checkUserFormForCreate(UserForm userForm) {
        if (userForm.getPassword() == null) {
            return new CheckDataBoolAnswer(false, "Введите пароль");
        }
        if (userForm.getPassword().length() < 4) {
            return new CheckDataBoolAnswer(false, "Введите пароль длинее 5 символов");
        }
        if (userForm.getPassword().length() > 20) {
            return new CheckDataBoolAnswer(false, "Ваш пароль слишком длинный!");
        }
        if (userForm.getUserName() == null) {
            return new CheckDataBoolAnswer(false, "Введите имя");
        }
        if (userForm.getUserName().length() > 25) {
            return new CheckDataBoolAnswer(false, "Введите имя короче 25 символов");
        }
        if (userRepos.findUserByName(userForm.getUserName()) != null) {
            return new CheckDataBoolAnswer(false, "Введите другое имя");
        }
        return new CheckDataBoolAnswer(true, null);
    }

    public User getUserByName(String name){
        return userRepos.findUserByName(name);
    }

    public String getCurrentUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
