package general.services;

import general.controllers.dto.ClassDTO;
import general.controllers.dto.SchoolDTO;
import general.controllers.dto.UserDTO;
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
import static org.mockito.ArgumentMatchers.anyString;

class UserServiceTest {
    UserRepos userRepos;
    SchoolRepos schoolRepos;
    SchoolClassRepos schoolClassRepos;
    PasswordEncoder passwordEncoder;
    UserService userService;
    School school;
    SchoolDTO schoolDTO;
    SchoolClass schoolClass;
    ClassDTO classDTO;
    UserDTO studentDTO;
    Student student;
    UserDTO teacherDTO;
    User operator;
    UserDetailsExtended operatorDetails;
    User admin;
    UserDetailsExtended adminDetails;

    UserServiceTest() {
        userRepos = Mockito.mock(UserRepos.class);
        schoolRepos = Mockito.mock(SchoolRepos.class);
        schoolClassRepos = Mockito.mock(SchoolClassRepos.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserService(userRepos, schoolRepos, schoolClassRepos, passwordEncoder);
        school = new School("schoolName");
        schoolDTO = new SchoolDTO(school.getName());
        schoolClass = new SchoolClass("className", 1, school);
        classDTO = new ClassDTO(schoolClass.getName(), schoolClass.getClassNumber(), null);
        studentDTO = new UserDTO("Student", "123456", Role.Student);
        student = new Student(studentDTO.getUserName(), studentDTO.getPassword(), school, schoolClass);
        teacherDTO = new UserDTO("Student", "123456", Role.Teacher);
        operator = new User("operator", "123456", school, Role.Operator);
        operatorDetails = new UserDetailsExtended(operator);
        admin = new User("admin", "123456", null, Role.Admin);
        adminDetails = new UserDetailsExtended(admin);
        schoolClass.setId(1L);
        classDTO.setClassId(schoolClass.getId());
    }

    @Test
    void createUser() {
        assertEquals(userService.createUser(studentDTO, classDTO, null, operatorDetails), Result.InvalidClassName);
        assertEquals(userService.createUser(teacherDTO, null, null, operatorDetails), Result.Ok);
        assertEquals(userService.createUser(studentDTO, classDTO, schoolDTO, adminDetails), Result.InvalidSchoolName);
        assertEquals(userService.createUser(teacherDTO, null, schoolDTO, adminDetails), Result.InvalidSchoolName);
        Mockito.when(schoolRepos.findSchoolByName(school.getName())).thenReturn(school);
        Mockito.when(schoolClassRepos.findSchoolClassByNameAndClassNumberAndSchool(classDTO.getClassName(), classDTO.getClassNumber(), school)).thenReturn(schoolClass);
        Mockito.when(schoolClassRepos.findSchoolClassById(schoolClass.getId())).thenReturn(schoolClass);
        assertEquals(userService.createUser(studentDTO, classDTO, null, operatorDetails), Result.Ok);
        assertEquals(userService.createUser(teacherDTO, null, null, operatorDetails), Result.Ok);
        assertEquals(userService.createUser(studentDTO, classDTO, schoolDTO, adminDetails), Result.Ok);
        assertEquals(userService.createUser(teacherDTO, null, schoolDTO, adminDetails), Result.Ok);
        assertEquals(userService.createUser(studentDTO, classDTO, schoolDTO, operatorDetails), Result.Ok);
        studentDTO.setUserName("");
        assertEquals(userService.createUser(studentDTO, classDTO, schoolDTO, operatorDetails), Result.TooShortName);
        studentDTO.setUserName(" 1234567890 1234567890 1234567890 1234567890 1234567890 ");
        assertEquals(userService.createUser(studentDTO, classDTO, schoolDTO, operatorDetails), Result.TooLongName);
        studentDTO.setUserName(null);
        assertEquals(userService.createUser(studentDTO, classDTO, schoolDTO, operatorDetails), Result.NameIsNull);
        studentDTO.setUserName("Student");
        studentDTO.setPassword("");
        assertEquals(userService.createUser(studentDTO, classDTO, schoolDTO, operatorDetails), Result.TooShortPassword);
        studentDTO.setPassword("1234567890 1234567890 1234567890 1234567890 1234567890");
        assertEquals(userService.createUser(studentDTO, classDTO, schoolDTO, operatorDetails), Result.TooLongPassword);
        studentDTO.setPassword(null);
        assertEquals(userService.createUser(studentDTO, classDTO, schoolDTO, operatorDetails), Result.PasswordIsNull);
        studentDTO.setPassword("12345");
        Mockito.when(userRepos.findUserByName(anyString())).thenReturn(student);
        assertEquals(userService.createUser(studentDTO, classDTO, schoolDTO, operatorDetails), Result.UserIsExists);
        assertEquals(userService.createUser(studentDTO, classDTO, null, operatorDetails), Result.UserIsExists);
        assertEquals(userService.createUser(teacherDTO, null, null, operatorDetails), Result.UserIsExists);
        assertEquals(userService.createUser(studentDTO, classDTO, schoolDTO, adminDetails), Result.UserIsExists);
        assertEquals(userService.createUser(teacherDTO, null, schoolDTO, adminDetails), Result.UserIsExists);
    }

    @Test
    void setClassForStudent() {
        assertEquals(userService.setClassForStudent(studentDTO, classDTO, operatorDetails), Result.InvalidName);
        assertEquals(userService.setClassForStudent(studentDTO, classDTO, adminDetails), Result.InvalidName);
        Mockito.when(userRepos.findStudentByName(studentDTO.getUserName())).thenReturn(student);
        assertEquals(userService.setClassForStudent(studentDTO, classDTO, adminDetails), Result.InvalidClassName);
        Mockito.when(userRepos.findStudentByNameAndSchool(studentDTO.getUserName(), school)).thenReturn(student);
        assertEquals(userService.setClassForStudent(studentDTO, classDTO, operatorDetails), Result.InvalidClassName);
        Mockito.when(schoolClassRepos.findSchoolClassById(classDTO.getClassId())).thenReturn(schoolClass);
        Mockito.when(schoolClassRepos.findSchoolClassByNameAndClassNumberAndSchool(classDTO.getClassName(), classDTO.getClassNumber(), school)).thenReturn(schoolClass);
        assertEquals(userService.setClassForStudent(studentDTO, classDTO, operatorDetails), Result.Ok);
    }
}