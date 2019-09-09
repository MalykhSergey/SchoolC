package general.reposes;

import general.entities.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepos extends CrudRepository <Student, Long> {
}
