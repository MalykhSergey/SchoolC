package general.services;

import general.entities.School;
import general.entities.SchoolClass;
import general.entities.Task;
import general.entities.Teacher;
import general.reposes.TaskRepos;
import general.utils.Result;
import general.utils.StringLengthConstants;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

class TaskServiceTest {
    TaskRepos taskRepos = Mockito.mock(TaskRepos.class);
    TaskService taskService = new TaskService(taskRepos);

    School school = new School("school");

    SchoolClass schoolClass = new SchoolClass("class", 1, school);
    SchoolClass otherClass = new SchoolClass("class", 1, school);
    Teacher teacher = new Teacher("Teacher", "password", school);
    StringBuilder nameBuilder = new StringBuilder();
    StringBuilder bodyBuilder = new StringBuilder();

    {
        schoolClass.setId(1L);
        otherClass.setId(2L);
        teacher.addSchoolClass(schoolClass);
    }

    @Test
    void createTask() {
        nameBuilder.setLength(StringLengthConstants.TaskName.getMinLength());
        bodyBuilder.setLength(StringLengthConstants.TaskBody.getMinLength());
        assertEquals(taskService.createTask(nameBuilder.toString(), bodyBuilder.toString(), "2077-12-12T23:00", schoolClass, teacher), Result.Ok);
        assertEquals(taskService.createTask(nameBuilder.toString(), bodyBuilder.toString(), "2077-Dec-12T23:00", schoolClass, teacher), Result.InvalidDate);
        assertEquals(taskService.createTask(nameBuilder.toString(), bodyBuilder.toString(), "2077-12-12T23:00", otherClass, teacher), Result.InvalidClassName);
        Mockito.when(taskRepos.findTaskBySchoolClassAndName(any(),any())).thenReturn(new Task());
        assertEquals(taskService.createTask(nameBuilder.toString(), bodyBuilder.toString(), "2077-12-12T23:00", schoolClass, teacher), Result.TaskIsExists);
    }

    @Test
    void validateTask() {
        nameBuilder.setLength(StringLengthConstants.TaskName.getMinLength());
        bodyBuilder.setLength(StringLengthConstants.TaskBody.getMinLength());
        Task rightTask = new Task(nameBuilder.toString(), bodyBuilder.toString(), schoolClass, teacher, new Timestamp(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(2)));
        nameBuilder.setLength(StringLengthConstants.TaskName.getMinLength() - 1);
        Task tooShortNameTask = new Task(nameBuilder.toString(), bodyBuilder.toString(), schoolClass, teacher, new Timestamp(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(2)));
        nameBuilder.setLength(StringLengthConstants.TaskName.getMinLength());
        bodyBuilder.setLength(StringLengthConstants.TaskBody.getMinLength() - 1);
        Task tooShortBodyTask = new Task(nameBuilder.toString(), bodyBuilder.toString(), schoolClass, teacher, new Timestamp(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(2)));
        bodyBuilder.setLength(StringLengthConstants.TaskBody.getMinLength());
        nameBuilder.setLength(StringLengthConstants.TaskName.getMaxLength() + 1);
        Task tooLongNameTask = new Task(nameBuilder.toString(), bodyBuilder.toString(), schoolClass, teacher, new Timestamp(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(2)));
        nameBuilder.setLength(StringLengthConstants.TaskName.getMinLength());
        bodyBuilder.setLength(StringLengthConstants.TaskBody.getMaxLength() + 1);
        Task tooLongBodyTask = new Task(nameBuilder.toString(), bodyBuilder.toString(), schoolClass, teacher, new Timestamp(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(2)));
        assertEquals(taskService.validateTask(rightTask), Result.Ok);
        assertEquals(taskService.validateTask(tooShortNameTask), Result.TooShortTaskName);
        assertEquals(taskService.validateTask(tooShortBodyTask), Result.TooShortTaskBody);
        assertEquals(taskService.validateTask(tooLongNameTask), Result.TooLongTaskName);
        assertEquals(taskService.validateTask(tooLongBodyTask), Result.TooLongTaskBody);
        rightTask.setTimeStamp(new Timestamp(System.currentTimeMillis()));
        assertEquals(taskService.validateTask(rightTask), Result.InvalidDate);
    }
}