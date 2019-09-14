package general.reposes;

import general.entities.School;
import general.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepos extends CrudRepository<User, Long> {
    public User findUserByNameAndSchool(String name, School school);
}
