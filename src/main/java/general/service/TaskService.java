package general.service;

import general.controller.dto.AnswerDTO;
import general.controller.dto.StudentsTasksAndAnswers;
import general.controller.dto.TaskDTO;
import general.entity.*;
import general.repository.TaskRepository;
import general.util.Result;
import general.util.StringLengthConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final AnswerService answerService;

    @Autowired
    public TaskService(TaskRepository taskRepository, AnswerService answerService) {
        this.taskRepository = taskRepository;
        this.answerService = answerService;
    }

    private static boolean isClassInTeacherSet(Task task) {
        return task.getTeacher().getSchoolClassSet().stream().anyMatch(schoolClass -> schoolClass.fastEqualsById(task.getSchoolClass()));
    }

    private static List<AnswerDTO> getAnswerDTOS(List<Answer> allAnswers) {
        List<AnswerDTO> answerDTOS = new ArrayList<>();
        allAnswers.forEach(answer -> answerDTOS.add(new AnswerDTO(answer)));
        return answerDTOS;
    }

    private static List<TaskDTO> getTaskDTOS(List<Task> tasks) {
        List<TaskDTO> taskDTOS = new ArrayList<>();
        tasks.forEach(task -> taskDTOS.add(new TaskDTO(task)));
        return taskDTOS;
    }

    public StudentsTasksAndAnswers getTasksAndAnswersByStudent(Student student) {
        List<Task> actualTasks = taskRepository.findOnlyNonAnsweredActualTasksByClassId(student.getSchoolClass().getId());
        List<Task> oldTasks = taskRepository.findOnlyNonAnsweredOldTasksByClassId(student.getSchoolClass().getId());
        List<Answer> allAnswers = answerService.getAnswersByStudent(student);
        List<TaskDTO> actualTaskDTOs = getTaskDTOS(actualTasks);
        List<TaskDTO> oldTaskDTOs = getTaskDTOS(oldTasks);
        List<AnswerDTO> answerDTOS = getAnswerDTOS(allAnswers);
        return new StudentsTasksAndAnswers(actualTaskDTOs, oldTaskDTOs, answerDTOS);
    }

    public StudentsTasksAndAnswers getTasksAndAnswersByStudentAndTeacher(Student student, Teacher teacher) {
        if (teacher != null) {
            List<Task> actualTasks = taskRepository.findOnlyNonAnsweredActualTasksByClassIdAndTeacherId(student.getSchoolClass().getId(), teacher.getId());
            List<Task> oldTasks = taskRepository.findOnlyNonAnsweredOldTasksByClassIdAndTeacherId(student.getSchoolClass().getId(), teacher.getId());
            List<Answer> allAnswers = answerService.getAnswersByStudentAndTeacher(student,teacher);
            List<TaskDTO> actualTaskDTOs = getTaskDTOS(actualTasks);
            List<TaskDTO> oldTaskDTOs = getTaskDTOS(oldTasks);
            List<AnswerDTO> answerDTOS = getAnswerDTOS(allAnswers);
            return new StudentsTasksAndAnswers(actualTaskDTOs, oldTaskDTOs, answerDTOS);
        } else return new StudentsTasksAndAnswers(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
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
            if (taskRepository.findTaskBySchoolClassAndName(task.getSchoolClass(), task.getName()) != null)
                return Result.TaskIsExists;
            return Result.Ok;
        } else return Result.InvalidClassName;
    }
}
