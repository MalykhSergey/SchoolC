package general.service;

import general.controller.dto.ClassDTO;
import general.controller.dto.SchoolDTO;
import general.controller.dto.UserDTO;
import general.entity.*;
import general.repository.SchoolClassRepository;
import general.repository.SchoolRepository;
import general.repository.UserRepository;
import general.util.Result;
import general.util.UserDetailsExtended;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

class UserServiceTest {
    UserRepository userRepository;
    SchoolRepository schoolRepository;
    SchoolClassRepository schoolClassRepository;
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
        userRepository = Mockito.mock(UserRepository.class);
        schoolRepository = Mockito.mock(SchoolRepository.class);
        schoolClassRepository = Mockito.mock(SchoolClassRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserService(userRepository, schoolRepository, schoolClassRepository, passwordEncoder);
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
        Mockito.when(schoolRepository.findSchoolByName(school.getName())).thenReturn(school);
        Mockito.when(schoolClassRepository.findSchoolClassByNameAndClassNumberAndSchool(classDTO.getClassName(), classDTO.getClassNumber(), school)).thenReturn(schoolClass);
        Mockito.when(schoolClassRepository.findSchoolClassById(schoolClass.getId())).thenReturn(schoolClass);
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
        Mockito.when(userRepository.findUserByName(anyString())).thenReturn(student);
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
        Mockito.when(userRepository.findStudentByName(studentDTO.getUserName())).thenReturn(student);
        assertEquals(userService.setClassForStudent(studentDTO, classDTO, adminDetails), Result.InvalidClassName);
        Mockito.when(userRepository.findStudentByNameAndSchool(studentDTO.getUserName(), school)).thenReturn(student);
        assertEquals(userService.setClassForStudent(studentDTO, classDTO, operatorDetails), Result.InvalidClassName);
        Mockito.when(schoolClassRepository.findSchoolClassById(classDTO.getClassId())).thenReturn(schoolClass);
        Mockito.when(schoolClassRepository.findSchoolClassByNameAndClassNumberAndSchool(classDTO.getClassName(), classDTO.getClassNumber(), school)).thenReturn(schoolClass);
        assertEquals(userService.setClassForStudent(studentDTO, classDTO, operatorDetails), Result.Ok);
    }
}