package general.service;

import general.entity.SchoolClass;
import general.entity.Task;
import general.entity.Teacher;
import general.repository.TaskRepository;
import general.util.Result;
import general.util.StringLengthConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    public Result createTask(String name, String body, String dateString, SchoolClass schoolClass, Teacher teacher) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date parsedDate = simpleDateFormat.parse(dateString);
            Timestamp timestamp = new Timestamp(parsedDate.getTime());
            Task task = new Task(name, body, schoolClass, teacher, timestamp);
            Result result = validateTask(task);
            if (result != Result.Ok) return result;
            taskRepository.save(task);
        } catch (ParseException e) {
            return Result.InvalidDate;
        }
        return Result.Ok;
    }

    public Task getTaskById(Long id) {
        return taskRepository.findTaskById(id);
    }

    public List<Task> getTasksByClass(SchoolClass schoolClass) {
        return taskRepository.findTasksBySchoolClassOrderByTimeStamp(schoolClass);
    }

    public List<Task> getTasksByClassAndTeacher(SchoolClass schoolClass, Teacher teacher) {
        return taskRepository.findAllByTeacherAndSchoolClassOrderByTimeStamp(teacher, schoolClass);
    }

    private Result validateTask(Task task) {
        if (task.getSchoolClass() != null && isClassInTeacherSet(task)) {
            if (task.getName().length() < StringLengthConstants.TaskName.getMinLength()) return Result.TooShortTaskName;
            if (task.getName().length() > StringLengthConstants.TaskName.getMaxLength()) return Result.TooLongTaskName;
            if (task.getBody().length() < StringLengthConstants.TaskBody.getMinLength()) return Result.TooShortTaskBody;
            if (task.getBody().length() > StringLengthConstants.TaskBody.getMaxLength()) return Result.TooLongTaskBody;
            if (task.getTimeStamp().getTime() < (System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)))
                return Result.InvalidDate;
            if (taskRepository.findTaskBySchoolClassAndName(task.getSchoolClass(),task.getName())!=null) return Result.TaskIsExists;
            return Result.Ok;
        } else return Result.InvalidClassName;
    }

    private static boolean isClassInTeacherSet(Task task) {
        return task.getTeacher().getSchoolClassSet().stream().anyMatch(schoolClass -> schoolClass.fastEqualsById(task.getSchoolClass()));
    }
}
