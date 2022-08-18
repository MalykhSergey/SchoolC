package general.service;

import general.entity.Answer;
import general.entity.Student;
import general.entity.Task;
import general.entity.Teacher;
import general.repository.AnswerRepository;
import general.util.Result;
import general.util.StringLengthConstants;
import general.util.UserDetailsExtended;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnswerService {
    private final AnswerRepository answerRepository;


    @Autowired
    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }


    @Transactional
    public Result createAnswer(String body, Task task, Student student) {
        if (answerRepository.findByStudentAndTask(student, task) != null) return Result.AnswerIsExists;
        if (body.length() < StringLengthConstants.AnswerBody.getMinLength()) return Result.TooShortAnswerBody;
        if (body.length() > StringLengthConstants.AnswerBody.getMaxLength()) return Result.TooLongAnswerBody;
        if (!student.getSchoolClass().fastEqualsById(task.getSchoolClass())) return Result.InvalidClassName;
        Answer answer = new Answer(student, task, body);
        answerRepository.save(answer);
        return Result.Ok;
    }


    @Transactional
    public Result checkAnswer(Answer answer, String comment, int rating, UserDetailsExtended userDetailsExtended) {
        if (answer == null) return Result.AnswerIsNotExists;
        if (rating > 5 || rating < 2) return Result.InvalidRating;
        if (!answer.getTeacher().fastEqualsById(userDetailsExtended.getUser())) return Result.InvalidTeacher;
        answerRepository.checkAnswer(answer.getId(), rating, comment);
        return Result.Ok;
    }
    public List<Answer> getAnswersByStudent(Student student) {
        return answerRepository.findAllByStudent(student);
    }

    public List<Answer> getAnswersByStudentAndTeacher(Student student, Teacher teacher) {
        return answerRepository.findAllByStudentAndTeacher(student, teacher);
    }

    public Answer getAnswerById(Long id) {
        return answerRepository.findAnswerById(id);
    }

}
