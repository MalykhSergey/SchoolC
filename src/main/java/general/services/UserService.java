package general.services;

import general.controllers.forms.UserForm;
import general.entities.User;
import general.reposes.UserRepos;
import general.utils.ResultOfInputDataChecking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public ResultOfInputDataChecking checkUserFormForCreate(UserForm userForm) {
        if (userForm.getPassword() == null) {
            return new ResultOfInputDataChecking(false, "Введите пароль");
        }
        if (userForm.getPassword().length() < 4) {
            return new ResultOfInputDataChecking(false, "Введите пароль длинее 5 символов");
        }
        if (userForm.getPassword().length() > 20) {
            return new ResultOfInputDataChecking(false, "Ваш пароль слишком длинный!");
        }
        if (userForm.getUserName() == null) {
            return new ResultOfInputDataChecking(false, "Введите имя");
        }
        if (userForm.getUserName().length() > 25) {
            return new ResultOfInputDataChecking(false, "Введите имя короче 25 символов");
        }
        if (userRepos.findUserByName(userForm.getUserName()) != null) {
            return new ResultOfInputDataChecking(false, "Введите другое имя");
        }
        return new ResultOfInputDataChecking(true, null);
    }

    public User getUserByName(String name){
        return userRepos.findUserByName(name);
    }

    public String getCurrentUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public List<String> getNamesOfTeachersByClassId(Long classId){
        return userRepos.findNamesOfTeachersByClassId(classId);
    }

}
