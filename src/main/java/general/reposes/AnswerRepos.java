package general.reposes;

import general.entities.Answer;
import general.entities.Student;
import general.entities.Task;
import general.entities.Teacher;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AnswerRepos extends CrudRepository<Answer, Long> {
    Answer findAnswerById(Long id);

    List<Answer> findAllByStudent(Student student);

    List<Answer> findAllByStudentAndTeacher(Student student, Teacher teacher);

    @Modifying
    @Query(value = "UPDATE answers SET rating = ?2, comment = ?3 WHERE id = ?1", nativeQuery = true)
    void checkAnswer(Long answerId, int rating, String comment);

    Answer findByStudentAndTask(Student student, Task task);
}
