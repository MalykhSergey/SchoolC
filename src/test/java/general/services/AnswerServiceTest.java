package general.services;

import general.entities.*;
import general.reposes.AnswerRepos;
import general.utils.Result;
import general.utils.StringLengthConstants;
import general.utils.UserDetailsExtended;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AnswerServiceTest {
    AnswerRepos answerRepos = Mockito.mock(AnswerRepos.class);
    AnswerService answerService = new AnswerService(answerRepos);
    School school = new School("school");
    SchoolClass schoolClass = new SchoolClass("class", 1, school);
    Student student = new Student("student", "password", school, schoolClass);
    SchoolClass otherClass = new SchoolClass("class", 1, school);
    Teacher teacher = new Teacher("Teacher", "password", school);
    Teacher otherTeacher = new Teacher("Other Teacher", "password", school);
    UserDetailsExtended teacherDetailsExtended = new UserDetailsExtended(teacher);
    UserDetailsExtended otherTeacherDetailsExtended = new UserDetailsExtended(otherTeacher);
    StringBuilder stringBuilder = new StringBuilder();

    {
        schoolClass.setId(1L);
        otherClass.setId(2L);
        teacher.setId(1L);
        otherTeacher.setId(2L);
        teacher.addSchoolClass(schoolClass);
        schoolClass.addStudent(student);
        stringBuilder.setLength(StringLengthConstants.TaskBody.getMinLength());
    }

    @Test
    void createAnswer() {
        stringBuilder.setLength(StringLengthConstants.TaskBody.getMinLength());
        Task task = new Task(stringBuilder.toString(), stringBuilder.toString(), schoolClass, teacher,
                new Timestamp(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)));
        stringBuilder.setLength(StringLengthConstants.AnswerBody.getMinLength());
        assertEquals(answerService.createAnswer(stringBuilder.toString(), task, student), Result.Ok);
        student.setSchoolClass(otherClass);
        assertEquals(answerService.createAnswer(stringBuilder.toString(), task, student), Result.InvalidClassName);
        student.setSchoolClass(schoolClass);
        stringBuilder.setLength(StringLengthConstants.AnswerBody.getMinLength() - 1);
        assertEquals(answerService.createAnswer(stringBuilder.toString(), task, student), Result.TooShortAnswerBody);
        stringBuilder.setLength(StringLengthConstants.AnswerBody.getMaxLength() + 1);
        assertEquals(answerService.createAnswer(stringBuilder.toString(), task, student), Result.TooLongAnswerBody);
        stringBuilder.setLength(StringLengthConstants.AnswerBody.getMaxLength() - 1);
        assertEquals(answerService.createAnswer(stringBuilder.toString(), task, student), Result.Ok);
    }

    @Test
    void checkAnswer() {
        stringBuilder.setLength(StringLengthConstants.TaskBody.getMinLength());
        Task task = new Task(stringBuilder.toString(), stringBuilder.toString(), schoolClass, teacher,
                new Timestamp(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)));
        stringBuilder.setLength(StringLengthConstants.AnswerBody.getMinLength());
        Answer answer = new Answer(student, task, stringBuilder.toString());
        stringBuilder.setLength(StringLengthConstants.Comment.getMinLength());
        assertEquals(answerService.checkAnswer(answer, stringBuilder.toString(), 5, teacherDetailsExtended), Result.Ok);
        assertEquals(answerService.checkAnswer(answer, stringBuilder.toString(), 2, teacherDetailsExtended), Result.Ok);
        assertEquals(answerService.checkAnswer(null, stringBuilder.toString(), 5, teacherDetailsExtended), Result.AnswerIsNotExists);
        assertEquals(answerService.checkAnswer(answer, stringBuilder.toString(), 1, teacherDetailsExtended), Result.InvalidRating);
        assertEquals(answerService.checkAnswer(answer, stringBuilder.toString(), 6, teacherDetailsExtended), Result.InvalidRating);
        assertEquals(answerService.checkAnswer(answer, stringBuilder.toString(), 2, otherTeacherDetailsExtended), Result.InvalidTeacher);
    }
}