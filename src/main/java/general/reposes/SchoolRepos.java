package general.reposes;

import general.entities.School;
import org.springframework.data.repository.CrudRepository;

public interface SchoolRepos extends CrudRepository<School, Long> {
    School findSchoolByName(String Name);
}
