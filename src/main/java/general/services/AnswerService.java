package general.services;

import general.entities.*;
import general.reposes.AnswerRepos;
import general.reposes.TaskRepos;
import general.reposes.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
public class AnswerService {
    private TaskRepos taskRepos;
    private UserRepos userRepos;
    private AnswerRepos answerRepos;
    private UserService userService;


    @Autowired
    public AnswerService(TaskRepos taskRepos, UserRepos userRepos, AnswerRepos answerRepos,
                         UserService userService) {
        this.taskRepos = taskRepos;
        this.userRepos = userRepos;
        this.answerRepos = answerRepos;
        this.userService = userService;
    }


    @Transactional
    public void createAnswer(String body, Task task, Student student) {
        Answer answer = new Answer(student, task, body);
        answerRepos.save(answer);
        taskRepos.addAnswerToTask(task.getId(), answer.getId());
    }


    @Transactional
    public void updateAnswer(String rating, String comment, Answer answer) {
        answerRepos.answerChecked(answer.getId(), Byte.parseByte(rating), comment);
    }

    public Boolean isStudentInClassSetOfTeacher(Teacher teacher, Answer answer) {
        for (SchoolClass sc : teacher.getSchoolClassSet()) {
            for (Student st : sc.getStudents()) {
                if (st.fastEqualsById(answer.getStudent())) {
                    return true;
                }
            }
        }
        return false;
    }

}
