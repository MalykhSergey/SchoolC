package general.reposes;

import general.entities.School;
import general.entities.SchoolClass;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface SchoolClassRepos extends CrudRepository<SchoolClass, Long> {
    SchoolClass findSchoolClassByName(String Name);
}
