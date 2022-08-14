package general.services;

import general.entities.SchoolClass;
import general.entities.Task;
import general.entities.Teacher;
import general.reposes.TaskRepos;
import general.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepos taskRepos;

    @Autowired
    public TaskService(TaskRepos taskRepos) {
        this.taskRepos = taskRepos;
    }

    @Transactional
    public void createTask(String name, String body, String dateString, SchoolClass schoolClass, Teacher teacher) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Timestamp timestamp;
        try {
            Date parsedDate = simpleDateFormat.parse(dateString);
            timestamp = new Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        Task task;
        task = new Task(name, body, schoolClass, teacher, timestamp);
        taskRepos.save(task);
    }

    public Task getTaskById(Long id) {
        return taskRepos.findTaskById(id);
    }

    public List<Task> getTasksByClass(SchoolClass schoolClass) {
        return taskRepos.findTasksBySchoolClassOrderByTimeStamp(schoolClass);
    }

    public List<Task> getTasksByClassAndTeacher(SchoolClass schoolClass, Teacher teacher) {
        return taskRepos.findAllByTeacherAndSchoolClassOrderByTimeStamp(teacher, schoolClass);
    }

    public Result<String> checkInputData(String name, String body, String dateString,
                                 Teacher teacher, SchoolClass schoolClass) {
        if (name == null || body == null || dateString == null) {
            return new Result<>(false, "Введите все значения");
        }
        boolean bool = false;
        for (SchoolClass checkSchoolClass : teacher.getSchoolClassSet()) {
            if (checkSchoolClass.fastEqualsById(schoolClass)) {
                bool = true;
            }
        }
        if (!bool) {
            return new Result<>(false, "Неверный класс");
        }
        if (name.length() < 5 || body.length() < 25) {
            return new Result<>(false, "Введите более полное описание или название задания");
        }
        if (name.length() > 80 || body.length() > 2000) {
            return new Result<>(false, "Введите более короткое описание или название задания");
        }
        return new Result<>(true, null);
    }

}
