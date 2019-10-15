package general.reposes;

import general.entities.Answer;
import org.springframework.data.repository.CrudRepository;

public interface AnswerRepos extends CrudRepository<Answer, Long> {
}
