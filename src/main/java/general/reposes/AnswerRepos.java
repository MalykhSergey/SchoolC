package general.reposes;

import general.entities.Answer;
import general.entities.Task;
import org.springframework.data.repository.CrudRepository;

public interface AnswerRepos extends CrudRepository<Answer, Long> {
    Answer findAnswerById(Long id);
}
