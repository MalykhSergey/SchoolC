package general.service;

import general.controller.dto.ClassDTO;
import general.controller.dto.SchoolDTO;
import general.controller.dto.UserDTO;
import general.entity.*;
import general.repository.SchoolClassRepository;
import general.repository.SchoolRepository;
import general.repository.UserRepository;
import general.util.Result;
import general.util.StringLengthConstants;
import general.util.UserDetailsExtended;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;
    private final SchoolClassRepository schoolClassRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, SchoolRepository schoolRepository, SchoolClassRepository schoolClassRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.schoolRepository = schoolRepository;
        this.schoolClassRepository = schoolClassRepository;
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
            return schoolClassRepository.findSchoolClassById(classDTO.getClassId());
        else
            return schoolClassRepository.findSchoolClassByNameAndClassNumberAndSchool(classDTO.getClassName(), classDTO.getClassNumber(), school);
    }

    private School getSchoolByRole(SchoolDTO schoolDTO, User authenticatedUser) {
        if (authenticatedUser.getRole() == Role.Operator) {
            return authenticatedUser.getSchool();
        } else return schoolRepository.findSchoolByName(schoolDTO.getSchoolName());
    }

    @Transactional
    public Result setClassForStudent(UserDTO userDTO, ClassDTO classDTO, UserDetailsExtended userDetailsExtended) {
        User authenticatedUser = userDetailsExtended.getUser();
        Student student;
        if (authenticatedUser.getRole() == Role.Operator)
            student = userRepository.findStudentByNameAndSchool(userDTO.getUserName(), authenticatedUser.getSchool());
        else student = userRepository.findStudentByName(userDTO.getUserName());
        if (student == null || student.getRole() != Role.Student) return Result.InvalidName;
        SchoolClass schoolClass = getSchoolClassByRole(classDTO, authenticatedUser, student.getSchool());
        if (schoolClass == null) return Result.InvalidClassName;
        userRepository.setClassForStudent(student.getId(), schoolClass.getId());
        return Result.Ok;
    }

    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
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
        if (userRepository.findUserByName(userName) != null) {
            return Result.UserIsExists;
        }
        return Result.Ok;
    }

    public User getUserByName(String name) {
        return userRepository.findUserByName(name);
    }

    public Teacher getTeacherByName(String name) {
        return userRepository.findTeacherByName(name);
    }

    public String getCurrentUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public List<String> getNamesOfTeachersByClassId(Long classId) {
        return userRepository.findNamesOfTeachersByClassId(classId);
    }

}
