package general.reposes;

import org.springframework.data.repository.CrudRepository;

import general.entities.Answer;

public interface AnswerRepos extends CrudRepository<Answer, Long> {
    Answer findAnswerById(Long id);
}
