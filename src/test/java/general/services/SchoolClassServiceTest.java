package general.services;

import general.entities.*;
import general.reposes.SchoolClassRepos;
import general.utils.Result;
import general.utils.StringLengthConstants;
import general.utils.UserDetailsExtended;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SchoolClassServiceTest {

    private final SchoolClassRepos schoolClassRepos;
    private final StringBuilder stringBuilder;
    private final SchoolClassService schoolClassService;
    private final School school;
    private final School otherSchool;
    private final SchoolClass schoolClass;

    private final User operator;
    private final UserDetailsExtended operatorDetails;
    private final User admin;
    private final UserDetailsExtended adminDetails;
    private final Teacher teacher;
    private final Teacher otherTeacher;

    public SchoolClassServiceTest() {
        schoolClassRepos = Mockito.mock(SchoolClassRepos.class);
        schoolClassService = new SchoolClassService(schoolClassRepos);
        school = new School("school");
        school.setId(1L);
        otherSchool = new School("otherSchool");
        otherSchool.setId(2L);
        schoolClass = new SchoolClass("className", 1, school);
        schoolClass.setId(1L);
        stringBuilder = new StringBuilder();
        operator = new User("operator", "123456", school, Role.Operator);
        operatorDetails = new UserDetailsExtended(operator);
        admin = new User("admin", "123456", null, Role.Admin);
        adminDetails = new UserDetailsExtended(admin);
        teacher = new Teacher("Teacher", "password", school);
        otherTeacher = new Teacher("Other Teacher", "password", otherSchool);
    }

    @Test
    void createSchoolClass() {
        stringBuilder.setLength(StringLengthConstants.ClassName.getMinLength());
        assertEquals(schoolClassService.createSchoolClass(stringBuilder.toString(), 1, school, operatorDetails), Result.Ok);
        assertEquals(schoolClassService.createSchoolClass(stringBuilder.toString(), 0, school, operatorDetails), Result.InvalidClassNumber);
        assertEquals(schoolClassService.createSchoolClass(stringBuilder.toString(), 110, school, operatorDetails), Result.InvalidClassNumber);
        stringBuilder.setLength(StringLengthConstants.ClassName.getMaxLength());
        assertEquals(schoolClassService.createSchoolClass(stringBuilder.toString(), 1, school, adminDetails), Result.Ok);
        assertEquals(schoolClassService.createSchoolClass(stringBuilder.toString(), 1, null, adminDetails), Result.InvalidSchoolName);
        stringBuilder.setLength(StringLengthConstants.ClassName.getMinLength() - 1);
        assertEquals(schoolClassService.createSchoolClass(stringBuilder.toString(), 1, school, adminDetails), Result.TooShortClassName);
        stringBuilder.setLength(StringLengthConstants.ClassName.getMaxLength() + 1);
        assertEquals(schoolClassService.createSchoolClass(stringBuilder.toString(), 1, school, adminDetails), Result.TooLongClassName);
        Mockito.when(schoolClassRepos.findSchoolClassByNameAndClassNumberAndSchool(schoolClass.getName(), schoolClass.getClassNumber(), school)).thenReturn(schoolClass);
        assertEquals(schoolClassService.createSchoolClass("className", 1, school, adminDetails), Result.ClassIsExists);
    }

    @Test
    void addClassForTeacher() {
        assertEquals(schoolClassService.addClassForTeacher(teacher, schoolClass), Result.Ok);
        assertEquals(schoolClassService.addClassForTeacher(otherTeacher, schoolClass), Result.InvalidName);
        assertEquals(schoolClassService.addClassForTeacher(null, schoolClass), Result.InvalidName);
        assertEquals(schoolClassService.addClassForTeacher(teacher, null), Result.InvalidClassName);
        teacher.addSchoolClass(schoolClass);
        assertEquals(schoolClassService.addClassForTeacher(teacher, schoolClass), Result.TeacherIsLinked);
    }
}