package general.services;

import general.entities.*;
import general.reposes.AnswerRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnswerService {
    private final AnswerRepos answerRepos;


    @Autowired
    public AnswerService(AnswerRepos answerRepos) {
        this.answerRepos = answerRepos;
    }


    @Transactional
    public void createAnswer(String body, Task task, Student student) {
        Answer answer = new Answer(student, task, body);
        answerRepos.save(answer);
    }


    @Transactional
    public void updateAnswer(String rating, String comment, Answer answer) {
        answerRepos.answerChecked(answer.getId(), Byte.parseByte(rating), comment);
    }

    public Answer getByStudentAndTask(Student student, Task task) {
        return answerRepos.findByStudentAndTask(student, task);
    }

    public List<Answer> getAnswersByStudent(Student student) {
        return answerRepos.findAllByStudent(student);
    }

    public List<Answer> getAnswersByStudentAndTeacher(Student student, Teacher teacher) {
        return answerRepos.findAllByStudentAndTeacher(student, teacher);
    }

    public Answer getAnswerById(Long id) {
        return answerRepos.findAnswerById(id);
    }

}
