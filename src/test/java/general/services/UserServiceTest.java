package general.services;

import general.controllers.api.dtos.UserDTO;
import general.entities.*;
import general.reposes.SchoolClassRepos;
import general.reposes.SchoolRepos;
import general.reposes.UserRepos;
import general.utils.Result;
import general.utils.UserDetailsExtended;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserServiceTest {
    UserRepos userRepos = Mockito.mock(UserRepos.class);
    SchoolRepos schoolRepos = Mockito.mock(SchoolRepos.class);
    SchoolClassRepos schoolClassRepos = Mockito.mock(SchoolClassRepos.class);
    PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
    UserService userService = new UserService(userRepos, schoolRepos, schoolClassRepos, passwordEncoder);

    School school = new School("schoolName");
    SchoolClass schoolClass = new SchoolClass("className", 1, school);

    {
        schoolClass.setId(1L);
    }

    UserDTO studentDTO = new UserDTO("Student", "123456", Role.Student,
            school.getName(), schoolClass.getName(), schoolClass.getClassNumber(), schoolClass.getId());
    Student student = new Student(studentDTO.getUserName(), studentDTO.getPassword(), school, schoolClass);
    UserDTO teacherDTO = new UserDTO("Student", "123456", Role.Teacher,
            school.getName(), null, null, null);
    User operator = new User("operator", "123456", school, Role.Operator);
    UserDetailsExtended operatorDetails = new UserDetailsExtended(operator);
    User admin = new User("admin", "123456", null, Role.Admin);
    UserDetailsExtended adminDetails = new UserDetailsExtended(admin);

    @Test
    void createUser() {
        assertEquals(userService.createUser(studentDTO, operatorDetails), Result.InvalidClassName);
        assertEquals(userService.createUser(teacherDTO, operatorDetails), Result.Ok);
        assertEquals(userService.createUser(studentDTO, adminDetails), Result.InvalidSchoolName);
        assertEquals(userService.createUser(teacherDTO, adminDetails), Result.InvalidSchoolName);
        Mockito.when(schoolRepos.findSchoolByName(school.getName())).thenReturn(school);
        Mockito.when(schoolClassRepos.findSchoolClassByNameAndClassNumberAndSchool(studentDTO.getClassName(), studentDTO.getClassNumber(), school))
                .thenReturn(schoolClass);
        Mockito.when(schoolClassRepos.findSchoolClassById(schoolClass.getId())).thenReturn(schoolClass);
        assertEquals(userService.createUser(studentDTO, operatorDetails), Result.Ok);
        assertEquals(userService.createUser(teacherDTO, operatorDetails), Result.Ok);
        assertEquals(userService.createUser(studentDTO, adminDetails), Result.Ok);
        assertEquals(userService.createUser(teacherDTO, adminDetails), Result.Ok);
    }

    @Test
    void setClassForStudent() {
        assertEquals(userService.setClassForStudent(studentDTO,operatorDetails),Result.InvalidName);
        assertEquals(userService.setClassForStudent(studentDTO,adminDetails),Result.InvalidName);
        Mockito.when(userRepos.findUserByName(studentDTO.getUserName())).thenReturn(student);
        assertEquals(userService.setClassForStudent(studentDTO,adminDetails),Result.InvalidClassName);
        Mockito.when(userRepos.findUserByNameAndSchool(studentDTO.getUserName(), school)).thenReturn(student);
        assertEquals(userService.setClassForStudent(studentDTO,operatorDetails),Result.InvalidClassName);
        Mockito.when(schoolClassRepos.findSchoolClassByNameAndClassNumberAndSchool(studentDTO.getClassName(), studentDTO.getClassNumber(), school))
                .thenReturn(schoolClass);
        Mockito.when(schoolClassRepos.findSchoolClassById(schoolClass.getId())).thenReturn(schoolClass);
    }

    @Test
    void validateUserNameAndPassword() {
        assertEquals(userService.validateUserNameAndPassword(studentDTO.getUserName(), studentDTO.getPassword()), Result.Ok);
        studentDTO.setUserName("");
        assertEquals(userService.validateUserNameAndPassword(studentDTO.getUserName(), studentDTO.getPassword()), Result.TooShortName);
        studentDTO.setUserName(" 1234567890 1234567890 1234567890 1234567890 1234567890 ");
        assertEquals(userService.validateUserNameAndPassword(studentDTO.getUserName(), studentDTO.getPassword()), Result.TooLongName);
        studentDTO.setUserName(null);
        assertEquals(userService.validateUserNameAndPassword(studentDTO.getUserName(), studentDTO.getPassword()), Result.NameIsNull);
        studentDTO.setUserName("Student");
        studentDTO.setPassword("");
        assertEquals(userService.validateUserNameAndPassword(studentDTO.getUserName(), studentDTO.getPassword()), Result.TooShortPassword);
        studentDTO.setPassword("1234567890 1234567890 1234567890 1234567890 1234567890");
        assertEquals(userService.validateUserNameAndPassword(studentDTO.getUserName(), studentDTO.getPassword()), Result.TooLongPassword);
        studentDTO.setPassword(null);
        assertEquals(userService.validateUserNameAndPassword(studentDTO.getUserName(), studentDTO.getPassword()), Result.PasswordIsNull);
        studentDTO.setPassword("12345");
        Mockito.when(userRepos.findUserByName(studentDTO.getUserName())).thenReturn(operator);
        assertEquals(userService.validateUserNameAndPassword(studentDTO.getUserName(), studentDTO.getPassword()), Result.InvalidName);
    }
}