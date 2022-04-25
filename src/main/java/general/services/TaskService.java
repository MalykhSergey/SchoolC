package general.services;

import general.utils.CheckDataBoolAnswer;
import general.entities.SchoolClass;
import general.entities.Task;
import general.entities.Teacher;
import general.reposes.SchoolClassRepos;
import general.reposes.TaskRepos;
import general.reposes.UserRepos;
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

    private TaskRepos taskRepos;
    private SchoolClassRepos schoolClassRepos;
    private UserRepos userRepos;

    @Autowired
    public TaskService(TaskRepos taskRepos, SchoolClassRepos schoolClassRepos, UserRepos userRepos) {
        this.taskRepos = taskRepos;
        this.schoolClassRepos = schoolClassRepos;
        this.userRepos = userRepos;
    }

    @Transactional
    public void createTask(String name, String body, String dateString, SchoolClass schoolClass) {
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
        task = new Task(name, body, schoolClass, timestamp);
        taskRepos.save(task);
        schoolClass.addTask(task);
        schoolClassRepos.save(schoolClass);
    }

    public CheckDataBoolAnswer checkInputData(String name, String body, String nameOfSchoolClass, String dateString,
                                               Teacher teacher, SchoolClass schoolClass) {
        if (name == null | body == null | nameOfSchoolClass == null | dateString == null) {
            return new CheckDataBoolAnswer(false, "Введите все значения");
        }
        boolean bool = false;
        for (SchoolClass checkSchoolClass : teacher.getSchoolClassSet()) {
            if (checkSchoolClass.fastEqualsById(schoolClass)) {
                bool = true;
            }
        }
        if (!bool) {
            return new CheckDataBoolAnswer(false, "Неверный класс");
        }
        if (name.length() < 5 | body.length() < 25) {
            return new CheckDataBoolAnswer(false, "Введите более полное описание или название задания");
        }
        if (name.length() > 40 | body.length() > 2000) {
            return new CheckDataBoolAnswer(false, "Введите более короткое описание или название задания");
        }
        return new CheckDataBoolAnswer(true, null);
    }

    public List<Task> getTaskByClass(Long schoolClassId) {
        return taskRepos.findTasksBySchoolClassId(schoolClassId);
    }

}
