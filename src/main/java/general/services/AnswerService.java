package general.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import general.entities.Answer;
import general.entities.SchoolClass;
import general.entities.Student;
import general.entities.Task;
import general.entities.TaskStatusOfStudent;
import general.entities.Teacher;
import general.reposes.AnswerRepos;
import general.reposes.TaskRepos;
import general.reposes.TaskStatusOfStudentRepos;
import general.reposes.UserRepos;

@Service
public class AnswerService {
    @Autowired
    private TaskRepos taskRepos;
    @Autowired
    private UserRepos userRepos;
    @Autowired
    private AnswerRepos answerRepos;
    @Autowired
    private TaskStatusOfStudentRepos taskStatusOfStudentRepos;
    @Autowired
    UserService userService;
    
    @Transactional
    public String addAnswer(String id, String body, Model model) throws IOException {
        Task task = taskRepos.findTaskById(Long.parseLong(id));
        Student student = (Student) (userRepos.findUserByName(userService.getUserName())); 
        if (checkTaskStatus(task, student)) {
            createAnswer(body, task, student);
            setTaskStatus(task, student);
            model.addAttribute("task", task);
            model.addAttribute("completed", "Ответ успешно добавлен");
            return "addanswer";
        }
        return "redirect:/";
    }
    @Transactional
    private void createAnswer(String body, Task task, Student student) throws IOException {
        Answer answer = new Answer(student, task);
        answer.setBody(body);
        student.addAnswer(answer);
        task.addAnswer(answer);
        answerRepos.save(answer);
        taskRepos.save(task);
        userRepos.save(student);
    }

    public Boolean checkTaskStatus(Task task, Student student) {
        Boolean bool = false;
        for (Task studentTask : student.getSchoolClass().getTasks()) {
            if (studentTask.fastEqualsById(task)) {
                if (taskStatusOfStudentRepos.findTaskStatusOfStudentByStudentAndTask(student, task).getStatus().equals("Решено!")) {
                    bool = false;
                    break;
                }
                bool = true;
                break;
            }
        }
        return bool;
    }

    public void setTaskStatus(Task task, Student student) {
        TaskStatusOfStudent taskStatusOfStudent = taskStatusOfStudentRepos.findTaskStatusOfStudentByStudentAndTask(student, task);
        taskStatusOfStudent.setStatus("Решено!");
        taskStatusOfStudentRepos.save(taskStatusOfStudent);
    }

    public String checkAnswer(String answerId, String rating, String comment) {
        if (Byte.parseByte(rating) < 6 && Byte.parseByte(rating) > 1) {
            Teacher teacher = (Teacher) (userRepos.findUserByName(userService.getUserName()));
            Answer answer = answerRepos.findAnswerById(Long.parseLong(answerId));
            if (isStudentInClassSetOfTeacher(teacher, answer) == true) {
                updateAnswer(rating, comment, answer);
                return "redirect:/answersOfTask/?taskId=" + answer.getTask().getId() + "&classId=" + answer.getTask().getSchoolClass().getId();
            }
        }
        return "redirect:/";
    }
    @Transactional
    private void updateAnswer(String rating, String comment, Answer answer) {
        answer.setRating(Byte.parseByte(rating));
        TaskStatusOfStudent taskStatusOfStudent = taskStatusOfStudentRepos.findTaskStatusOfStudentByStudentAndTask
                (answer.getStudent(), answer.getTask());
        taskStatusOfStudent.setMark(Integer.parseInt(rating));
        taskStatusOfStudent.setComment(comment);
        taskStatusOfStudentRepos.save(taskStatusOfStudent);
        answerRepos.save(answer);
    }
    private Boolean isStudentInClassSetOfTeacher(Teacher teacher, Answer answer) {
        Boolean bool = false;
        for (SchoolClass sc : teacher.getSchoolClassSet()) {
            for (Student st : sc.getStudents()) {
                if (st.fastEqualsById(answer.getStudent())) {
                    bool = true;
                    break;
                }
            }
            break;
        }
        return bool;
    }
 
    public AnswerService(TaskRepos taskRepos, UserRepos userRepos, AnswerRepos answerRepos,
            TaskStatusOfStudentRepos taskStatusOfStudentRepos) {
        this.taskRepos = taskRepos;
        this.userRepos = userRepos;
        this.answerRepos = answerRepos;
        this.taskStatusOfStudentRepos = taskStatusOfStudentRepos;
    }

    public AnswerService() {
    }
    
}
