package general.services;

import general.entities.Answer;
import general.entities.Student;
import general.entities.Task;
import general.entities.Teacher;
import general.reposes.AnswerRepos;
import general.utils.Result;
import general.utils.StringLengthConstants;
import general.utils.UserDetailsExtended;
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
    public Result createAnswer(String body, Task task, Student student) {
        if (getByStudentAndTask(student, task) != null) return Result.AnswerIsExists;
        if (body.length() < StringLengthConstants.AnswerBody.getMinLength()) return Result.TooShortAnswerBody;
        if (body.length() > StringLengthConstants.AnswerBody.getMaxLength()) return Result.TooLongAnswerBody;
        if (!student.getSchoolClass().fastEqualsById(task.getSchoolClass())) return Result.InvalidClassName;
        Answer answer = new Answer(student, task, body);
        answerRepos.save(answer);
        return Result.Ok;
    }


    @Transactional
    public Result checkAnswer(Answer answer, String comment, int rating, UserDetailsExtended userDetailsExtended) {
        if (answer == null) return Result.AnswerIsNotExists;
        if (rating > 5 || rating < 2) return Result.InvalidRating;
        if (!answer.getTeacher().fastEqualsById(userDetailsExtended.getUser())) return Result.InvalidTeacher;
        answerRepos.checkAnswer(answer.getId(), rating, comment);
        return Result.Ok;
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
