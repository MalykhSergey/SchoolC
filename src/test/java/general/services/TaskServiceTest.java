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
        assertEquals(taskService.createTask(nameBuilder.toString(), bodyBuilder.toString(), "2007-12-12T23:00", schoolClass, teacher), Result.InvalidDate);
        assertEquals(taskService.createTask(nameBuilder.toString(), bodyBuilder.toString(), "2077-12-12T23:00", otherClass, teacher), Result.InvalidClassName);
        Mockito.when(taskRepos.findTaskBySchoolClassAndName(any(), any())).thenReturn(new Task());
        assertEquals(taskService.createTask(nameBuilder.toString(), bodyBuilder.toString(), "2077-12-12T23:00", schoolClass, teacher), Result.TaskIsExists);
        nameBuilder.setLength(StringLengthConstants.TaskName.getMinLength() - 1);
        assertEquals(taskService.createTask(nameBuilder.toString(), bodyBuilder.toString(), "2077-12-12T23:00", schoolClass, teacher), Result.TooShortTaskName);
        nameBuilder.setLength(StringLengthConstants.TaskName.getMaxLength() + 1);
        assertEquals(taskService.createTask(nameBuilder.toString(), bodyBuilder.toString(), "2077-12-12T23:00", schoolClass, teacher), Result.TooLongTaskName);
        nameBuilder.setLength(StringLengthConstants.TaskName.getMinLength());
        bodyBuilder.setLength(StringLengthConstants.TaskBody.getMinLength() - 1);
        assertEquals(taskService.createTask(nameBuilder.toString(), bodyBuilder.toString(), "2077-12-12T23:00", schoolClass, teacher), Result.TooShortTaskBody);
        bodyBuilder.setLength(StringLengthConstants.TaskBody.getMaxLength() + 1);
        assertEquals(taskService.createTask(nameBuilder.toString(), bodyBuilder.toString(), "2077-12-12T23:00", schoolClass, teacher), Result.TooLongTaskBody);
    }
}