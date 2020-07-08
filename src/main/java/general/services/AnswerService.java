package general.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
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
    TaskRepos taskRepos;
    @Autowired
    UserRepos userRepos;
    @Autowired
    AnswerRepos answerRepos;
    @Autowired
    TaskStatusOfStudentRepos taskStatusOfStudentRepos;

    public String addAnswer(String id, String body, Model model) throws IOException {
        Task task = taskRepos.findTaskById(Long.parseLong(id));
        Student student = (Student) (userRepos.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName()));
        Boolean bool;
        bool = checkAndChangeTaskStatus(task, student, false);
        if (bool) {
            createAnswer(body, task, student);
            model.addAttribute("task", task);
            model.addAttribute("completed", "Ответ успешно добавлен");
            return "addanswer";
        }
        return "redirect:/";
    }

    private void createAnswer(String body, Task task, Student student) throws IOException {
        Answer answer = new Answer(student, task);
        answer.setBody(body);
        student.addAnswer(answer);
        task.addAnswer(answer);
        answerRepos.save(answer);
        taskRepos.save(task);
        userRepos.save(student);
    }

    private Boolean checkAndChangeTaskStatus(Task task, Student student, Boolean bool) {
        for (Task studentTask : student.getSchoolClass().getTasks()) {
            if (studentTask.getName().equals(task.getName())) {
                if (taskStatusOfStudentRepos.findTaskStatusOfStudentByStudentAndTask(student, task).getStatus().equals("Решено!")) {
                    bool = false;
                    break;
                }
                taskStatusOfStudentRepos.findTaskStatusOfStudentByStudentAndTask(student, task).setStatus("Решено!");
                bool = true;
                break;
            }
        }
        return bool;
    }

    public String checkAnswer(String answerId, String rating, String comment) {
        if (Byte.parseByte(rating) < 6 && Byte.parseByte(rating) > 1) {
            Teacher teacher = (Teacher) (userRepos.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName()));
            Answer answer = answerRepos.findAnswerById(Long.parseLong(answerId));
            Boolean bool;
            bool = isStudentInClassSetOfTeacher(teacher, answer, false);
            if (bool == true) {
                answer.setRating(Byte.parseByte(rating));
                TaskStatusOfStudent taskStatusOfStudent = taskStatusOfStudentRepos.findTaskStatusOfStudentByStudentAndTask
                        (answer.getStudent(), answer.getTask());
                taskStatusOfStudent.setMark(Integer.parseInt(rating));
                taskStatusOfStudent.setComment(comment);
                taskStatusOfStudentRepos.save(taskStatusOfStudent);
                answerRepos.save(answer);
                return "redirect:/answersOfTask/?taskId=" + answer.getTask().getId() + "&classId=" + answer.getTask().getSchoolClass().getId();
            }
        }
        return "redirect:/";
    }
    private Boolean isStudentInClassSetOfTeacher(Teacher teacher, Answer answer, Boolean bool) {
        for (SchoolClass sc : teacher.getSchoolClassSet()) {
            for (Student st : sc.getStudents()) {
                if (st.getName().equals(answer.getStudent().getName())) {
                    bool = true;
                    break;
                }
            }
            break;
        }
        return bool;
    }
}
