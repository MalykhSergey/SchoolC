package general.services;

import general.controllers.dto.ClassDTO;
import general.controllers.dto.SchoolDTO;
import general.controllers.dto.UserDTO;
import general.entities.*;
import general.reposes.SchoolClassRepos;
import general.reposes.SchoolRepos;
import general.reposes.UserRepos;
import general.utils.Result;
import general.utils.StringLengthConstants;
import general.utils.UserDetailsExtended;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final UserRepos userRepos;
    private final SchoolRepos schoolRepos;
    private final SchoolClassRepos schoolClassRepos;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepos userRepos, SchoolRepos schoolRepos, SchoolClassRepos schoolClassRepos, PasswordEncoder passwordEncoder) {
        this.userRepos = userRepos;
        this.schoolRepos = schoolRepos;
        this.schoolClassRepos = schoolClassRepos;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Result createUser(UserDTO userDTO, ClassDTO classDTO, SchoolDTO schoolDTO, UserDetailsExtended userDetailsExtended) {
        Result result = validateUserNameAndPassword(userDTO.getUserName(), userDTO.getPassword());
        if (result != Result.Ok) return result;
        School school;
        User authenticatedUser = userDetailsExtended.getUser();
        school = getSchoolByRole(schoolDTO, authenticatedUser);
        if (school == null) return Result.InvalidSchoolName;
        SchoolClass schoolClass = null;
        if (userDTO.getRole() == Role.Student) {
            schoolClass = getSchoolClassByRole(classDTO, authenticatedUser, school);
            if (schoolClass == null) return Result.InvalidClassName;
        }
        saveUser(userDTO.getRole().createUserByRole(userDTO.getUserName(), passwordEncoder.encode(userDTO.getPassword()), school, schoolClass));
        return Result.Ok;
    }

    private SchoolClass getSchoolClassByRole(ClassDTO classDTO, User authenticatedUser, School school) {
        if (authenticatedUser.getRole() == Role.Operator)
            return schoolClassRepos.findSchoolClassById(classDTO.getClassId());
        else
            return schoolClassRepos.findSchoolClassByNameAndClassNumberAndSchool(classDTO.getClassName(), classDTO.getClassNumber(), school);
    }

    private School getSchoolByRole(SchoolDTO schoolDTO, User authenticatedUser) {
        if (authenticatedUser.getRole() == Role.Operator) {
            return authenticatedUser.getSchool();
        } else return schoolRepos.findSchoolByName(schoolDTO.getSchoolName());
    }

    @Transactional
    public Result setClassForStudent(UserDTO userDTO, ClassDTO classDTO, UserDetailsExtended userDetailsExtended) {
        User authenticatedUser = userDetailsExtended.getUser();
        Student student;
        if (authenticatedUser.getRole() == Role.Operator)
            student = userRepos.findStudentByNameAndSchool(userDTO.getUserName(), authenticatedUser.getSchool());
        else student = userRepos.findStudentByName(userDTO.getUserName());
        if (student == null || student.getRole() != Role.Student) return Result.InvalidName;
        SchoolClass schoolClass = getSchoolClassByRole(classDTO, authenticatedUser, student.getSchool());
        if (schoolClass == null) return Result.InvalidClassName;
        userRepos.setClassForStudent(student.getId(), schoolClass.getId());
        return Result.Ok;
    }

    @Transactional
    public void saveUser(User user) {
        userRepos.save(user);
    }

    private Result validateUserNameAndPassword(String userName, String password) {
        if (password == null) {
            return Result.PasswordIsNull;
        }
        if (password.length() < StringLengthConstants.Password.getMinLength()) {
            return Result.TooShortPassword;
        }
        if (password.length() > StringLengthConstants.Password.getMaxLength()) {
            return Result.TooLongPassword;
        }
        if (userName == null) {
            return Result.NameIsNull;
        }
        if (userName.length() < StringLengthConstants.Name.getMinLength()) {
            return Result.TooShortName;
        }
        if (userName.length() > StringLengthConstants.Name.getMaxLength()) {
            return Result.TooLongName;
        }
        if (userRepos.findUserByName(userName) != null) {
            return Result.UserIsExists;
        }
        return Result.Ok;
    }

    public User getUserByName(String name) {
        return userRepos.findUserByName(name);
    }

    public Teacher getTeacherByName(String name) {
        return userRepos.findTeacherByName(name);
    }

    public String getCurrentUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public List<String> getNamesOfTeachersByClassId(Long classId) {
        return userRepos.findNamesOfTeachersByClassId(classId);
    }

}
